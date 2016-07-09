package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 A data checker for Identifiers (regex: letter letterOrDigit*). The input also can not be empty
 Created on 07/08/2016.
 */
public class IdentifierFieldDataChecker implements InputFieldDataChecker<String> {
	@Override
	public boolean validData(String data) {
		return data.length() > 0 && !Character.isDigit(data.charAt(0)) && !data.contains(" ");
	}

	@Override
	public String parse(String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return "Identifier";
	}
}
