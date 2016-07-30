package com.kaylerrenslow.armaDialogCreator.control.sv;

/** A generic wrapper implementation for a boolean. */
public final class SVBoolean extends SerializableValue {
	private static final String S_TRUE = "true";
	private static final String S_FALSE = "false";
	
	private boolean b;
	
	public static SVBoolean TRUE = new SVBoolean(true);
	public static SVBoolean FALSE = new SVBoolean(false);
	
	private SVBoolean(boolean b) {
		super(b + "");
		this.b = b;
	}
		
	public boolean isTrue() {
		return b;
	}
	
	@Override
	public SerializableValue deepCopy() {
		return get(b);
	}
	
	@Override
	public String toString() {
		return b ? S_TRUE : S_FALSE;
	}
	
	/** Return {@link #TRUE} if value==true. Return {@link #FALSE} if value==false */
	public static SerializableValue get(boolean value) {
		return value ? TRUE : FALSE;
	}
}
