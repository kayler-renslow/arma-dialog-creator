package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVDouble;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.DoubleChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import org.jetbrains.annotations.NotNull;

/**
 Checker for Doubles that returns a SerializableValue

 @author Kayler
 @since 05/31/2016. */
public class SVDoubleChecker implements InputFieldDataChecker<SVDouble> {
	private static final DoubleChecker checker = new DoubleChecker();

	@Override
	public String validData(@NotNull String data) {
		return checker.validData(data);
	}

	@Override
	public SVDouble parse(@NotNull String data) {
		Double d = checker.parse(data);
		if (d == null) {
			throw new IllegalStateException("returned value shouldn't be null");
		}
		return new SVDouble(d);
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
