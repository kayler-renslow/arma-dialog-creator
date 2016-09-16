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

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.CheckMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import com.kaylerrenslow.armaDialogCreator.main.lang.LookupLang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 A value editor for {@link LookupLang.PropertyType#CONTROL_STYLE}. By default, all {@link ControlStyle}'s are added in the MenuButton.
 Created on 08/05/2016. */
public class ControlStyleValueEditor extends HBox implements ValueEditor<ControlStyleGroup> {
	protected final CheckMenuButton<ControlStyle> menuButton = new CheckMenuButton<>(Lang.ValueEditors.ControlStyleGroupEditor.SELECT_STYLES, null);
	private final TextField textField = new TextField();
	private final InputField<StringChecker, String> tfOverride = new InputField<>(new StringChecker());
	protected final ValueObserver<ControlStyleGroup> valueObserver = new ValueObserver<>(null);

	public ControlStyleValueEditor() {
		super(5);
		getChildren().add(menuButton);
		getChildren().add(textField);
		menuButton.getItems().addAll(ControlStyle.values());
		for (ControlStyle style : menuButton.getItems()) {
			menuButton.bindTooltip(style, style.documentation);
		}
		menuButton.getSelectedItems().addListener(new ListChangeListener<ControlStyle>() {
			@Override
			public void onChanged(Change<? extends ControlStyle> c) {
				String s = "";
				List<ControlStyle> selected = menuButton.getSelectedItems();
				for (int i = 0; i < selected.size(); i++) {
					s += selected.get(i).styleId + (i != selected.size() - 1 ? ControlStyleGroup.DEFAULT_DELIMITER : "");
				}
				textField.setText(s);
				ControlStyleGroup group = menuButton.getSelectedItems().size() == 0 ? null :
						new ControlStyleGroup(menuButton.getSelectedItems().toArray(new ControlStyle[menuButton.getSelectedItems().size()]));
				valueObserver.updateValue(group);
			}
		});
		HBox.setHgrow(textField, Priority.ALWAYS);
		textField.setEditable(false);
	}

	@Override
	public void submitCurrentData() {

	}

	@Nullable
	@Override
	public ControlStyleGroup getValue() {
		return valueObserver.getValue();
	}

	@Override
	public void setValue(ControlStyleGroup val) {
		if (val == null) {
			menuButton.clearSelection();
		} else {
			menuButton.setSelected(val.getValues());
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return this;
	}

	@Override
	public void setToOverride(boolean override) {
		getChildren().clear();
		if (override) {
			getChildren().add(this.tfOverride);
		} else {
			getChildren().add(menuButton);
			getChildren().add(textField);
		}
	}

	@Override
	public InputField<StringChecker, String> getOverrideTextField() {
		return tfOverride;
	}

	@Override
	public void focusToEditor() {
		menuButton.requestFocus();
	}

	@Override
	public void addValueListener(@NotNull ValueListener<ControlStyleGroup> listener) {
		valueObserver.addValueListener(listener);
	}

	@Override
	public boolean removeValueListener(@NotNull ValueListener<ControlStyleGroup> listener) {
		return valueObserver.removeListener(listener);
	}
}
