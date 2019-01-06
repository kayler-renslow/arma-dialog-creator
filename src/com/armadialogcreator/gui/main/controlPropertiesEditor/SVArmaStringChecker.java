package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.sv.SVString;
import com.armadialogcreator.gui.fxcontrol.inputfield.ArmaStringChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputFieldDataChecker;
import org.jetbrains.annotations.NotNull;

/**
 Checker for Arma Strings that returns a SerializableValue

 @author Kayler
 @since 05/31/2016. */
public class SVArmaStringChecker implements InputFieldDataChecker<SVString> {

	private static final ArmaStringChecker checker = new ArmaStringChecker();

	@Override
	public String errorMsgOnData(@NotNull String data) {
		return checker.errorMsgOnData(data);
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
