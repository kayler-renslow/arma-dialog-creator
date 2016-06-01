package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 Checker for Strings
 Created on 05/31/2016. */
public class StringFieldDataChecker implements IInputFieldDataChecker<String> {
	@Override
	public boolean validData(String data) {
		return true;
	}

	@Override
	public String parse(String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return "String";
	}
}
