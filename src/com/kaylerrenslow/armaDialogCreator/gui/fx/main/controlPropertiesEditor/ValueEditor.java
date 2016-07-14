package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.DoubleChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IntegerChecker;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/13/2016.
 */
public interface ValueEditor<V extends SerializableValue> {

	@Nullable V getValue();

	void setValue(@Nullable V val);

	@NotNull Node getRootNode();

	void setToOverride(boolean override);

	InputField<ArmaStringChecker, String> getOverrideTextField();

	static ValueEditor getEditor(ControlProperty.PropertyType propertyType) {
		switch (propertyType) {
			case INT:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new IntegerChecker()));
			case FLOAT:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new DoubleChecker()));
			case BOOLEAN:
				return new BooleanValueEditor();
			case STRING:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new ArmaStringChecker()));
			case ARRAY:
				return new ArrayValueEditor(2);
			case COLOR:
				return new ColorValueEditor();
			case SOUND:
				return new SoundValueEditor();
			case FONT:
				return new FontValueEditor();
			case FILE_NAME:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new ArmaStringChecker()));
			case IMAGE:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new ArmaStringChecker()));
			case HEX_COLOR_STRING:
				return new HexColorValueEditor();
			case TEXTURE:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new ArmaStringChecker()));
			case EVENT:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new ArmaStringChecker()));
			case SQF:
				return new InputFieldValueEditor<>(new DataCheckerWrapper(new ArmaStringChecker()));
		}
		throw new IllegalStateException("Should have made a match");
	}
}
