package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVImage;
import com.kaylerrenslow.armaDialogCreator.data.Workspace;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.ConvertImageTask;
import com.kaylerrenslow.armaDialogCreator.main.ADCStatic;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ResourceBundle;

/**
 A ValueEditor implementation for selecting image files. This editor also has an implementation for discovering if the image file is a .paa or .tga file and will automatically
 convert it to a read-able format and store it.

 @author Kayler
 @since 07/16/2016. */
public class ImageValueEditor implements ValueEditor<SVImage> {
	private final ResourceBundle bundle = Lang.ApplicationBundle();

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
		ConvertImageTask task = new ConvertImageTask(chosenFile, new Callback<KeyValue<File, File>, Void>() {
			@Override
			public Void call(KeyValue<File, File> result) {
				setValue(new SVImage(
								result.getKey(), result.getValue()
						)
				);

				return null;
			}
		});
		task.start();
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

	@Override
	public void focusToEditor() {
		btnChooseImage.requestFocus();
	}

	@NotNull
	@Override
	public ReadOnlyValueObserver<SVImage> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

	@Override
	public boolean displayFullWidth() {
		return true;
	}

}
