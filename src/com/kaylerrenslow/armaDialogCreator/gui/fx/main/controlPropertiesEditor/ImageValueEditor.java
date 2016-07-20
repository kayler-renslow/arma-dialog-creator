package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVImage;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.SelectSaveLocationPopup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
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

/**
 @author Kayler
 A ValueEditor implementation for selecting image files. This editor also has an implementation for discovering if the image file is a .paa or .tga file and will automatically
 convert it to a read-able format and store it.
 Created on 07/16/2016. */
public class ImageValueEditor implements ValueEditor {
	private final InputField<ArmaStringChecker, String> overrideField = new InputField<>(new ArmaStringChecker());
	
	private final Button btnChooseImage = new Button(Lang.ValueEditors.ImageValueEditor.LOCATE_IMAGE);
	protected final TextField tfFilePath = new TextField("");
	
	private final HBox hBox = new HBox(5, btnChooseImage, tfFilePath);
	
	private final StackPane masterPane = new StackPane(hBox);
	
	protected @Nullable SVImage imageValue = null;
	
	public ImageValueEditor() {
		HBox.setHgrow(tfFilePath, Priority.ALWAYS);
		tfFilePath.setEditable(false);
		btnChooseImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fc = new FileChooser();
				fc.setTitle(Lang.ValueEditors.ImageValueEditor.LOCATE_IMAGE);
				fc.getExtensionFilters().addAll(Lang.ValueEditors.ImageValueEditor.IMAGE_FILE_EXTENSIONS);
				fc.setInitialDirectory(ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory());
				File chosenFile = fc.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
				if (chosenFile == null) {
					return;
				}
				chooseImage(chosenFile);
			}
		});
	}
	
	private void chooseImage(File chosenFile) {
		if (chosenFile.getName().endsWith(".paa")) {
			convertImage(chosenFile);
		} else {
			setValue(new SVImage(chosenFile));
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
		ConvertingPaaPopup convertingPaaPopup = new ConvertingPaaPopup(chosenFile);
		ConvertPaaTask task = new ConvertPaaTask(chosenFile, a3Tools);
		convertingPaaPopup.getProgressBar().progressProperty().bind(task.progressProperty());
		task.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
				newValue.printStackTrace(System.out);
			}
		});
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				setValue(task.getValue());
				convertingPaaPopup.close();
			}
		});
		
		convertingPaaPopup.show();
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
		
	}
	
	@Override
	public @Nullable SerializableValue getValue() {
		return imageValue;
	}
	
	@Override
	public void setValue(SerializableValue val) {
		imageValue = (SVImage) val;
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
	
	@Override
	public void setToOverride(boolean override) {
		masterPane.getChildren().clear();
		if (override) {
			masterPane.getChildren().add(overrideField);
		} else {
			masterPane.getChildren().add(hBox);
		}
	}
	
	@Override
	public InputField<ArmaStringChecker, String> getOverrideTextField() {
		return overrideField;
	}
	
	@Override
	public void focusToEditor() {
		btnChooseImage.requestFocus();
	}
	
	private static class ConvertingPaaPopup extends StagePopup<VBox> {
		
		private final ProgressBar progressBar = new ProgressBar(0);
		
		public ConvertingPaaPopup(File convertingFile) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), Lang.ValueEditors.ImageValueEditor.ConvertingPaaPopup.POPUP_TITLE);
			myStage.initModality(Modality.APPLICATION_MODAL);
			myRootElement.setPadding(new Insets(10));
			myStage.setResizable(false);
			
			myRootElement.getChildren().add(new Label(String.format(Lang.ValueEditors.ImageValueEditor.ConvertingPaaPopup.MESSAGE_F, convertingFile.getName())));
			myRootElement.getChildren().add(progressBar);
		}
		
		public ProgressBar getProgressBar() {
			return progressBar;
		}
		
		@Override
		protected void onCloseRequest(WindowEvent event) {
			event.consume();
		}
		
	}
	
	private static class ConvertPaaTask extends Task<SVImage> {
		
		private final File toConvert;
		private final File a3Tools;
		
		public ConvertPaaTask(@NotNull File toConvert, @NotNull File a3Tools) {
			this.toConvert = toConvert;
			this.a3Tools = a3Tools;
		}
		
		@Override
		protected SVImage call() throws Exception {
			updateProgress(-1, 1);
			Thread.sleep(5000);
			//			ArmaTools.imageToPAA(a3Tools, toConvert, ) //todo
			updateProgress(1, 1);
			Thread.sleep(500); //show that there was success for a brief moment to not to confuse user
			return new SVImage(new File("D:\\DATA\\Steam\\steamapps\\common\\Arma 3 Tools"));
		}
	}
	
	private static class Arma3ToolsDirNotSetPopup extends StagePopup<VBox> {
		
		public Arma3ToolsDirNotSetPopup() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.Popups.GENERIC_POPUP_TITLE);
			myStage.initModality(Modality.APPLICATION_MODAL);
			myStage.setResizable(false);
			
			Button btnLocate = new Button(Lang.ValueEditors.ImageValueEditor.SET_A3_TOOLS_BTN);
			btnLocate.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					new SelectSaveLocationPopup(ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory(), ArmaDialogCreator.getApplicationDataManager().getArma3ToolsDirectory()).showAndWait();
					close();
				}
			});
			
			myRootElement.setPadding(new Insets(10));
			myRootElement.getChildren().add(new Label(Lang.ValueEditors.ImageValueEditor.A3_TOOLS_DIR_NOT_SET));
			myRootElement.getChildren().add(btnLocate);
			
			myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true, false));
		}
		
		@Nullable
		public File getA3ToolsDir() {
			return ArmaDialogCreator.getApplicationDataManager().getArma3ToolsDirectory();
		}
	}
}
