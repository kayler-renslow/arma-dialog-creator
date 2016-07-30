package com.kaylerrenslow.armaDialogCreator.control.sv;

/** A generic wrapper implementation for a String. */
public final class SVString extends SerializableValue {

	public SVString(String s) {
		super(s);
	}

	public String getString() {
		return valuesAsArray[0];
	}

	public void setString(String s) {
		this.valuesAsArray[0] = s;
	}

	@Override
	public SerializableValue deepCopy() {
		return new SVString(valuesAsArray[0]);
	}

	@Override
	public String toString() {
		return valuesAsArray[0];
	}
}
