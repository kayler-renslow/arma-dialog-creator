package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVString;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputFieldDataChecker;
import org.jetbrains.annotations.NotNull;

/**
 Checker for Arma Strings that returns a SerializableValue

 @author Kayler
 @since 05/31/2016. */
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
