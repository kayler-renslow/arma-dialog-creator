/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control.sv;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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
	
	/**Returns {@link Arrays#toString(Object[])} with {@link #getAsStringArray()} as the parameter*/
	public String toStringDebug(){
		return Arrays.toString(valuesAsArray);
	}
	
	/** Return the instance as a deep copy */
	public abstract SerializableValue deepCopy();
	
	
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof SerializableValue){
			SerializableValue other = (SerializableValue) o;
			return Arrays.equals(this.valuesAsArray, other.valuesAsArray);
		}
		return false;
	}
}
