package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class BooleanValueEditor implements ValueEditor<Boolean> {
	protected final ChoiceBox<Boolean> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(true, false));
	private final StackPane masterPane = new StackPane(choiceBox);
	private final InputField<ArmaStringFieldDataChecker, String> overrideField = new InputField<>(new ArmaStringFieldDataChecker());

	@Override
	public Boolean getValue() {
		return choiceBox.getValue();
	}

	@Override
	public void setValue(Boolean val) {
		choiceBox.setValue(val);
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
			masterPane.getChildren().add(choiceBox);
		}
	}

	@Override
	public InputField<ArmaStringFieldDataChecker, String> getOverrideTextField() {
		return overrideField;
	}
}
