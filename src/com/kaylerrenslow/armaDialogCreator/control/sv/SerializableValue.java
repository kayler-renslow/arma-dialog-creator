package com.kaylerrenslow.armaDialogCreator.control.sv;

/**
 @author Kayler
 Interface that specifies that a value can be converted into a String[]. For primitives or other values that don't have multiple attributes, the String[] is length 1 with the attribute at index 0.
 Created on 07/13/2016. */
public interface SerializableValue {

	/**
	 Return the value as a String array
	 */
	String[] getAsStringArray();

	/** Return the instance as a deep copy */
	SerializableValue deepCopy();
}
