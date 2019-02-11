package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.expression.Env;

/**
 Renderer for a ControlGroup control. Use for controls whose classes that extends ArmaControlGroup

 @author Kayler
 @since 07/04/2016. */
public class ControlGroupRenderer extends ArmaControlRenderer {

	public ControlGroupRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
	}
}
