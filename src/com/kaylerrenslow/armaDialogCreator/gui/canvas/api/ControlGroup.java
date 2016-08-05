package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import java.util.List;

/**
 Created by Kayler on 08/04/2016.
 */
public interface ControlGroup extends Control{
	
	List<? extends Control> getControls();
	
}
