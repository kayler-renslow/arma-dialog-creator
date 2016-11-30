package com.kaylerrenslow.armaDialogCreator.gui.fx.notification;

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 Handles all {@link Notification} instances that are to be displayed. When a {@link Notification} is shown via {@link #showNotification(Notification)}, {@link #getPastNotifications()} will have
 that notification appended and the notification will be displayed.

 @author Kayler
 @since 11/16/2016 */
public class Notifications {
	private static final Notifications INSTANCE = new Notifications();
	private static final int MAX_HISTORY_NOTIFICATIONS = 15;


	/**
	 Shows the specified {@link Notification}

	 @param notification notification to show
	 @throws IllegalStateException when {@link #setNotificationPane(NotificationPane)} is not invoked prior to this call
	 */
	public static void showNotification(@NotNull Notification notification) {
		if (INSTANCE.notificationPane == null) {
			throw new IllegalStateException("notificationPane is not set from setNotificationPane()");
		}
		INSTANCE.doShowNotification(notification);
	}

	/** Return a list of past notifications that were displayed. */
	@NotNull
	public static ReadOnlyList<NotificationDescriptor> getPastNotifications() {
		return INSTANCE.pastNotificationsReadOnly;
	}

	/**
	 Set the {@link NotificationPane} to display the notifications on. This must be invoked before {@link #showNotification(Notification)}.

	 @param notificationPane the pane
	 */
	public static void setNotificationPane(@NotNull NotificationPane notificationPane) {
		INSTANCE.notificationPane = notificationPane;
	}

	private Notifications() {
	}


	private final LinkedList<NotificationDescriptor> showingNotifications = new LinkedList<>();
	private final NotificationsVisibilityTask visibilityTask = new NotificationsVisibilityTask(this);
	private final LinkedList<NotificationDescriptor> pastNotifications = new LinkedList<>();
	private final ReadOnlyList<NotificationDescriptor> pastNotificationsReadOnly = new ReadOnlyList<>(pastNotifications);
	private NotificationPane notificationPane;

	private void doShowNotification(@NotNull Notification notification) {
		NotificationDescriptor descriptor = new NotificationDescriptor(notification, System.currentTimeMillis());
		pastNotifications.add(descriptor);
		if (pastNotifications.size() >= MAX_HISTORY_NOTIFICATIONS) {
			while (pastNotifications.size() >= MAX_HISTORY_NOTIFICATIONS) {
				pastNotifications.removeFirst();
			}
		}
		showingNotifications.add(descriptor);
		notificationPane.addNotification(notification);
		if (!visibilityTask.isRunning()) {
			Thread thread = new Thread(visibilityTask);
			thread.setDaemon(true);
			thread.start();
		}
	}

	private static class NotificationsVisibilityTask extends Task<Boolean> {

		private final Notifications notifications;

		public NotificationsVisibilityTask(Notifications notifications) {
			this.notifications = notifications;
		}

		@Override
		protected Boolean call() throws Exception {
			LinkedList<NotificationDescriptor> tohide = new LinkedList<>();
			while (true) {
				long now = System.currentTimeMillis();
				for (int i = 0; i < notifications.showingNotifications.size(); ) {
					NotificationDescriptor wrapper = notifications.showingNotifications.get(i);
					if (!wrapper.getNotification().isShowing() || wrapper.getTimeShown() + wrapper.getNotification().getDisplayDurationMilliseconds() <= now) {
						NotificationDescriptor remove = notifications.showingNotifications.remove(i);
						tohide.add(remove);
						continue;
					}
					i++;
				}
				if (tohide.size() > 0) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							while (tohide.size() > 0) {
								tohide.removeFirst().getNotification().setShowing(false);
							}
						}
					});
				}
				if (notifications.showingNotifications.size() == 0) {
					try {
						Thread.sleep(2000);
					} catch (Exception ignore) {
					}
				} else {
					try {
						Thread.sleep(20);
					} catch (Exception ignore) {
					}
				}
			}

		}
	}
}
