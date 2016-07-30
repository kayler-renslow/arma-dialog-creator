package com.kaylerrenslow.armaDialogCreator.control.sv;

/** A generic wrapper implementation for an int. */
public final class SVInteger extends SVNumber {
	private int i;

	public SVInteger(int i) {
		super(i);
		this.i = i;
	}

	public void setInt(int i) {
		this.i = i;
		valuesAsArray[0] = this.i + "";
	}

	public int getInt() {
		return i;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVInteger(i);
	}

	@Override
	public String toString() {
		return valuesAsArray[0];
	}
	
	@Override
	protected void setValue(String value) {
		setInt(Integer.parseInt(value));
	}
	
	@Override
	public double getNumber() {
		return i;
	}
}
