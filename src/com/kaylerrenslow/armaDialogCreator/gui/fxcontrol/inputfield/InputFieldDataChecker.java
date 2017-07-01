package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 05/31/2016.
 @see InputField
 */
public interface InputFieldDataChecker<V> {
	/** Return null if the text inputted can correctly be parsed into the generic type E. Return a String containing the message as to why the data inputted is incorrect */
	String errorMsgOnData(@NotNull String data);
	
	/** Parses the given data and returns an instance of type E */
	@Nullable V parse(@NotNull String data);
	
	/** Returns a display name that describes the type of data that is allowed (e.g. integer or double) */
	String getTypeName();
	
	/** Return true if the input field can remain empty and be valid, false if it requires at least 1 character to be entered */
	boolean allowEmptyData();
	
	/**
	 Return the value that will be used when the InputField loses focus and there is no valid data. If this value is null, the InputField will then enter the Button State.<br>
	 If the value is not null, the InputField will never enter the Button State, unless {@link InputField#setValue(Object)} is used with null parameter, {@link InputField#clear()} is used,
	 or {@link InputField#setValueFromText(String)} is used with null parameter.<br>
	 The default implementation of this method in the interface returns null.
	 */
	default V getDefaultValue() {
		return null;
	}
	
}
