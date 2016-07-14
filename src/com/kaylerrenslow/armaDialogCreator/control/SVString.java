package com.kaylerrenslow.armaDialogCreator.control;

/** A generic wrapper implementation for a String. */
public class SVString implements SerializableValue {
	private final String[] arr;

	public SVString(String s) {
		arr = new String[]{s};
	}

	public String getString() {
		return arr[0];
	}

	public void setString(String s) {
		this.arr[0] = s;
	}

	@Override
	public String[] getAsStringArray() {
		return arr;
	}
}
