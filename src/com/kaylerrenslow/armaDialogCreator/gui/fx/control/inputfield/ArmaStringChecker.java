package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.FXControlLang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Checker for Arma Strings
 Created on 05/31/2016. */
public class ArmaStringChecker implements InputFieldDataChecker<String> {
	@Override
	public String validData(@NotNull String data) {
		if (data.length() == 0) {
			return null;
		}
		char[] s = data.toCharArray();
		boolean quote = false;
		//check to see if there are quotes. If there are, only quote followed by another is allowed (""). Placing the quotes next to each other cancel them out. Basically, \" for Java is "" for Arma
		for (int i = 0; i < s.length; i++) {
			if (quote && s[i] != '"') {
				return FXControlLang.InputField.DataCheckers.ArmaString.MISSING_QUOTE;
			}
			if (!quote && s[i] == '"') {
				quote = true;
			} else if (quote && s[i] == '"') {
				quote = false;
			}
		}
		if(quote){
			return FXControlLang.InputField.DataCheckers.ArmaString.MISSING_QUOTE;
		}
		return null;
	}

	@Override
	public String parse(@NotNull String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return FXControlLang.InputField.DataCheckers.ArmaString.TYPE_NAME;
	}

	@Override
	public boolean allowEmptyData() {
		return true;
	}
}
