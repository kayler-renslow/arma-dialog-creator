package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/16/2016.
 */
public class ImageValueEditor implements ValueEditor {
	private final InputField<ArmaStringChecker, String> overrideField = new InputField<>(new ArmaStringChecker());

	private final Button btnChooseImage = new Button(Lang.ValueEditors.ImageValueEditor.LOCATE_IMAGE);
	private final TextField tfFilePath = new TextField("");

	private final HBox hBox = new HBox(5, btnChooseImage, tfFilePath);

	private final StackPane masterPane = new StackPane(hBox);

	public ImageValueEditor() {
		tfFilePath.setEditable(false);
		btnChooseImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});
	}

	@Override
	public @Nullable SerializableValue getValue() {
		return null;
	}

	@Override
	public void setValue(SerializableValue val) {

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
}
