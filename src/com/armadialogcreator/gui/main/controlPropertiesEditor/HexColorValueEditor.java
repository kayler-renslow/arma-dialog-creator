package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.sv.SVHexColor;
import com.armadialogcreator.util.ReadOnlyValueObserver;
import com.armadialogcreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class HexColorValueEditor implements ValueEditor<SVHexColor> {
	private final ColorPicker colorPicker = new ColorPicker();
	private final TextField tfHexColor = new TextField();

	private final StackPane masterPane = new StackPane(new HBox(5, colorPicker, tfHexColor));

	private final ValueObserver<SVHexColor> valueObserver = new ValueObserver<>(null);

	public HexColorValueEditor() {
		colorPicker.setValue(null);
		tfHexColor.setEditable(false);
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color newValue = colorPicker.getValue();
				SVHexColor aColor;
				if (newValue == null) {
					aColor = null;
					tfHexColor.setText("");
				} else {
					aColor = new SVHexColor(newValue);
					tfHexColor.setText(aColor.getHexColor());
				}
				valueObserver.updateValue(aColor);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public SVHexColor getValue() {
		return valueObserver.getValue();
	}

	@Override
	public void setValue(SVHexColor val) {
		if (val == null) {
			colorPicker.setValue(null);
			tfHexColor.setText("");
		} else {
			colorPicker.setValue(val.toJavaFXColor());
			tfHexColor.setText(val.getHexColor());
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public void focusToEditor() {
		colorPicker.requestFocus();
	}

	@NotNull
	@Override
	public ReadOnlyValueObserver<SVHexColor> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

	@Override
	public boolean displayFullWidth() {
		return false;
	}
}
