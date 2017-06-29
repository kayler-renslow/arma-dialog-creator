package com.kaylerrenslow.armaDialogCreator.gui.notification;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see Notifications
 @since 11/17/2016 */
public class NotificationDescriptor {
	private final Notification notification;
	private final long timeShown;
	private final NotificationPane pane;

	NotificationDescriptor(@NotNull Notification notification, long timeShown, @NotNull NotificationPane pane) {
		this.notification = notification;
		this.timeShown = timeShown;
		this.pane = pane;
	}

	@NotNull
	public Notification getNotification() {
		return notification;
	}

	public long getTimeShown() {
		return timeShown;
	}

	@NotNull
	NotificationPane getPane() {
		return pane;
	}

	@Override
	public String toString() {
		return notification.getNotificationText();
	}
}
