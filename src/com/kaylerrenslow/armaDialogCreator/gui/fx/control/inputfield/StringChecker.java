package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.FXControlLang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Checker for normal Strings. Will allow any input.
 Created on 05/31/2016. */
public class StringChecker implements InputFieldDataChecker<String> {
	@Override
	public String validData(@NotNull String data) {
		return null;
	}
	
	@Override
	public String parse(@NotNull String data) {
		return data;
	}
	
	@Override
	public String getTypeName() {
		return FXControlLang.InputField.DataCheckers.StringChecker.TYPE_NAME;
	}
	
	@Override
	public boolean allowEmptyData() {
		return true;
	}
}
