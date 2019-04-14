package com.armadialogcreator.gui.fxcontrol.inputfield;

import com.armadialogcreator.lang.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 A data checker for macro identifiers (regex: [a-zA-Z_$][$a-zA-Z_0-9]*). The input also can not be empty

 @author Kayler
 @since 07/08/2016. */
public class MacroIdentifierChecker implements InputFieldDataChecker<String> {

	private static final Pattern p = Pattern.compile("[$a-zA-Z_0-9]+");

	@Override
	public String errorMsgOnData(@NotNull String data) {
		boolean match = data.length() > 0 && p.matcher(data).matches();
		if (match) {
			return null;
		}
		return Lang.FxControlBundle().getString("InputField.DataCheckers.MacroIdentifier.not_identifier");
	}

	@Override
	public String parse(@NotNull String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return Lang.FxControlBundle().getString("InputField.DataCheckers.MacroIdentifier.type_name");
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
