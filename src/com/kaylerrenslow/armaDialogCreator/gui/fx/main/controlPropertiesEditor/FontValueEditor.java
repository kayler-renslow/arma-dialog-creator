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

import com.kaylerrenslow.armaDialogCreator.control.sv.AFont;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class FontValueEditor implements ValueEditor<AFont> {
	private final Button btnChooseDefault = new Button(Lang.ValueEditors.FontValueEditor.DEFAULT_FONT);
	protected final ComboBox<AFont> comboBox = new ComboBox<>(FXCollections.observableArrayList(AFont.values()));
	private final HBox editorHbox = new HBox(5, comboBox, btnChooseDefault);
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private final StackPane masterPane = new StackPane(editorHbox);

	public FontValueEditor() {
		btnChooseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				comboBox.getSelectionModel().select(AFont.DEFAULT);
			}
		});
	}

	@Override
	public AFont getValue() {
		return comboBox.getValue();
	}

	@Override
	public void setValue(AFont val) {
		comboBox.setValue(val);
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public void setToOverride(boolean override) {
		masterPane.getChildren().clear();
		if(override){
			masterPane.getChildren().add(overrideField);
		}else{
			masterPane.getChildren().add(editorHbox);
		}
	}

	@Override
	public InputField<StringChecker, String> getOverrideTextField() {
		return overrideField;
	}
	
	@Override
	public void focusToEditor() {
		comboBox.requestFocus();
	}
}
