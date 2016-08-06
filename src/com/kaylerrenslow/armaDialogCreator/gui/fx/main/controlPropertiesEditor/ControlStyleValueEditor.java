package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.CheckMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
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
 A value editor for {@link com.kaylerrenslow.armaDialogCreator.main.Lang.PropertyType#CONTROL_STYLE}. By default, all {@link ControlStyle}'s are added in the MenuButton.
 Created on 08/05/2016. */
public class ControlStyleValueEditor extends HBox implements ValueEditor<ControlStyleGroup> {
	protected final CheckMenuButton<ControlStyle> menuButton = new CheckMenuButton<>(Lang.ValueEditors.ControlStyleGroupEditor.SELECT_STYLES, null);
	private final TextField textField = new TextField();
	private final InputField<StringChecker, String> tfOverride = new InputField<>(new StringChecker());
	
	public ControlStyleValueEditor() {
		super(5);
		getChildren().add(menuButton);
		getChildren().add(textField);
		menuButton.getItems().addAll(ControlStyle.values());
		menuButton.getSelectedItems().addListener(new ListChangeListener<ControlStyle>() {
			@Override
			public void onChanged(Change<? extends ControlStyle> c) {
				String s = "";
				List<ControlStyle> selected = menuButton.getSelectedItems();
				for(int i = 0; i < selected.size(); i++){
					s += selected.get(i).styleId + (i != selected.size() - 1 ? ControlStyleGroup.DEFAULT_DELIMITER : "");
				}
				textField.setText(s);
			}
		});
		HBox.setHgrow(textField, Priority.ALWAYS);
		textField.setEditable(false);
	}
	
	@Nullable
	@Override
	public ControlStyleGroup getValue() {
		return new ControlStyleGroup(menuButton.getItems().toArray(new ControlStyle[menuButton.getItems().size()]));
	}
	
	@Override
	public void setValue(ControlStyleGroup val) {
		menuButton.setSelected(val.getValues());
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
}
