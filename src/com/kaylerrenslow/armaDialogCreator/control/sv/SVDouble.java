package com.kaylerrenslow.armaDialogCreator.control.sv;

/** A generic wrapper implementation for a double. */
public final class SVDouble extends SVNumber {
	private double d;
	
	public SVDouble(double d) {
		super(d);
		this.d = d;
	}
	
	public void setDouble(double d) {
		this.d = d;
		valuesAsArray[0] = d + "";
	}
	
	public double getDouble() {
		return d;
	}
	
	@Override
	public SerializableValue deepCopy() {
		return new SVDouble(d);
	}
	
	@Override
	public String toString() {
		return valuesAsArray[0];
	}
	
	@Override
	protected void setValue(String value) {
		setDouble(Double.parseDouble(value));
	}
	
	@Override
	public double getNumber() {
		return d;
	}
}
