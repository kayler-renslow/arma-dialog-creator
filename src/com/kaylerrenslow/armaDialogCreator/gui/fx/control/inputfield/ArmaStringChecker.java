/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.lang.FXControlLang;
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
