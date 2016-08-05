package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;

import java.util.List;

/**
 Created by Kayler on 08/04/2016.
 */
public interface Display {
	
	enum DisplayUpdate {
		ADD_CONTROL, REMOVE_CONTROL
	}
	
	List<? extends Control> getControls();
	
	/** Any time the display or its controls get updated, notify this group. */
	UpdateListenerGroup<DisplayUpdate> getUpdateListenerGroup();
}
