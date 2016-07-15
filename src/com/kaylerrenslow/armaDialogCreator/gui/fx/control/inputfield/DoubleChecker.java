package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.FXControlLang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Checker for Doubles
 Created on 05/31/2016. */
public class DoubleChecker implements InputFieldDataChecker<Double> {
	@Override
	public String validData(@NotNull String data) {
		try {
			Double.parseDouble(data);
			return null;
		} catch (NumberFormatException e) {
			return FXControlLang.InputField.DataCheckers.Double.NOT_A_NUMBER;
		}
	}

	@Override
	public Double parse(@NotNull String data) {
		return Double.parseDouble(data);
	}

	@Override
	public String getTypeName() {
		return FXControlLang.InputField.DataCheckers.Double.TYPE_NAME;
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
