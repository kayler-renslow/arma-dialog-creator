package com.kaylerrenslow.armaDialogCreator.control;

/**
 Created by Kayler on 07/13/2016.
 */
public class SVStringArray implements SerializableValue {
	private String[] strings;

	public SVStringArray(String... strings) {
		this.strings = strings;
	}

	public void setStrings(String[] strings) {
		this.strings = strings;
	}

	@Override
	public String[] getAsStringArray() {
		return strings;
	}

	@Override
	public String toString() {
		String ret = "{";
		for (int i = 0; i < strings.length; i++) {
			ret += strings[i] + (i != strings.length - 1 ? ", " : "}");
		}
		return ret;
	}
}
