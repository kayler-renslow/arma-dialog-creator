package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVHexColor;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class HexColorValueEditor implements ValueEditor<SVHexColor> {
	protected final ColorPicker colorPicker = new ColorPicker();
	
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private final StackPane masterPane = new StackPane(colorPicker);

	private final ValueObserver<SVHexColor> valueObserver = new ValueObserver<>(null);

	public HexColorValueEditor() {
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color newValue = colorPicker.getValue();
				SVHexColor aColor;
				if (newValue == null) {
					aColor = null;
				} else {
					aColor = new SVHexColor(newValue);
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
		colorPicker.setValue(val.toJavaFXColor());
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
			masterPane.getChildren().add(colorPicker);
		}
	}

	@Override
	public InputField<StringChecker, String> getCustomDataTextField() {
		return overrideField;
	}
	
	@Override
	public void focusToEditor() {
		colorPicker.requestFocus();
	}

	@Override
	public ReadOnlyValueObserver<SVHexColor> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}
}
