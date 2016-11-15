/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 05/31/2016.
 @see InputField
 */
public interface InputFieldDataChecker<V> {
	/** Return null if the text inputted can correctly be parsed into the generic type E. Return a String containing the message as to why the data inputted is incorrect */
	String validData(@NotNull String data);
	
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
