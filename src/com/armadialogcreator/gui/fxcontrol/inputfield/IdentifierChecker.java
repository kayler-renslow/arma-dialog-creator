package com.armadialogcreator.gui.fxcontrol.inputfield;

import com.armadialogcreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 A data checker for Identifiers (regex: letter letterOrDigit*). The input also can not be empty

 @author Kayler
 @since 07/08/2016. */
public class IdentifierChecker implements InputFieldDataChecker<String> {
	@Override
	public String errorMsgOnData(@NotNull String data) {
		boolean match = data.length() > 0 && data.matches("[a-zA-Z_][a-zA-Z_0-9]*");
		if (match) {
			return null;
		}
		return Lang.FxControlBundle().getString("InputField.DataCheckers.Identifier.not_identifier");
	}

	@Override
	public String parse(@NotNull String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return Lang.FxControlBundle().getString("InputField.DataCheckers.Identifier.type_name");
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
