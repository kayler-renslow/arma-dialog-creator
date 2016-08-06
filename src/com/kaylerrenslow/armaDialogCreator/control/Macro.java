/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Holds a simple macro. The macro is referenced by a key and the result is text that is appended into the ending .h file.
 Created on 07/05/2016. */
public class Macro<T extends SerializableValue> {

	private final String key;
	private final PropertyType propertyType;
	protected ValueObserver<T> valueObserver;
	protected String comment;

	/**
	 A macro is referenced by a key and the result is text that is appended into the ending .h file.

	 @param key the key (prefered to be all caps)
	 @param value the value (Object.toString() will be used to get end result)
	 */
	public Macro(@NotNull String key, @NotNull T value, @NotNull PropertyType propertyType) {
		this.key = key;
		this.valueObserver = new ValueObserver<>(value);
		this.propertyType = propertyType;
	}

	/** Get the key */
	@NotNull
	public String getKey() {
		return key;
	}

	/** Get the value */
	@NotNull
	public T getValue() {
		return valueObserver.getValue();
	}

	/** Set the value */
	public void setValue(@NotNull T value) {
		this.valueObserver.updateValue(value);
	}

	@NotNull
	public ValueObserver<T> getValueObserver() {
		return valueObserver;
	}

	@Nullable
	public String getComment() {
		return comment;
	}

	public void setComment(@Nullable String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return key;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}
}
