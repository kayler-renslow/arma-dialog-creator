package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.data.ImagesTool;
import com.armadialogcreator.gui.StagePopup;
import com.armadialogcreator.lang.Lang;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ResourceBundle;

/**
 A dialog that automatically appears when {@link ImagesTool#getImageFile(String, ImagesTool.ImageConversionCallback)}
 is invoked.
 <p>
 This differs from {@link ConvertImageTask} because this has no user input and doesn't convert anything. This dialog is
 used for {@link ImagesTool#subscribeToConversion(ImagesTool.ImageConversionSubscriber)}.

 @author Kayler
 @since 06/29/2017 */
public class ConvertingImageSubscriberDialog extends StagePopup<VBox>
		implements ImagesTool.ImageConversionSubscriber {

	private final ProgressBar progressBar = new ProgressBar(0);
	private final ResourceBundle bundle = Lang.ApplicationBundle();
	private final Label lblConvertingImg = new Label();
	private volatile long shownTime;

	public ConvertingImageSubscriberDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null);

		setTitle(bundle.getString("Popups.ImageConversion.popup_title"));

		myStage.initModality(Modality.APPLICATION_MODAL);

		myRootElement.setPadding(new Insets(10));
		myStage.setResizable(false);

		myRootElement.setMinWidth(240);
		myRootElement.getChildren().add(lblConvertingImg);
		myRootElement.getChildren().add(progressBar);

		myStage.setAlwaysOnTop(true);
	}

	private void conversionError(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new ConversionFailPopup(msg).showAndWait();
			}
		});
	}

	@Override
	public void conversionStarted(@NotNull File image) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				shownTime = System.currentTimeMillis();
				show();
				myStage.sizeToScene();
				lblConvertingImg.setText(
						String.format(
								bundle.getString("Popups.ImageConversion.message_f"),
								image.getName()
						)
				);
				progressBar.setProgress(-1);
			}
		});
	}

	@Override
	public void conversionFailed(@NotNull File image, @Nullable Exception e) {
		conversionError((e != null && e.getMessage() != null) ? e.getMessage()
				: bundle.getString("Popups.ImageConversion.unknown_image_conversion_error"));
		close(false);
	}

	@Override
	public void conversionSucceeded(@NotNull File image, @Nullable File resultFile) {
		close(true);
	}

	@Override
	public void conversionCancelled(@NotNull File image) {
		close(false);
	}

	private void close(boolean wait) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//wait for the dialog to at least appear before closing it
					if (System.currentTimeMillis() - shownTime < 100) {
						Thread.sleep(500);
					}

					if (wait) {
						Thread.sleep(500); //show that there was success for a brief moment to not to confuse user
					}

				} catch (InterruptedException ignore) {

				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ConvertingImageSubscriberDialog.this.close();
					}
				});
			}
		}).start();
	}

	private static class ConversionFailPopup extends StagePopup<VBox> {

		public ConversionFailPopup(@NotNull String message) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null);

			ResourceBundle bundle = Lang.ApplicationBundle();
			setTitle(bundle.getString("Popups.ImageConversion.convert_error_popup_title"));

			myStage.initModality(Modality.APPLICATION_MODAL);
			myRootElement.setPadding(new Insets(10));
			myRootElement.getChildren().addAll(new HBox(10, new ImageView("/com/armadialogcreator/gui/img/icons/error64.png"), new Label(message)));
			myStage.setMinWidth(340d);

			myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(false, true, false));
		}
	}

}
