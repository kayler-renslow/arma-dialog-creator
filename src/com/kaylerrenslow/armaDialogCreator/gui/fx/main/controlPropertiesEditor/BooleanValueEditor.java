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

import com.kaylerrenslow.armaDialogCreator.control.sv.SVBoolean;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.BooleanChoiceBox;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class BooleanValueEditor implements ValueEditor<SVBoolean> {
	protected final BooleanChoiceBox choiceBox = new BooleanChoiceBox();
	private final StackPane masterPane = new StackPane(choiceBox);
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private final ValueObserver<SVBoolean> svBooleanValueObserver = new ValueObserver<>(null);

	public BooleanValueEditor() {
		choiceBox.getValueObserver().addListener(new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean newValue) {
				svBooleanValueObserver.updateValue(SVBoolean.get(newValue));
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public SVBoolean getValue() {
		return choiceBox.getValue() == null ? null : SVBoolean.get(choiceBox.getValue());
	}

	@Override
	public void setValue(SVBoolean val) {
		choiceBox.setValue(val == null ? null : val.isTrue());
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public void setToCustomData(boolean override) {
		masterPane.getChildren().clear();
		if (override) {
			masterPane.getChildren().add(overrideField);
		} else {
			masterPane.getChildren().add(choiceBox);
		}
	}

	@Override
	public boolean displayFullWidth() {
		return false;
	}

	@Override
	public InputField<StringChecker, String> getCustomDataTextField() {
		return overrideField;
	}
	
	@Override
	public void focusToEditor() {
		choiceBox.requestFocus();
	}

	@Override
	public ReadOnlyValueObserver<SVBoolean> getReadOnlyObserver() {
		return svBooleanValueObserver.getReadOnlyValueObserver();
	}

}
