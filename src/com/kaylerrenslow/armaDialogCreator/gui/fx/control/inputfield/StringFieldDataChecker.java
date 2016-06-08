package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 Checker for Strings
 Created on 05/31/2016. */
public class StringFieldDataChecker implements IInputFieldDataChecker<String> {
	@Override
	public boolean validData(String data) {
		char[] s = data.toCharArray();
		System.out.println("StringFieldDataChecker.validData " + data);
		boolean quote = false;
		//check to see if there are quotes. If there are, only quote followed by another is allowed (""). Placing the quotes next to each other cancel them out
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
