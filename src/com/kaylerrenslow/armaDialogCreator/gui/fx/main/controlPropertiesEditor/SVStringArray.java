package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.SerializableValue;

/**
 Created by Kayler on 07/13/2016.
 */
public class SVStringArray implements SerializableValue{
	private String[] strings;

	public SVStringArray(String ... strings) {
		this.strings = strings;
	}

	public void setStrings(String[] strings) {
		this.strings = strings;
	}

	@Override
	public String[] getAsStringArray() {
		return strings;
	}
}
