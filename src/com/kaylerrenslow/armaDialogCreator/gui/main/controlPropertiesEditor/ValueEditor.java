package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
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
	@NotNull ReadOnlyValueObserver<V> getReadOnlyObserver();

	/**
	 Get a new ValueEditor instance associated with the given property type.

	 @param propertyType type of property to get editor for
	 @param env environment
	 */
	@NotNull
	static ValueEditor getEditor(@NotNull PropertyType propertyType, @NotNull Env env) {
		switch (propertyType) {
			case Int:
				return new InputFieldValueEditor.IntegerEditor(env);
			case Float:
				return new InputFieldValueEditor.DoubleEditor(env);
			case ControlStyle:
				return new ControlStyleValueEditor();
			case Boolean:
				return new BooleanValueEditor();
			case String:
				return new InputFieldValueEditor.ArmaStringEditor();
			case Array:
				return new ArrayValueEditor(2);
			case Color:
				return new ColorArrayValueEditor();
			case Sound:
				return new SoundValueEditor();
			case Font:
				return new FontValueEditor();
			case FileName:
				return new FileNameValueEditor();
			case Image:
				return new ImageValueEditor();
			case HexColorString:
				return new HexColorValueEditor();
			case Texture:
				return new InputFieldValueEditor.ArmaStringEditor();
			case SQF:
				return new InputFieldValueEditor.SQFEditor();
			case Raw:
				return new InputFieldValueEditor.RawEditor();
		}
		throw new IllegalStateException("Should have made a match");
	}

}
