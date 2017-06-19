package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVColor;
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
public class ColorValueEditor implements ValueEditor<SVColor> {
	protected final ColorPicker colorPicker = new ColorPicker();
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private StackPane masterPane = new StackPane(colorPicker);
	private final ValueObserver<SVColor> valueObserver = new ValueObserver<>(null);

	public ColorValueEditor() {
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color newValue = colorPicker.getValue();
				SVColor color;
				if (newValue == null) {
					color = null;
				} else {
					color = new SVColor(newValue);

				}
				valueObserver.updateValue(color);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public SVColor getValue() {
		return valueObserver.getValue();
	}
	
	@Override
	public void setValue(SVColor val) {
		if (val == null) {
			colorPicker.setValue(null);
		} else {
			colorPicker.setValue(val.toJavaFXColor());
		}
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
	public boolean displayFullWidth() {
		return false;
	}

	@Override
	public void focusToEditor() {
		colorPicker.requestFocus();
	}

	@Override
	public ReadOnlyValueObserver<SVColor> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}
}
