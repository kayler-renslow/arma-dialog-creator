package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class InputFieldValueEditor<V extends SerializableValue> implements ValueEditor<V> {
	protected final InputField<? extends InputFieldDataChecker, V> inputField;
	private final StackPane masterPane;
	private final InputField<ArmaStringChecker, String> overrideField = new InputField<>(new ArmaStringChecker());

	public InputFieldValueEditor(@NotNull InputFieldDataChecker dataChecker) {
		this.inputField = new InputField<>(dataChecker);
		this.masterPane = new StackPane(inputField);
	}

	@Override
	public V getValue() {
		return inputField.getValue();
	}

	@Override
	public void setValue(V val) {
		inputField.setValue(val);
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
			masterPane.getChildren().add(inputField);
		}
	}

	@Override
	public InputField<ArmaStringChecker, String> getOverrideTextField() {
		return overrideField;
	}
}
