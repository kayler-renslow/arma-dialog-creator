package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 Checker for Doubles
 Created on 05/31/2016. */
public class DoubleChecker implements InputFieldDataChecker<Double> {
	@Override
	public boolean validData(String data) {
		if (data == null) {
			return false;
		}
		try {
			Double.parseDouble(data);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public Double parse(String data) {
		return Double.parseDouble(data);
	}

	@Override
	public String getTypeName() {
		return "Floating Point Number";
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
