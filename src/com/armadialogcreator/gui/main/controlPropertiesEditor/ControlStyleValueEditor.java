package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.sv.SVControlStyleGroup;
import com.armadialogcreator.gui.fxcontrol.CheckMenuButton;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.ReadOnlyValueObserver;
import com.armadialogcreator.util.ValueObserver;
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
public class ControlStyleValueEditor extends HBox implements ValueEditor<SVControlStyleGroup> {
	protected final CheckMenuButton<ControlStyle> menuButton = new CheckMenuButton<>(Lang.ApplicationBundle().getString("ValueEditors.ControlStyleGroupEditor.select_styles"), null);
	private final TextField textField = new TextField();
	protected final ValueObserver<SVControlStyleGroup> valueObserver = new ValueObserver<>(null);

	public ControlStyleValueEditor() {
		super(5);
		getChildren().add(menuButton);
		getChildren().add(textField);
		menuButton.getItems().addAll(ControlStyle.values());
		menuButton.setTooltip(new Tooltip(Lang.ApplicationBundle().getString("ValueEditors.ControlStyleGroupEditor.tooltip_select_styles")));
		for (ControlStyle style : menuButton.getItems()) {
			menuButton.bindTooltip(style, style.documentation);
		}
		menuButton.getSelectedItemsReadOnly().addListener(new ListChangeListener<ControlStyle>() {
			@Override
			public void onChanged(Change<? extends ControlStyle> c) {
				List<ControlStyle> selected = menuButton.getSelectedItemsReadOnly();
				if (selected.size() == 0) {
					valueObserver.updateValue(null);
					textField.setText("");
					return;
				}
				SVControlStyleGroup group = menuButton.getSelectedItemsReadOnly().size() == 0 ? null :
						new SVControlStyleGroup(menuButton.getSelectedItemsReadOnly());
				textField.setText(group == null ? "" : group.toString());
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
	public SVControlStyleGroup getValue() {
		return valueObserver.getValue();
	}

	@Override
	public void setValue(SVControlStyleGroup val) {
		if (val == null) {
			menuButton.clearSelection();
			textField.setText("");
		} else {
			menuButton.setSelected(val.getStyleArray());
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return this;
	}

	@Override
	public void focusToEditor() {
		menuButton.requestFocus();
	}

	@Override
	public boolean displayFullWidth() {
		return true;
	}

	@NotNull
	@Override
	public ReadOnlyValueObserver<SVControlStyleGroup> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

}
