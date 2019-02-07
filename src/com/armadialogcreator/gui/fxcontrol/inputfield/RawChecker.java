package com.armadialogcreator.gui.fxcontrol.inputfield;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SVRaw;
import com.armadialogcreator.lang.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Checker for Raw values. Will allow any input.

 @author Kayler
 @since 05/31/2016. */
public class RawChecker implements InputFieldDataChecker<SVRaw> {
	private final PropertyType substituteType;

	public RawChecker(@Nullable PropertyType substituteType) {
		this.substituteType = substituteType;
	}

	@Override
	public String errorMsgOnData(@NotNull String data) {
		return null;
	}

	@Override
	public SVRaw parse(@NotNull String data) {
		return new SVRaw(data, substituteType);
	}

	@Override
	public String getTypeName() {
		return Lang.FxControlBundle().getString("InputField.DataCheckers.RawChecker.type_name");
	}

	@Override
	public boolean allowEmptyData() {
		return true;
	}
}
