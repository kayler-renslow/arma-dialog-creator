package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupControl extends ArmaControlGroup {
	public ControlGroupControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(name, resolution, RendererLookup.CONTROL_GROUP, env);
		defineType(ControlType.CONTROLS_GROUP);
	}

	public ControlGroupControl(@NotNull String name, int idc, @NotNull ControlStyleGroup style, Expression x, Expression y, Expression width, Expression height, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(name, idc, ControlType.CONTROLS_GROUP, style, x, y, width, height, resolution, RendererLookup.CONTROL_GROUP, env);
	}

}
