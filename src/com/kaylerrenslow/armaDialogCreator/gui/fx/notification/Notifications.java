/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.notification;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 @author Kayler
 @since 11/16/2016 */
public class Notifications {
	private static LinkedList<NotificationWrapper> showingNotifications = new LinkedList<>();
	private static NotificationsVisibilityTask visibilityTask = new NotificationsVisibilityTask();

	public static void showNotification(@NotNull Notification notification) {
		showingNotifications.add(new NotificationWrapper(notification, System.currentTimeMillis()));
		ArmaDialogCreator.getMainWindow().getNotificationPane().addNotification(notification);
		if (!visibilityTask.isRunning()) {
			new Thread(visibilityTask).start();
		}
	}

	private static class NotificationWrapper {
		private final Notification notification;
		private final long timeShown;

		public NotificationWrapper(@NotNull Notification notification, long timeShown) {
			this.notification = notification;
			this.timeShown = timeShown;
		}
	}

	private static class NotificationsVisibilityTask extends Task<Boolean> {


		@Override
		protected Boolean call() throws Exception {
			LinkedList<NotificationWrapper> tohide = new LinkedList<>();
			while (true) {
				long now = System.currentTimeMillis();
				for (int i = 0; i < showingNotifications.size(); ) {
					NotificationWrapper wrapper = showingNotifications.get(i);
					if (!wrapper.notification.isShowing() || wrapper.timeShown + wrapper.notification.getDisplayDurationMilliseconds() <= now) {
						NotificationWrapper remove = showingNotifications.remove(i);
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
								tohide.removeFirst().notification.setShowing(false);
							}
						}
					});
				}
				if (showingNotifications.size() == 0) {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
					}
				} else {
					try {
						Thread.sleep(20);
					} catch (Exception e) {
					}
				}
			}

		}
	}
}
