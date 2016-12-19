package com.kaylerrenslow.armaDialogCreator.gui.notification;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see Notifications
 @since 11/17/2016 */
public class NotificationDescriptor {
	private final Notification notification;
	private final long timeShown;

	NotificationDescriptor(@NotNull Notification notification, long timeShown) {
		this.notification = notification;
		this.timeShown = timeShown;
	}

	@NotNull
	public Notification getNotification() {
		return notification;
	}

	public long getTimeShown() {
		return timeShown;
	}

	@Override
	public String toString() {
		return notification.getNotificationText();
	}
}
