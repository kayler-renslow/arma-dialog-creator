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

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 A data checker for Identifiers (regex: letter letterOrDigit*). The input also can not be empty
 Created on 07/08/2016.
 */
public class IdentifierChecker implements InputFieldDataChecker<String> {
	@Override
	public String validData(@NotNull String data) {
		boolean match = data.length() > 0 && data.matches("[a-zA-Z_$][$a-zA-Z_0-9]*");
		if(match){
			return null;
		}
		return Lang.FxControlBundle.getString("InputField.DataCheckers.Identifier.not_identifier");
	}

	@Override
	public String parse(@NotNull String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return Lang.FxControlBundle.getString("InputField.DataCheckers.Identifier.type_name");
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
