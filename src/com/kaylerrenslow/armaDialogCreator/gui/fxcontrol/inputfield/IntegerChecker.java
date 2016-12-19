package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 Checker for Integers

 @author Kayler
 @since 05/31/2016. */
public class IntegerChecker implements InputFieldDataChecker<Integer> {
	@Override
	public String validData(@NotNull String data) {
		try {
			Integer.parseInt(data);
			return null;
		} catch (NumberFormatException e) {
			return Lang.FxControlBundle().getString("InputField.DataCheckers.Integer.not_integer");
		}
	}

	@Override
	public Integer parse(@NotNull String data) {
		return Integer.parseInt(data);
	}

	@Override
	public String getTypeName() {
		return Lang.FxControlBundle().getString("InputField.DataCheckers.Integer.type_name");
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
