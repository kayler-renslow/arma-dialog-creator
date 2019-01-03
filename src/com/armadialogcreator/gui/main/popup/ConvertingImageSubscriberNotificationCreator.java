package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.data.ImagesTool;
import com.armadialogcreator.gui.notification.BoxNotification;
import com.armadialogcreator.gui.notification.Notification;
import com.armadialogcreator.gui.notification.Notifications;
import com.armadialogcreator.lang.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ResourceBundle;

/**
 A {@link Notification} spawner that creates notifications when
 {@link ImagesTool#getImageFile(String, ImagesTool.ImageConversionCallback)} is
 invoked.

 @author Kayler
 @see ConvertingImageSubscriberDialog
 @since 06/29/2017 */
public class ConvertingImageSubscriberNotificationCreator implements ImagesTool.ImageConversionSubscriber {
	private final ResourceBundle bundle = Lang.ApplicationBundle();

	@Override
	public void conversionStarted(@NotNull File image) {
		Notifications.showNotification(
				new MyNotification(
						String.format(
								bundle.getString("Notifications.ConvertingImage.started_f"),
								image.getName()
						),
						false
				)
		);
	}

	@Override
	public void conversionFailed(@NotNull File image, @Nullable Exception e) {
		Notifications.showNotification(
				new MyNotification(
						String.format(
								bundle.getString("Notifications.ConvertingImage.failed_f"),
								image.getName()
						),
						true
				)
		);
	}

	@Override
	public void conversionSucceeded(@NotNull File image, @Nullable File resultFile) {
		Notifications.showNotification(
				new MyNotification(
						String.format(
								bundle.getString("Notifications.ConvertingImage.succeeded_f"),
								image.getName()
						),
						false
				)
		);
	}

	@Override
	public void conversionCancelled(@NotNull File image) {
		Notifications.showNotification(
				new MyNotification(
						String.format(
								bundle.getString("Notifications.ConvertingImage.cancelled_f"),
								image.getName()
						),
						false
				)
		);
	}

	private class MyNotification extends BoxNotification {

		public MyNotification(@NotNull String msg, boolean isError) {
			super(bundle.getString("Notifications.ConvertingImage.title"), msg, 10 * 1000, isError);
		}
	}
}
