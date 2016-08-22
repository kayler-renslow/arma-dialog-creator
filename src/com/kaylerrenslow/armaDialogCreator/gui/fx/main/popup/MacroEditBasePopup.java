/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ValueEditor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Base class for a popup that manipulates/creates macro data.
 Created on 07/15/2016. */
public abstract class MacroEditBasePopup extends StagePopup<VBox> {
	private final Env env;
	private ValueEditor editor;

	private StackPane stackPaneEditor = new StackPane();

	private final TextField tfMacroDescription = new TextField();
	private final InputField<IdentifierChecker, String> inMacroKey = new InputField<>(new IdentifierChecker());
	private final ChoiceBox<PropertyType> cbMacroType = new ChoiceBox<>();

	private final Label lblNoTypeChosen = new Label(Lang.Popups.MacroEdit.NO_TYPE_CHOSEN);

	/**
	 Creates a Macro editor.

	 @param env instance used for evaluating {@link com.kaylerrenslow.armaDialogCreator.control.sv.Expression} based Macros' values. The env is only used for checking that an expression evaluates properly.
	 */
	public MacroEditBasePopup(Env env) {
		super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Popups.MacroEdit.POPUP_TITLE);
		this.env = env;
		myRootElement.setPadding(new Insets(10));
		stackPaneEditor.minWidth(0d);
		stackPaneEditor.setAlignment(Pos.CENTER_LEFT);

		stackPaneEditor.getChildren().add(lblNoTypeChosen);

		EventHandler<? super KeyEvent> oldEvent = inMacroKey.getOnKeyReleased();
		inMacroKey.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				int cursorPosition = inMacroKey.getCaretPosition();
				IndexRange selection = inMacroKey.getSelection();

				inMacroKey.setText(inMacroKey.getText().toUpperCase().replaceAll("\\s", "_"));

				inMacroKey.positionCaret(cursorPosition);
				inMacroKey.selectRange(selection.getStart(), selection.getEnd());

				if (oldEvent != null) {
					oldEvent.handle(event);
				}
			}
		});
		inMacroKey.setOnKeyTyped(inMacroKey.getOnKeyReleased());

		cbMacroType.getItems().addAll(PropertyType.values());

		cbMacroType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PropertyType>() {
			@Override
			public void changed(ObservableValue<? extends PropertyType> observable, PropertyType oldValue, PropertyType selected) {
				editor = ValueEditor.getEditor(selected, env);
				stackPaneEditor.getChildren().clear();
				stackPaneEditor.getChildren().add(editor.getRootNode());
				if (editor.displayFullWidth()) {
					HBox.setHgrow(stackPaneEditor, Priority.ALWAYS);
				} else {
					HBox.setHgrow(stackPaneEditor, Priority.SOMETIMES);
				}
			}
		});

		VBox vbTop = new VBox(5);
		vbTop.setFillWidth(true);
		HBox hboxValueEditor = new HBox(new Label(Lang.Popups.MacroEdit.MACRO_VALUE), stackPaneEditor);

		vbTop.getChildren().addAll(hbox(Lang.Popups.MacroEdit.MACRO_KEY, inMacroKey), hbox(Lang.Popups.MacroEdit.MACRO_TYPE, cbMacroType), hboxValueEditor, hbox(Lang.Popups.MacroEdit.MACRO_COMMENT,
				tfMacroDescription));
		myRootElement.getChildren().add(vbTop);
		VBox.setVgrow(vbTop, Priority.ALWAYS);

		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true, false));

		myStage.setMinWidth(480d);
		myStage.setWidth(500d);
		myStage.setHeight(240);
	}

	private HBox hbox(String text, Node graphic) {
		Label lbl = new Label(text);
		HBox.setHgrow(graphic, Priority.ALWAYS);
		return new HBox(5, lbl, graphic);
	}

	/** Return true if all fields have their input set (macro key, editor has valid value, type is set). If at least one input isn't set, will return false and the input will request focus. */
	protected boolean checkFields() {
		inMacroKey.submitValue();
		editor.submitCurrentData();
		if (inMacroKey.getValue() == null) {
			inMacroKey.requestFocus();
			beep();
			return false;
		}
		if (editor == null) {
			cbMacroType.requestFocus();
			beep();
			return false;
		}
		if (editor.getValue() == null) {
			editor.focusToEditor();
			beep();
			return false;
		}
		return true;
	}

	/** Set the editor to the given macro */
	protected void setToMacro(@Nullable Macro m) {
		if (m == null) {
			inMacroKey.clear();
			cbMacroType.setDisable(false);
			editor = null;
			stackPaneEditor.getChildren().clear();
			stackPaneEditor.getChildren().add(lblNoTypeChosen);
			cbMacroType.setValue(null);
			return;
		}
		inMacroKey.setValue(m.getKey());
		tfMacroDescription.setText(m.getComment());
		cbMacroType.setValue(m.getPropertyType());
		cbMacroType.setDisable(true);
		editor.setValue(m.getValue());
	}

	/** Return a new Macro instance with the current settings. */
	@Nullable
	protected Macro<? extends SerializableValue> getMacro() {
		if (!checkFields()) {
			return null;
		}
		Macro<? extends SerializableValue> m = new Macro<>(inMacroKey.getValue(), editor.getValue(), cbMacroType.getValue());
		m.setComment(tfMacroDescription.getText());
		return m;
	}

}
