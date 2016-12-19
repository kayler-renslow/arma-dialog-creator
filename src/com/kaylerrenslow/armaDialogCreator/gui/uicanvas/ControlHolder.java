package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;

/**
 Created by Kayler on 08/05/2016.
 */
public interface ControlHolder<C extends CanvasControl> {
	ControlList<C> getControls();
	DataContext getUserData();
}
