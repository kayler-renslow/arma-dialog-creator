package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 Checker for Arma Strings
 Created on 05/31/2016. */
public class StringFieldDataChecker implements InputFieldDataChecker<String> {
	@Override
	public boolean validData(String data) {
		if (data == null) {
			return false;
		}
		if (data.length() == 0) {
			return true;
		}
		char[] s = data.toCharArray();
		boolean quote = false;
		//check to see if there are quotes. If there are, only quote followed by another is allowed (""). Placing the quotes next to each other cancel them out. Basically, \" for Java is "" for Arma
		for (int i = 0; i < s.length; i++) {
			if (quote && s[i] != '"') {
				return false;
			}
			if (!quote && s[i] == '"') {
				quote = true;
			} else if (quote && s[i] == '"') {
				quote = false;
			}
		}
		return !quote;
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
