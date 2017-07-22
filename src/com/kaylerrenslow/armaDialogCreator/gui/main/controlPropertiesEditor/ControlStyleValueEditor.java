package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.CheckMenuButton;
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

import java.util.ArrayList;
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
						new SVControlStyleGroup(menuButton.getSelectedItemsReadOnly().toArray(new ControlStyle[menuButton.getSelectedItemsReadOnly().size()]));
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
			ControlStyle[] styles = val.getStyleArray();

			ArrayList<ControlStyle> selectMe = new ArrayList<>();

			for (ControlStyle style : styles) {
				if (menuButton.getItems().contains(style)) {
					selectMe.add(style);
				} else {
					//The menuButton may have some styles removed because the user may not actually need all the styles.

					//Since the style isn't in the button, we will attempt to find one that is in the button and has an
					//equal value.

					for (ControlStyle buttonStyle : menuButton.getItems()) {
						if (buttonStyle.styleValue == style.styleValue) {
							selectMe.add(buttonStyle);
							break;
						}
					}
				}
			}
			menuButton.setSelected(selectMe);
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
