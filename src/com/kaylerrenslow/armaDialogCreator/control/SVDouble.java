package com.kaylerrenslow.armaDialogCreator.control;

/** A generic wrapper implementation for a double. */
public class SVDouble implements SerializableValue {
	private final String[] arr;
	private double d;

	public SVDouble(double d) {
		this.d = d;
		arr = new String[]{d + ""};
	}

	public void setDouble(double d) {
		this.d = d;
		arr[0] = d + "";
	}

	public double getDouble() {
		return d;
	}

	@Override
	public String[] getAsStringArray() {
		return arr;
	}
}
