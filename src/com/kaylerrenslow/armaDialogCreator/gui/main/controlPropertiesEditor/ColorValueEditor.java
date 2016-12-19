package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
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
public class ColorValueEditor implements ValueEditor<AColor> {
	protected final ColorPicker colorPicker = new ColorPicker();
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private StackPane masterPane = new StackPane(colorPicker);
	private final ValueObserver<AColor> valueObserver = new ValueObserver<>(null);

	public ColorValueEditor() {
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color newValue = colorPicker.getValue();
				AColor aColor;
				if (newValue == null) {
					aColor = null;
				} else {
					aColor = new AColor(newValue);

				}
				valueObserver.updateValue(aColor);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public AColor getValue() {
		return valueObserver.getValue();
	}
	
	@Override
	public void setValue(AColor val) {
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
	public ReadOnlyValueObserver<AColor> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}
}
