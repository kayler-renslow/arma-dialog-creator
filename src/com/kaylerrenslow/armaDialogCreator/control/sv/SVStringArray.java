package com.kaylerrenslow.armaDialogCreator.control.sv;

/**
 Created by Kayler on 07/13/2016.
 */
public final class SVStringArray extends SerializableValue {
	
	public SVStringArray(String... strings) {
		super(strings);
	}
	
	/** Set the string at index equal to s */
	public void setString(String s, int index) {
		this.valuesAsArray[index] = s;
	}
	
	public void setStrings(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			valuesAsArray[i] = strings[i];
		}
	}
		
	@Override
	public String toString() {
		String ret = "{";
		for (int i = 0; i < valuesAsArray.length; i++) {
			ret += valuesAsArray[i] + (i != valuesAsArray.length - 1 ? ", " : "}");
		}
		return ret;
	}
}
