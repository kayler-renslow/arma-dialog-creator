package com.kaylerrenslow.armaDialogCreator.control.sv;

/**
 Created by Kayler on 07/13/2016.
 */
public final class SVStringArray implements SerializableValue {
	private String[] strings;

	public SVStringArray(String... strings) {
		this.strings = strings;
	}

	/**Set the string at index equal to s*/
	public void setString(String s, int index){
		this.strings[index] = s;
	}

	public void setStrings(String[] strings) {
		this.strings = strings;
	}

	@Override
	public String[] getAsStringArray() {
		return strings;
	}

	@Override
	public SerializableValue deepCopy() {
		String[] arr = new String[strings.length];
		System.arraycopy(strings, 0, arr, 0, arr.length);
		return new SVStringArray(arr);
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
