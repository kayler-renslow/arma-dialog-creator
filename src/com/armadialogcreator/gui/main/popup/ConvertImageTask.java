package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.data.ApplicationSettingsManager;
import com.armadialogcreator.data.olddata.ApplicationProperty;
import com.armadialogcreator.data.olddata.ImagesTool;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.StagePopup;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.KeyValue;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;

/**
 A task that converts a .paa image to a .png.
 This class uses {@link ImagesTool#getImageFile(String, ImagesTool.ImageConversionCallback)} for converting images.
 <p>
 This class properly handles retrieving Arma 3 tools directory if not set by presenting a GUI to the user.
 <p>
 This task is not closable, however, if the conversion takes too long, the task will shutdown automatically.

 @author Kayler
 @see ConvertingImageSubscriberDialog
 @since 06/29/2017 */
public class ConvertImageTask {

	private enum Option {
		CANCEL(""),
		OVERWRITE(Lang.ApplicationBundle().getString("ValueEditors.ImageValueEditor.ImageAlreadyExistsDialog.overwrite")),
		NEW_NAME(Lang.ApplicationBundle().getString("ValueEditors.ImageValueEditor.ImageAlreadyExistsDialog.new_name"));

		private final String displayName;

		Option(String displayName) {
			this.displayName = displayName;
		}
	}

	private final ConvertPaaTask convertPaaTask;
	private final Callback<KeyValue<File, File>, Void> callback;

	private final LinkedBlockingQueue q = new LinkedBlockingQueue();
	private final Object closeTask = new Object();

	/**
	 @param paaImageFile paa image to convert
	 @param callback callback to use when conversion completes, errors, or cancels
	 The key is the file that was requested to be converted.
	 The value is the resulted file. The key will not be null, however, the value may be null.
	 */
	public ConvertImageTask(@NotNull File paaImageFile, @NotNull Callback<KeyValue<File, File>, Void> callback) {
		this.callback = callback;

		convertPaaTask = new ConvertPaaTask(paaImageFile);
	}

	public void start() {
		Thread thread = new Thread(convertPaaTask, "ADC - ConvertImageTask.java");
		thread.setDaemon(true);
		thread.start();
	}


	private class ConvertPaaTask implements Runnable, ImagesTool.ImageConversionCallback {

		private final File toConvert;
		private KeyValue<File, File> value;

		public ConvertPaaTask(@NotNull File toConvert) {
			this.toConvert = toConvert;
			value = new KeyValue<>(toConvert, null);
		}

		@Override
		public void run() {
			ImagesTool.getImageFile(toConvert.getPath(), this);

			callback.call(value);
		}

		@Override
		public @Nullable String replaceExistingConvertedImage(@NotNull File image, @NotNull File destination) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ImageAlreadyExistsDialog dialog = new ImageAlreadyExistsDialog(destination);
					dialog.show();
				}
			});

			try {
				Object take = q.take();
				if (take == closeTask) {
					return null;
				}
				Option option = (Option) take;

				switch (option) {
					case CANCEL: {
						return null;
					}
					case NEW_NAME: {
						return destination.getName() + ".new.png";
					}
					case OVERWRITE: {
						return destination.getName();
					}
					default: {
						throw new IllegalStateException("unexpected option (bug):" + option);
					}
				}
			} catch (InterruptedException ignore) {

			}
			return null;
		}

		@Override
		public @Nullable File arma3ToolsDirectory() {
			File a3Tools = ApplicationProperty.A3_TOOLS_DIR.getValue();
			if (a3Tools != null) {
				return a3Tools;
			}

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Arma3ToolsDirNotSetPopup popup = new Arma3ToolsDirNotSetPopup();
					popup.show();
				}
			});
			try {
				Object take = q.take();
				if (take == closeTask) {
					return null;
				}
				return (File) take;
			} catch (InterruptedException ignore) {

			}
			return null;
		}

		@Override
		public void conversionStarted(@NotNull File image) {

		}

		@Override
		public void conversionFailed(@NotNull File image, @Nullable Exception e) {

		}

		@Override
		public void conversionSucceeded(@NotNull File image, @Nullable File resultFile) {
			value = new KeyValue<>(image, resultFile);
		}

		@Override
		public void conversionCancelled(@NotNull File image) {

		}
	}

	private class Arma3ToolsDirNotSetPopup extends StagePopup<VBox> {

		public Arma3ToolsDirNotSetPopup() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null);
			ResourceBundle bundle = Lang.ApplicationBundle();

			setTitle(bundle.getString("Popups.generic_popup_title"));

			myStage.initModality(Modality.APPLICATION_MODAL);
			myStage.setResizable(false);

			Button btnLocate = new Button(bundle.getString("ValueEditors.ImageValueEditor.set_a3_tools_btn"));
			btnLocate.setOnAction(new EventHandler<>() {
				@Override
				public void handle(ActionEvent event) {
					new ChangeDirectoriesDialog().showAndWait();
					close();
				}
			});

			myRootElement.setPadding(new Insets(10));
			myRootElement.getChildren().add(new Label(bundle.getString("ValueEditors.ImageValueEditor.a3_tools_dir_not_set")));
			myRootElement.getChildren().add(btnLocate);

			myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(true, true, false));
		}

		@Override
		protected void closing() {
			File f = ApplicationSettingsManager.instance.getSettings().ArmaToolsSetting.get();
			if (f == null) {
				q.add(closeTask);
			} else {
				q.add(f);
			}
		}
	}

	private class ImageAlreadyExistsDialog extends StageDialog<VBox> {


		private final ToggleGroup toggleGroup = new ToggleGroup();
		private Option selectedOption = Option.OVERWRITE;

		/**
		 @param existingImage the .png image file that already exists
		 */
		public ImageAlreadyExistsDialog(@NotNull File existingImage) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, false);
			if (!existingImage.getName().endsWith(".png")) {
				throw new IllegalArgumentException("not a png image");
			}

			ResourceBundle bundle = Lang.ApplicationBundle();

			setTitle(bundle.getString("ValueEditors.ImageValueEditor.ImageAlreadyExistsDialog.dialog_title"));

			myRootElement.getChildren().add(new Label(String.format(bundle.getString("ValueEditors.ImageValueEditor.ImageAlreadyExistsDialog.message_f"), existingImage.getName())));

			final ImageView imageViewPreviewImage;
			try {
				imageViewPreviewImage = new ImageView(new Image(new FileInputStream(existingImage.getPath())));
			} catch (FileNotFoundException e) {
				ExceptionHandler.error(e);
				return;
			}
			myRootElement.getChildren().add(new VBox(5, new Label(bundle.getString("ValueEditors.ImageValueEditor.ImageAlreadyExistsDialog.existing_image")), imageViewPreviewImage));
			imageViewPreviewImage.setFitWidth(400d);
			imageViewPreviewImage.setFitHeight(400d);

			final VBox vboxOptions = new VBox(5);
			myRootElement.getChildren().add(vboxOptions);

			for (Option option : Option.values()) {
				if (option == Option.CANCEL) {
					continue;
				}
				RadioButton radioButton = new RadioButton(option.displayName);
				radioButton.setToggleGroup(toggleGroup);
				vboxOptions.getChildren().add(radioButton);
				radioButton.setUserData(option);
			}
			for (Toggle toggle : toggleGroup.getToggles()) {
				if (toggle.getUserData() == selectedOption) {
					toggleGroup.selectToggle(toggle);
					break;
				}
			}
		}

		@Override
		protected void ok() {
			selectedOption = (Option) toggleGroup.getSelectedToggle().getUserData();
			super.ok();
		}

		@Override
		protected void cancel() {
			selectedOption = Option.CANCEL;
			super.cancel();
		}

		@Override
		protected void closing() {
			q.add(selectedOption);
		}
	}

}
