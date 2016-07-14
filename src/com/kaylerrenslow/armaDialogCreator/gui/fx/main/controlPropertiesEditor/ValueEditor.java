package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.DoubleFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IntegerFieldDataChecker;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/13/2016.
 */
public interface ValueEditor<V> {

	@Nullable V getValue();

	void setValue(@Nullable V val);

	@NotNull Node getRootNode();

	void setToOverride(boolean override);

	InputField<ArmaStringFieldDataChecker, String> getOverrideTextField();

	static ValueEditor getEditor(ControlProperty.PropertyType propertyType) {
		switch (propertyType) {
			case INT:
				return new InputFieldValueEditor<>(new IntegerFieldDataChecker());
			case FLOAT:
				return new InputFieldValueEditor<>(new DoubleFieldDataChecker());
			case BOOLEAN:
				return new BooleanValueEditor();
			case STRING:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case ARRAY:
				return new ArrayValueEditor(2);
			case COLOR:
				return new ColorValueEditor();
			case SOUND:
				return new SoundValueEditor();
			case FONT:
				return new FontValueEditor();
			case FILE_NAME:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case IMAGE:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case HEX_COLOR_STRING:
				return new HexColorValueEditor();
			case TEXTURE:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case EVENT:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case SQF:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
		}
		throw new IllegalStateException("Should have made a match");
	}
}
