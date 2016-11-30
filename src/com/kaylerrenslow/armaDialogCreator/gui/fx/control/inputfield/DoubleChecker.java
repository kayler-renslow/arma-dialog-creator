package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 Checker for Doubles

 @author Kayler
 @since 05/31/2016. */
public class DoubleChecker implements InputFieldDataChecker<Double> {
	@Override
	public String validData(@NotNull String data) {
		try {
			Double.parseDouble(data);
			return null;
		} catch (NumberFormatException e) {
			return Lang.FxControlBundle().getString("InputField.DataCheckers.Double.not_a_number");
		}
	}

	@Override
	public Double parse(@NotNull String data) {
		return Double.parseDouble(data);
	}

	@Override
	public String getTypeName() {
		return Lang.FxControlBundle().getString("InputField.DataCheckers.Double.type_name");
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
