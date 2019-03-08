package com.armadialogcreator.data;

import com.armadialogcreator.application.ADCData;
import com.armadialogcreator.application.Configurable;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 3/7/19 */
public class DefaultValueSheet implements ADCData {

	public DefaultValueSheet() {
	}

	@Override
	@NotNull
	public String getDataID() {
		return "default-value-sheet";
	}

	@Override
	public void loadFromConfigurable(@NotNull Configurable config) {

	}

	@Override
	public void exportToConfigurable(@NotNull Configurable configurable) {

	}
}
