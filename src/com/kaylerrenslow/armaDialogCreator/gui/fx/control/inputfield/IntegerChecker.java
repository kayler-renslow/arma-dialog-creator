package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.FXControlLang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Checker for Integers
 Created on 05/31/2016. */
public class IntegerChecker implements InputFieldDataChecker<Integer> {
	@Override
	public String validData(@NotNull String data) {
		try {
			Integer.parseInt(data);
			return null;
		} catch (NumberFormatException e) {
			return FXControlLang.InputField.DataCheckers.Integer.NOT_INTEGER;
		}
	}

	@Override
	public Integer parse(@NotNull String data) {
		return Integer.parseInt(data);
	}

	@Override
	public String getTypeName() {
		return FXControlLang.InputField.DataCheckers.Integer.TYPE_NAME;
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
