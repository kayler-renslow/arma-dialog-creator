package com.kaylerrenslow.armaDialogCreator.control.sv;

/** A generic wrapper implementation for a double. */
public final class SVDouble implements SVNumber {
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

	@Override
	public SerializableValue deepCopy() {
		return new SVDouble(d);
	}

	@Override
	public String toString() {
		return arr[0];
	}

	@Override
	public double getNumber() {
		return d;
	}
}
