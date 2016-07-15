package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.FXControlLang;
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
		return FXControlLang.InputField.DataCheckers.Identifier.NOT_IDENTIFIER;
	}

	@Override
	public String parse(@NotNull String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return FXControlLang.InputField.DataCheckers.Identifier.TYPE_NAME;
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
