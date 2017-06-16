package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.SpecificationRegistry;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupControl extends ArmaControlGroup {

	public ControlGroupControl(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, idc, resolution, RendererLookup.ControlGroup, env, registry);
	}

}
