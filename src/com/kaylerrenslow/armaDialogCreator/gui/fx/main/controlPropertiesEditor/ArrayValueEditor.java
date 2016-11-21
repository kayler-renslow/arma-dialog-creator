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

import com.kaylerrenslow.armaDialogCreator.control.sv.SVStringArray;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 07/13/2016.
 */
public class ArrayValueEditor implements ValueEditor<SVStringArray> {

	private final ValueListener editorValueUpdateListener = new ValueListener() {
		@Override
		public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
			valueObserver.updateValue(createValue());
		}
	};
	protected ArrayList<InputField<ArmaStringChecker, String>> editors = new ArrayList<>();

	protected final Button btnDecreaseSize = new Button("-");
	protected final Button btnIncreaseSize = new Button("+");
	private final double gap = 5;
	private final double tfPrefWidth = 100d;
	protected final FlowPane editorsPane = new FlowPane(gap, gap);
	private final HBox masterPane;
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());

	private final ValueObserver<SVStringArray> valueObserver = new ValueObserver<>(null);

	public ArrayValueEditor(int numInitialFields) {
		masterPane = new HBox(5, editorsPane);
		editorsPane.minWidth(0d);
		editorsPane.prefWidth(0d);
		editorsPane.setPrefWrapLength(tfPrefWidth * 3 + gap * numInitialFields); //have room for 3 text fields
		masterPane.minWidth(0d);

		masterPane.getChildren().addAll(btnDecreaseSize, btnIncreaseSize);
		btnDecreaseSize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (editors.size() > 1) {
					InputField removed = editors.remove(editors.size() - 1);
					editorsPane.getChildren().remove(removed);
				}
			}
		});
		btnIncreaseSize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				InputField in = getTextField();
				editors.add(in);
				editorsPane.getChildren().add(in);
				editorsPane.autosize();

				addEditorValueUpdateListener();
			}
		});
		InputField in;
		for (int i = 0; i < numInitialFields; i++) {
			in = getTextField();
			editors.add(in);
			editorsPane.getChildren().add(in);
		}
		editorsPane.autosize();

		addEditorValueUpdateListener();
	}

	private void addEditorValueUpdateListener() {
		for (InputField inf : editors) {
			inf.getValueObserver().addListener(editorValueUpdateListener);
		}
	}

	@Override
	public void focusToEditor() {
		for (InputField tf : editors) {
			if (tf.getValue() == null) {
				tf.requestFocus();
				return;
			}
		}
		editors.get(0).requestFocus();
	}

	@Override
	public ReadOnlyValueObserver<SVStringArray> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

	private InputField<ArmaStringChecker, String> getTextField() {
		InputField<ArmaStringChecker, String> tf = new InputField<>(new ArmaStringChecker());
		tf.setPrefWidth(tfPrefWidth);
		return tf;
	}

	@Override
	public void submitCurrentData() {
		for (InputField tf : editors) {
			tf.submitValue();
		}
	}

	@Override
	public SVStringArray getValue() {
		return valueObserver.getValue();
	}

	@Nullable
	private SVStringArray createValue() {
		String[] values = new String[editors.size()];
		int i = 0;
		for (InputField tf : editors) {
			if (tf.getValue() == null) {
				return null;
			}
			values[i++] = tf.getText();
		}
		return new SVStringArray(values);
	}

	@Override
	public void setValue(SVStringArray val) {
		if (val == null) {
			for (InputField<ArmaStringChecker, String> editor : editors) {
				editor.setValue(null);
			}
		} else {
			int i = 0;
			for (String s : val.getAsStringArray()) {
				editors.get(i++).setValue(s);
			}
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public boolean displayFullWidth() {
		return false;
	}

	@Override
	public void setToCustomData(boolean override) {
		masterPane.getChildren().clear();
		if (override) {
			masterPane.getChildren().add(overrideField);
		} else {
			masterPane.getChildren().add(editorsPane);
		}
	}

	@Override
	public InputField<StringChecker, String> getCustomDataTextField() {
		return overrideField;
	}
}
