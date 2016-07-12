package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 A data checker for Identifiers (regex: letter letterOrDigit*). The input also can not be empty
 Created on 07/08/2016.
 */
public class IdentifierFieldDataChecker implements InputFieldDataChecker<String> {
	@Override
	public boolean validData(String data) {
		return data.length() > 0 && data.matches("[a-zA-Z_$][$a-zA-Z_0-9]*");
	}

	@Override
	public String parse(String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return "Identifier";
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
