package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.SVInteger;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IntegerChecker;

/**
 @author Kayler
 Checker for Integers that returns a SerializableValue
 Created on 05/31/2016.
 */
public class SVIntegerChecker implements InputFieldDataChecker<SVInteger> {
	private static final IntegerChecker checker = new IntegerChecker();

	@Override
	public boolean validData(String data) {
		return checker.validData(data);
	}

	@Override
	public SVInteger parse(String data) {
		return new SVInteger(checker.parse(data));
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
