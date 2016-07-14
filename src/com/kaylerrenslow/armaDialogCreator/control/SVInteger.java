package com.kaylerrenslow.armaDialogCreator.control;

/** A generic wrapper implementation for an int. */
public class SVInteger implements SerializableValue {
	private final String[] arr;
	private int i;

	public SVInteger(int i) {
		this.i = i;
		arr = new String[]{i + ""};
	}

	public void setInt(int i) {
		this.i = i;
		arr[0] = this.i + "";
	}

	public int getInt() {
		return i;
	}

	@Override
	public String[] getAsStringArray() {
		return arr;
	}

	@Override
	public String toString() {
		return arr[0];
	}
}
