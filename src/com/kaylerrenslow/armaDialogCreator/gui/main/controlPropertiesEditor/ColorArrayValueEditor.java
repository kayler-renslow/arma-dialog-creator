package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVColorArray;
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
public class ColorArrayValueEditor implements ValueEditor<SVColorArray> {
	protected final ColorPicker colorPicker = new ColorPicker();
	private StackPane masterPane = new StackPane(colorPicker);
	private final ValueObserver<SVColorArray> valueObserver = new ValueObserver<>(null);

	public ColorArrayValueEditor() {
		colorPicker.setValue(null);
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color newValue = colorPicker.getValue();
				SVColorArray color;
				if (newValue == null) {
					color = null;
				} else {
					color = new SVColorArray(newValue);

				}
				valueObserver.updateValue(color);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public SVColorArray getValue() {
		return valueObserver.getValue();
	}
	
	@Override
	public void setValue(SVColorArray val) {
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
	public boolean displayFullWidth() {
		return false;
	}

	@Override
	public void focusToEditor() {
		colorPicker.requestFocus();
	}

	@Override
	public ReadOnlyValueObserver<SVColorArray> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}
}
