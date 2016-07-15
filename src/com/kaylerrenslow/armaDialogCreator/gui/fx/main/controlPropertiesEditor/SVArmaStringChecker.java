package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVString;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Checker for Arma Strings that returns a SerializableValue
 Created on 05/31/2016. */
public class SVArmaStringChecker implements InputFieldDataChecker<SVString> {

	private static final ArmaStringChecker checker = new ArmaStringChecker();

	@Override
	public String validData(@NotNull String data) {
		return checker.validData(data);
	}

	@Override
	public SVString parse(@NotNull String data) {
		return new SVString(data);
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
