package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.SVDouble;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.DoubleChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;

/**
 @author Kayler
 Checker for Doubles that returns a SerializableValue
 Created on 05/31/2016. */
public class SVDoubleChecker implements InputFieldDataChecker<SVDouble> {
	private static final DoubleChecker checker = new DoubleChecker();

	@Override
	public boolean validData(String data) {
		return checker.validData(data);
	}

	@Override
	public SVDouble parse(String data) {
		return new SVDouble(checker.parse(data));
	}

	@Override
	public String getTypeName() {
		return checker.getTypeName();
	}

	@Override
	public boolean allowEmptyData() {
		return checker.allowEmptyData();
	}
}
