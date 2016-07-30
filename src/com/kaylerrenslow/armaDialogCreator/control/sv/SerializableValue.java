package com.kaylerrenslow.armaDialogCreator.control.sv;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Base class that specifies that a value can be converted into a String[]. For primitives or other values that don't have multiple attributes, the String[] is length 1 with the attribute at index 0.
 Created on 07/13/2016. */
public abstract class SerializableValue {
	
	protected final String[] valuesAsArray;
	
	/**
	 Equivalent of doing:<code>new SerializableValue(new String[]{onlyValue})</code>
	 */
	public SerializableValue(@NotNull String onlyValue) {
		this(new String[]{onlyValue});
	}
	
	/** Construct the SerializableValue from the given String values. Each value corresponds to a value in {@link #getAsStringArray()} */
	public SerializableValue(@NotNull String[] values) {
		this.valuesAsArray = values;
	}
	
	/** Return the value as a String array */
	public String[] getAsStringArray() {
		return valuesAsArray;
	}
	
	/** Return the instance as a deep copy */
	public abstract SerializableValue deepCopy();
}
