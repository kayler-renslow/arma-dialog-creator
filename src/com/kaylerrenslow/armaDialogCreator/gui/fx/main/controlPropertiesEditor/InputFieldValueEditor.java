/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ExpressionChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public abstract class InputFieldValueEditor<V extends SerializableValue> implements ValueEditor<V> {
	protected final InputField<? extends InputFieldDataChecker, V> inputField;
	private final StackPane masterPane;
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());

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
	public void setToOverride(boolean override) {
		masterPane.getChildren().clear();
		if (override) {
			masterPane.getChildren().add(overrideField);
		} else {
			masterPane.getChildren().add(inputField);
		}
	}

	@Override
	public InputField<StringChecker, String> getOverrideTextField() {
		return overrideField;
	}
	
	@Override
	public void focusToEditor() {
		inputField.requestFocus();
	}
		
	@Override
	public boolean displayFullWidth() {
		return true;
	}

	@Override
	public ReadOnlyValueObserver<V> getReadOnlyObserver() {
		return inputField.getValueObserver().getReadOnlyValueObserver();
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

	public static class ExpressionEditor extends InputFieldValueEditor<Expression> {
		public ExpressionEditor(Env env) {
			super(new ExpressionChecker(env));
		}
	}
}
