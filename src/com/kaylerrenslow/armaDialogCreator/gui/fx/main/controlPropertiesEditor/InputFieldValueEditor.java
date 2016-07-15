package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVDouble;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVInteger;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVString;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public abstract class InputFieldValueEditor<V extends SerializableValue> implements ValueEditor<V> {
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

	public static class IntegerEditor extends InputFieldValueEditor<SVInteger>{
		public IntegerEditor() {
			super(new SVIntegerChecker());
		}
	}

	public static class DoubleEditor extends InputFieldValueEditor<SVDouble>{
		public DoubleEditor() {
			super(new SVDoubleChecker());
		}
	}

	public static class ArmaStringEditor extends InputFieldValueEditor<SVString>{
		public ArmaStringEditor() {
			super(new SVArmaStringChecker());
		}
	}
}
