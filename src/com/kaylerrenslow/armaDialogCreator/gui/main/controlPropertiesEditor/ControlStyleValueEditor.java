package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.CheckMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 A value editor for {@link ControlStyle}. By default, all {@link ControlStyle}'s are added in the MenuButton.

 @author Kayler
 @since 08/05/2016. */
public class ControlStyleValueEditor extends HBox implements ValueEditor<ControlStyleGroup> {
	protected final CheckMenuButton<ControlStyle> menuButton = new CheckMenuButton<>(Lang.ApplicationBundle().getString("ValueEditors.ControlStyleGroupEditor.select_styles"), null);
	private final TextField textField = new TextField();
	private final InputField<StringChecker, String> tfOverride = new InputField<>(new StringChecker());
	protected final ValueObserver<ControlStyleGroup> valueObserver = new ValueObserver<>(null);

	public ControlStyleValueEditor() {
		super(5);
		getChildren().add(menuButton);
		getChildren().add(textField);
		menuButton.getItems().addAll(ControlStyle.values());
		menuButton.setTooltip(new Tooltip(Lang.ApplicationBundle().getString("ValueEditors.ControlStyleGroupEditor.tooltip_select_styles")));
		for (ControlStyle style : menuButton.getItems()) {
			menuButton.bindTooltip(style, style.documentation);
		}
		menuButton.getSelectedItems().addListener(new ListChangeListener<ControlStyle>() {
			@Override
			public void onChanged(Change<? extends ControlStyle> c) {
				String s = "";
				List<ControlStyle> selected = menuButton.getSelectedItems();
				for (int i = 0; i < selected.size(); i++) {
					s += selected.get(i).styleValue + (i != selected.size() - 1 ? ControlStyleGroup.DEFAULT_DELIMITER : "");
				}
				textField.setText(s);
				ControlStyleGroup group = menuButton.getSelectedItems().size() == 0 ? null :
						new ControlStyleGroup(menuButton.getSelectedItems().toArray(new ControlStyle[menuButton.getSelectedItems().size()]));
				valueObserver.updateValue(group);
			}
		});
		HBox.setHgrow(textField, Priority.ALWAYS);
		textField.setEditable(false);
		setAlignment(Pos.CENTER_LEFT);
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
	public void setToCustomData(boolean override) {
		getChildren().clear();
		if (override) {
			getChildren().add(this.tfOverride);
		} else {
			getChildren().add(menuButton);
			getChildren().add(textField);
		}
	}

	@Override
	public InputField<StringChecker, String> getCustomDataTextField() {
		return tfOverride;
	}

	@Override
	public void focusToEditor() {
		menuButton.requestFocus();
	}

	@Override
	public boolean displayFullWidth() {
		return true;
	}

	@Override
	public ReadOnlyValueObserver<ControlStyleGroup> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

}
