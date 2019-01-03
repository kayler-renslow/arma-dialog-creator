package com.armadialogcreator.canvas;

import com.armadialogcreator.util.DataContext;

/**
 Created by Kayler on 08/05/2016.
 */
public interface ControlHolder<C extends CanvasControl> {
	ControlList<C> getControls();

	DataContext getUserData();
}
