package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/13/2016.
 */
public interface ValueEditor<V extends SerializableValue> {

	/**
	 Tells the editor to submit whatever data it has as a value. However, this method does <b>not</b> guarantee the value is actually updated. Any time the value isn't updated, the user should be
	 notified.
	 */
	void submitCurrentData();

	/** Get the value stored inside the editor */
	@Nullable V getValue();

	/** Set the value the editor is editing */
	void setValue(@Nullable V val);

	/** Get the root node used to display the entire editor */
	@NotNull Node getRootNode();

	/** Set to override mode. Override mode is used for allowing the user to enter whatever they want, even if it appears invalid to the normal editor. Use this with caution. */
	void setToCustomData(boolean override);

	/** Get the TextField used for entering the raw text */
	InputField<StringChecker, String> getCustomDataTextField();

	/** Used for {@link Node#requestFocus()} but should target the specific editor (TextField, ColorPicker, etc) */
	void focusToEditor();

	/**
	 Return true if the {@link #getRootNode()}'s width should fill the parent's width.
	 False if the width should be whatever it is initially. By default, will return false.
	 */
	default boolean displayFullWidth() {
		return false;
	}

	/** Get the observer that observes the value. */
	ReadOnlyValueObserver<V> getReadOnlyObserver();

	/**
	 Get a new ValueEditor instance associated with the given property type.

	 @param propertyType type of property to get editor for
	 @param env environment
	 */
	static ValueEditor getEditor(PropertyType propertyType, Env env) {
		switch (propertyType) {
			case INT:
				return new InputFieldValueEditor.IntegerEditor(env);
			case FLOAT:
				return new InputFieldValueEditor.DoubleEditor(env);
			case CONTROL_STYLE:
				return new ControlStyleValueEditor();
			case BOOLEAN:
				return new BooleanValueEditor();
			case STRING:
				return new InputFieldValueEditor.ArmaStringEditor();
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
				return new ImageValueEditor();
			case HEX_COLOR_STRING:
				return new HexColorValueEditor();
			case TEXTURE:
				return new InputFieldValueEditor.ArmaStringEditor();
			case SQF:
				return new InputFieldValueEditor.ArmaStringEditor();
		}
		throw new IllegalStateException("Should have made a match");
	}

}
