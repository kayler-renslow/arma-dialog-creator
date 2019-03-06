package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControlGroup;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupControl extends ArmaControlGroup {

	public ControlGroupControl(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull Env env,
							   @NotNull ArmaDisplay display) {
		super(name, ArmaControlLookup.ControlsGroup, resolution, env, display);
		findProperty(ConfigPropertyLookup.IDC).setValue(idc);
	}

}
