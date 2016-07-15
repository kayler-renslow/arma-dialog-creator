package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
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
				return new InputFieldValueEditor.IntegerEditor();
			case FLOAT:
				return new InputFieldValueEditor.DoubleEditor();
			case BOOLEAN:
				return new BooleanValueEditor();
			case STRING:
				return new InputFieldValueEditor.DoubleEditor();
			case ARRAY:
				return new ArrayValueEditor(2);
			case COLOR:
				return new ColorValueEditor();
			case SOUND:
				return new SoundValueEditor();
			case FONT:
				return new FontValueEditor();
			case FILE_NAME:
				return new InputFieldValueEditor.ArmaStringEditor();
			case IMAGE:
				return new InputFieldValueEditor.ArmaStringEditor();
			case HEX_COLOR_STRING:
				return new HexColorValueEditor();
			case TEXTURE:
				return new InputFieldValueEditor.ArmaStringEditor();
			case EVENT:
				return new InputFieldValueEditor.ArmaStringEditor();
			case SQF:
				return new InputFieldValueEditor.ArmaStringEditor();
		}
		throw new IllegalStateException("Should have made a match");
	}

}
