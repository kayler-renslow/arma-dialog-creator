package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupControl extends ArmaControlGroup {
	public ControlGroupControl(@NotNull String name, @NotNull ArmaResolution resolution) {
		super(name, resolution, ControlGroupRenderer.class, null, null);
	}

	public ControlGroupControl(@NotNull String name, int idc, @NotNull ControlType type, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull ArmaResolution resolution) {
		super(name, idc, type, style, x, y, width, height, resolution, ControlGroupRenderer.class, null, null);
	}

}
