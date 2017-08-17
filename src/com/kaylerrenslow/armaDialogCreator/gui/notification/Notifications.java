package com.kaylerrenslow.armaDialogCreator.gui.notification;

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 Handles all {@link Notification} instances that are to be displayed. When a {@link Notification} is shown via {@link #showNotification(Notification)} or {@link #showNotification(Notification, NotificationPane)},
 {@link #getPastNotifications()} will have that notification appended (if {@link Notification#saveToHistory()}) and the notification will be displayed.

 @author Kayler
 @since 11/16/2016 */
public class Notifications {
	private static final Notifications INSTANCE = new Notifications();
	private static final int MAX_HISTORY_NOTIFICATIONS = 15;

	//this list doesn't need to be synchronized since we are synchronizing on INSTANCE
	private static final List<Notification> showWhenInitialized = new LinkedList<>();

	/**
	 Shows the specified {@link Notification}

	 @param notification notification to show
	 @param pane pane to place the notification on
	 */
	public static void showNotification(@NotNull Notification notification, @NotNull NotificationPane pane) {
		synchronized (INSTANCE) {
			INSTANCE.doShowNotification(notification, pane);
		}
	}

	/**
	 Shows the specified {@link Notification} on {@link #getNotificationPane()}. If the pane is undefined at the time of this call,
	 the notification will be placed on a queue and made visible when it is defined with {@link #setDefaultNotificationPane(NotificationPane)}.

	 @param notification notification to show
	 */
	public static void showNotification(@NotNull Notification notification) {
		synchronized (INSTANCE) {
			if (INSTANCE.notificationPane == null) {
				showWhenInitialized.add(notification);
				return;
			}
			INSTANCE.doShowNotification(notification, INSTANCE.notificationPane);
		}
	}

	/** Return a list of past notifications that were displayed. */
	@NotNull
	public static ReadOnlyList<NotificationDescriptor> getPastNotifications() {
		synchronized (INSTANCE) {
			return INSTANCE.pastNotificationsReadOnly;
		}
	}

	/**
	 Set the {@link NotificationPane} to display the notifications that are invoked from {@link #showNotification(Notification)}. This must be invoked before {@link #showNotification(Notification)}.

	 @param notificationPane the pane
	 */
	public static void setDefaultNotificationPane(@NotNull NotificationPane notificationPane) {
		synchronized (INSTANCE) {
			INSTANCE.notificationPane = notificationPane;
			for (Notification notification : showWhenInitialized) {
				INSTANCE.doShowNotification(notification, INSTANCE.notificationPane);
			}
			showWhenInitialized.clear();
		}
	}

	/** {@link #setDefaultNotificationPane(NotificationPane)} */
	@Nullable
	public static NotificationPane getNotificationPane() {
		synchronized (INSTANCE) {
			return INSTANCE.notificationPane;
		}
	}

	private Notifications() {
	}


	private final List<NotificationDescriptor> toShowNotifications = Collections.synchronizedList(new LinkedList<>());
	private final List<NotificationDescriptor> showingNotifications = Collections.synchronizedList(new LinkedList<>());
	private volatile Thread visibilityThread;
	private final List<NotificationDescriptor> pastNotifications = Collections.synchronizedList(new LinkedList<>());
	private final ReadOnlyList<NotificationDescriptor> pastNotificationsReadOnly = new ReadOnlyList<>(pastNotifications);
	private NotificationPane notificationPane;

	private synchronized void doShowNotification(@NotNull Notification notification, @NotNull NotificationPane notificationPane) {
		NotificationDescriptor descriptor = new NotificationDescriptor(notification, System.currentTimeMillis(), notificationPane);
		if (notification.saveToHistory()) {
			pastNotifications.add(descriptor);
			while (pastNotifications.size() >= MAX_HISTORY_NOTIFICATIONS) {
				pastNotifications.remove(0);
			}
		}
		toShowNotifications.add(descriptor);
		if (visibilityThread == null || !visibilityThread.isAlive()) {
			visibilityThread = new Thread(new NotificationsVisibilityTask(this));
			visibilityThread.setName("ADC - Notifications Thread");
			visibilityThread.setDaemon(true);
			visibilityThread.start();
		}
	}

	private static class NotificationsVisibilityTask implements Runnable {

		private final Notifications notifications;
		private final List<NotificationDescriptor> tohide = Collections.synchronizedList(new LinkedList<>());
		private int doNothingIterations = 0;

		public NotificationsVisibilityTask(@NotNull Notifications notifications) {
			this.notifications = notifications;
		}

		public void run() {
			while (doNothingIterations < 10) {
				long now = System.currentTimeMillis();
				for (int i = 0; i < notifications.showingNotifications.size(); ) {
					NotificationDescriptor wrapper = notifications.showingNotifications.get(i);
					if (!wrapper.getNotification().isShowing() || wrapper.getTimeShown() + wrapper.getNotification().getDisplayDurationMilliseconds() <= now) {
						tohide.add(notifications.showingNotifications.remove(i));
						continue;
					}
					i++;
				}
				if (tohide.size() > 0 || !notifications.toShowNotifications.isEmpty()) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							synchronized (notifications.toShowNotifications) {
								for (NotificationDescriptor d : notifications.toShowNotifications) {
									d.getPane().addNotification(d.getNotification());
									notifications.showingNotifications.add(d);
								}
								notifications.toShowNotifications.clear();
							}

							while (tohide.size() > 0) {
								tohide.remove(0).getNotification().setShowing(false);
							}
						}
					});
				}
				if (notifications.showingNotifications.size() == 0) {
					doNothingIterations++;
					try {
						Thread.sleep(300);
					} catch (Exception ignore) {
					}
				} else {
					Thread.yield();
				}
			}

		}
	}
}
