package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 Checker for normal Strings. Will allow any input.

 @author Kayler
 @since 05/31/2016. */
public class StringChecker implements InputFieldDataChecker<String> {
	@Override
	public String validData(@NotNull String data) {
		return null;
	}

	@Override
	public String parse(@NotNull String data) {
		return data;
	}

	@Override
	public String getTypeName() {
		return Lang.FxControlBundle().getString("InputField.DataCheckers.StringChecker.type_name");
	}

	@Override
	public boolean allowEmptyData() {
		return true;
	}
}
