package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControlGroup;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.core.ControlPropertyLookup;
import com.armadialogcreator.core.SpecificationRegistry;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupControl extends ArmaControlGroup {

	public ControlGroupControl(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull Env env,
							   @NotNull SpecificationRegistry registry) {
		super(name, ArmaControlLookup.ControlsGroup, resolution, env, registry);
		findProperty(ControlPropertyLookup.IDC).setDefaultValue(true, idc);
	}

}
