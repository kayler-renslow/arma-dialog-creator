package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class ColorValueEditor implements ValueEditor<AColor> {
	protected final ColorPicker colorPicker = new ColorPicker();
	private final InputField<ArmaStringFieldDataChecker, String> overrideField = new InputField<>(new ArmaStringFieldDataChecker());
	private StackPane masterPane = new StackPane(colorPicker);

	@Override
	public AColor getValue() {
		return colorPicker.getValue() == null ? null : new AColor(colorPicker.getValue());
	}

	@Override
	public void setValue(AColor val) {
		colorPicker.setValue(val.toJavaFXColor());
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public void setToOverride(boolean override) {
		masterPane.getChildren().clear();
		if (override) {
			masterPane.getChildren().add(overrideField);
		} else {
			masterPane.getChildren().add(colorPicker);
		}
	}

	@Override
	public InputField<ArmaStringFieldDataChecker, String> getOverrideTextField() {
		return overrideField;
	}
}
