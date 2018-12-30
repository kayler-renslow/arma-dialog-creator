package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.control.sv.SVExpression;
import com.armadialogcreator.control.sv.SVRaw;
import com.armadialogcreator.control.sv.SVString;
import com.armadialogcreator.control.sv.SerializableValue;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.fxcontrol.inputfield.ExpressionChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputField;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputFieldDataChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.RawChecker;
import com.armadialogcreator.util.ReadOnlyValueObserver;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public abstract class InputFieldValueEditor<V extends SerializableValue> implements ValueEditor<V> {
	protected final InputField<? extends InputFieldDataChecker, V> inputField;
	private final StackPane masterPane;

	public InputFieldValueEditor(@NotNull InputFieldDataChecker<V> dataChecker) {
		this.inputField = new InputField<>(dataChecker);
		this.masterPane = new StackPane(inputField);
	}

	@Override
	public void submitCurrentData() {
		inputField.submitValue();
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
	public void focusToEditor() {
		inputField.requestFocus();
	}

	@Override
	public boolean displayFullWidth() {
		return true;
	}

	@NotNull
	@Override
	public ReadOnlyValueObserver<V> getReadOnlyObserver() {
		return inputField.getValueObserver().getReadOnlyValueObserver();
	}

	public static class IntegerEditor extends InputFieldValueEditor<SVExpression> {
		public IntegerEditor(@NotNull Env env) {
			super(new ExpressionChecker(env, ExpressionChecker.TYPE_INT));
		}
	}

	public static class DoubleEditor extends InputFieldValueEditor<SVExpression> {
		public DoubleEditor(@NotNull Env env) {
			super(new ExpressionChecker(env, ExpressionChecker.TYPE_FLOAT));
		}
	}

	public static class ArmaStringEditor extends InputFieldValueEditor<SVString> {
		public ArmaStringEditor() {
			super(new SVArmaStringChecker());
		}
	}

	public static class SQFEditor extends InputFieldValueEditor<SVString> {
		public SQFEditor() {
			super(new SQFDataChecker());
		}
	}

	public static class RawEditor extends InputFieldValueEditor<SVRaw> {
		public RawEditor() {
			super(new RawChecker(null));
		}
	}

}
