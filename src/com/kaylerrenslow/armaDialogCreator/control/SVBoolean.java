package com.kaylerrenslow.armaDialogCreator.control;

/** A generic wrapper implementation for a boolean. */
public class SVBoolean implements SerializableValue {
	private static final String S_TRUE = "true";
	private static final String S_FALSE = "false";

	private final String[] arr;
	private boolean b;

	public static SVBoolean TRUE = new SVBoolean(true);
	public static SVBoolean FALSE = new SVBoolean(false);

	private SVBoolean(boolean b) {
		this.b = b;
		arr = new String[]{b + ""};
	}

	public boolean isTrue() {
		return b;
	}

	@Override
	public String[] getAsStringArray() {
		return arr;
	}

	@Override
	public String toString() {
		return b ? S_TRUE : S_FALSE;
	}
}
