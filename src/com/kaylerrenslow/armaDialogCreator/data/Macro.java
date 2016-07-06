package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Holds a simple macro. The macro is referenced by a key and the result is text that is appended into the ending .h file.
 Created on 07/05/2016. */
public class Macro<T> {

	private final String key;
	protected T value;

	/**
	 A macro is referenced by a key and the result is text that is appended into the ending .h file.

	 @param key the key (prefered to be all caps)
	 @param value the value (Object.toString() will be used to get end result)
	 */
	public Macro(@NotNull String key, @NotNull T value) {
		this.key = key;
		this.value = value;
	}

	/** Get the key */
	@NotNull
	public String getKey() {
		return key;
	}

	/** Get the value */
	@NotNull
	public T getValue() {
		return value;
	}

	/** Set the value */
	public void setValue(@NotNull T value) {
		this.value = value;
	}
}
