package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.AHexColor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
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
public class HexColorValueEditor implements ValueEditor<AHexColor> {
	protected final ColorPicker colorPicker = new ColorPicker();
	
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private final StackPane masterPane = new StackPane(colorPicker);

	private final ValueObserver<AHexColor> valueObserver = new ValueObserver<>(null);

	public HexColorValueEditor() {
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color newValue = colorPicker.getValue();
				AHexColor aColor;
				if (newValue == null) {
					aColor = null;
				} else {
					aColor = new AHexColor(newValue);
				}
				valueObserver.updateValue(aColor);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public AHexColor getValue() {
		return valueObserver.getValue();
	}

	@Override
	public void setValue(AHexColor val) {
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
	public ReadOnlyValueObserver<AHexColor> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}
}
