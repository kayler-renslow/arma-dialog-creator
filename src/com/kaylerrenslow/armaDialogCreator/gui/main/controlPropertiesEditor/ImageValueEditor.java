package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaTools;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVImage;
import com.kaylerrenslow.armaDialogCreator.data.*;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.SelectSaveLocationPopup;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ADCStatic;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

/**
 A ValueEditor implementation for selecting image files. This editor also has an implementation for discovering if the image file is a .paa or .tga file and will automatically
 convert it to a read-able format and store it.

 @author Kayler
 @since 07/16/2016. */
public class ImageValueEditor implements ValueEditor<SVImage> {
	private final ResourceBundle bundle = Lang.ApplicationBundle();

	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private final Button btnChooseImage = new Button(bundle.getString("ValueEditors.ImageValueEditor.locate_image"));

	protected final TextField tfFilePath = new TextField("");

	private final HBox hBox = new HBox(5, btnChooseImage, tfFilePath);

	private final StackPane masterPane = new StackPane(hBox);

	protected @Nullable SVImage imageValue = null;

	private final ValueObserver<SVImage> valueObserver = new ValueObserver<>(null);

	public ImageValueEditor() {
		HBox.setHgrow(tfFilePath, Priority.ALWAYS);
		tfFilePath.setEditable(false);
		btnChooseImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fc = new FileChooser();
				fc.setTitle(bundle.getString("ValueEditors.ImageValueEditor.locate_image"));
				fc.getExtensionFilters().addAll(ADCStatic.IMAGE_FILE_EXTENSIONS);
				fc.setInitialDirectory(Workspace.getWorkspace().getWorkspaceDirectory());
				File chosenFile = fc.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
				if (chosenFile == null) {
					return;
				}
				chooseImage(chosenFile);
			}
		});
	}

	private void chooseImage(@NotNull File chosenFile) {
		if (chosenFile.getName().endsWith(".paa")) {
			convertImage(chosenFile);
		} else {
			setValue(new SVImage(chosenFile, chosenFile));
		}
	}

	private void convertImage(File chosenFile) {
		File a3Tools = ArmaDialogCreator.getApplicationDataManager().getArma3ToolsDirectory();
		if (a3Tools == null) {
			Arma3ToolsDirNotSetPopup setPopup = new Arma3ToolsDirNotSetPopup();
			setPopup.showAndWait();
			a3Tools = setPopup.getA3ToolsDir();
			if (a3Tools == null) {
				return;
			}
		}
		ConvertingPaaPopup convertingPaaPopup = new ConvertingPaaPopup(this, chosenFile, a3Tools);
		convertingPaaPopup.show();

	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	@Nullable
	public SVImage getValue() {
		return imageValue;
	}

	@Override
	public void setValue(SVImage val) {
		imageValue = val;
		valueObserver.updateValue(val);
		if (val == null) {
			tfFilePath.setText("");
		} else {
			tfFilePath.setText(val.toString());
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	public void setToCustomData(boolean override) {
		masterPane.getChildren().clear();
		if (override) {
			masterPane.getChildren().add(overrideField);
		} else {
			masterPane.getChildren().add(hBox);
		}
	}

	@Override
	public InputField<StringChecker, String> getCustomDataTextField() {
		return overrideField;
	}

	@Override
	public void focusToEditor() {
		btnChooseImage.requestFocus();
	}

	@Override
	public ReadOnlyValueObserver<SVImage> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

	@Override
	public boolean displayFullWidth() {
		return true;
	}

	private static class ConvertingPaaPopup extends StagePopup<VBox> {

		private final ProgressBar progressBar = new ProgressBar(0);
		private ConvertPaaTask convertPaaTask;
		private boolean errorShown = false;
		private boolean dontShow = false;
		private final ResourceBundle bundle = Lang.ApplicationBundle();

		public ConvertingPaaPopup(ImageValueEditor imageValueEditor, File convertingFile, File a3tools) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null);

			setTitle(bundle.getString("ValueEditors.ImageValueEditor.ConvertingPaaPopup.popup_title"));

			myStage.initModality(Modality.APPLICATION_MODAL);
			myRootElement.setPadding(new Insets(10));
			myStage.setResizable(false);

			myRootElement.getChildren().add(new Label(String.format(bundle.getString("ValueEditors.ImageValueEditor.ConvertingPaaPopup.message_f"), convertingFile.getName())));
			myRootElement.getChildren().add(progressBar);

			initTask(imageValueEditor, convertingFile, a3tools);
		}

		private void initTask(ImageValueEditor imageValueEditor, File chosenFile, File a3Tools) {
			File convertDest = WorkspaceResourceRegistry.getInstance().getFileForName(chosenFile.getName() + ".png");

			if (convertDest.exists()) {
				ImageAlreadyExistsDialog dialog = new ImageAlreadyExistsDialog(convertDest);
				dialog.show();
				ImageAlreadyExistsDialog.Option option = dialog.getSelectedOption();
				switch (option) {
					case CANCEL: {
						dontShow = true;
						return;
					}
					case NEW_NAME: {
						convertDest = new File(convertDest.getPath() + ".new.png");
						break;
					}
					case OVERWRITE: {
						break;
					}
					default: {
						throw new IllegalStateException("unexpected option (bug):" + option);
					}
				}
			}

			convertPaaTask = new ConvertPaaTask(chosenFile, convertDest, a3Tools);
			progressBar.progressProperty().bind(convertPaaTask.progressProperty());
			convertPaaTask.exceptionProperty().addListener(new ChangeListener<Throwable>() {
				@Override
				public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
					newValue.printStackTrace();
					conversionError(newValue.getMessage() != null ? newValue.getMessage() : bundle.getString("ValueEditors.ImageValueEditor.ConvertingPaaPopup.unknown_image_conversion_error"));
				}
			});
			convertPaaTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					imageValueEditor.setValue(convertPaaTask.getValue());
					close();
				}
			});
			convertPaaTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					if (convertPaaTask.isCancelWithFailMessage()) {
						conversionError(bundle.getString("ValueEditors.ImageValueEditor.ConvertingPaaPopup.unknown_image_conversion_error"));
					}
				}
			});
			convertPaaTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					conversionError(bundle.getString("ValueEditors.ImageValueEditor.ConvertingPaaPopup.unknown_image_conversion_error"));
				}
			});

			myStage.setOnShowing(new EventHandler<WindowEvent>() {//prevent the conversion thread being finished before the popup is shown
				@Override
				public void handle(WindowEvent event) {
					Thread thread = new Thread(convertPaaTask);
					thread.setDaemon(true);
					thread.start();
				}
			});
		}

		@Override
		public void show() {
			if (dontShow) {
				return;
			}
			super.show();
		}

		private void conversionError(String msg) {
			if (errorShown) { //prevent the popup showing twice
				return;
			}
			errorShown = true;
			new ConversionFailPopup(msg).showAndWait();
			close();
		}


		@Override
		protected void onCloseRequest(WindowEvent event) {
			event.consume();
		}
	}

	private static class ConvertPaaTask extends Task<SVImage> {

		private final File toConvert;
		private final File convertDest;
		private final File a3Tools;

		private static final long TIMEOUT = 1000 * 10; //ten seconds

		private boolean cancelWithFailMessage = true;

		public ConvertPaaTask(@NotNull File toConvert, @NotNull File convertDest, @NotNull File a3Tools) {
			this.toConvert = toConvert;
			this.convertDest = convertDest;
			this.a3Tools = a3Tools;
		}

		@Override
		protected SVImage call() throws Exception {
			updateProgress(-1, 1);

			boolean good = ArmaTools.imageToPAA(a3Tools, toConvert, convertDest, TIMEOUT);
			if (!good) {
				cancel();
				return null;
			}
			updateProgress(1, 1);
			Thread.sleep(500); //show that there was success for a brief moment to not to confuse user

			ExternalResource resource = new PaaImageExternalResource(toConvert, convertDest);
			WorkspaceResourceRegistry.getInstance().getResourceList().add(resource);
			Project.getCurrentProject().getResourceRegistry().getResourceList().add(resource);

			return new SVImage(toConvert, convertDest);
		}

		/** If true, do not show an error message that the task was cancelled */
		public boolean isCancelWithFailMessage() {
			return cancelWithFailMessage;
		}
	}

	private static class Arma3ToolsDirNotSetPopup extends StagePopup<VBox> {

		public Arma3ToolsDirNotSetPopup() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null);
			ResourceBundle bundle = Lang.ApplicationBundle();

			setTitle(bundle.getString("Popups.generic_popup_title"));

			myStage.initModality(Modality.APPLICATION_MODAL);
			myStage.setResizable(false);

			Button btnLocate = new Button(bundle.getString("ValueEditors.ImageValueEditor.set_a3_tools_btn"));
			btnLocate.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					new SelectSaveLocationPopup(ArmaDialogCreator.getApplicationDataManager().getArma3ToolsDirectory()).showAndWait();
					close();
				}
			});

			myRootElement.setPadding(new Insets(10));
			myRootElement.getChildren().add(new Label(bundle.getString("ValueEditors.ImageValueEditor.a3_tools_dir_not_set")));
			myRootElement.getChildren().add(btnLocate);

			myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(true, true, false));
		}

		@Nullable
		public File getA3ToolsDir() {
			return ArmaDialogCreator.getApplicationDataManager().getArma3ToolsDirectory();
		}
	}

	private static class ConversionFailPopup extends StagePopup<VBox> {

		public ConversionFailPopup(@NotNull String message) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null);

			ResourceBundle bundle = Lang.ApplicationBundle();
			setTitle(bundle.getString("ValueEditors.ImageValueEditor.ConvertingPaaPopup.convert_error_popup_title"));

			myStage.initModality(Modality.APPLICATION_MODAL);
			myRootElement.setPadding(new Insets(10));
			myRootElement.getChildren().addAll(new HBox(10, new ImageView("/com/kaylerrenslow/armaDialogCreator/gui/img/icons/error64.png"), new Label(message)));
			myStage.setMinWidth(340d);

			myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(false, true, false));
		}
	}

	private static class ImageAlreadyExistsDialog extends StageDialog<VBox> {

		enum Option {
			CANCEL(""),
			OVERWRITE(Lang.ApplicationBundle().getString("ValueEditors.ImageValueEditor.ImageAlreadyExistsDialog.overwrite")),
			NEW_NAME(Lang.ApplicationBundle().getString("ValueEditors.ImageValueEditor.ImageAlreadyExistsDialog.new_name"));

			private final String displayName;

			Option(String displayName) {
				this.displayName = displayName;
			}
		}

		private final ToggleGroup toggleGroup = new ToggleGroup();
		private Option selectedOption = Option.OVERWRITE;

		/**
		 @param existingImage the .png image file that already exists
		 */
		public ImageAlreadyExistsDialog(File existingImage) {
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

		@NotNull
		public Option getSelectedOption() {
			return selectedOption;
		}
	}
}
