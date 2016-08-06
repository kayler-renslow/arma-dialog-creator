package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;

/**
 Created by Kayler on 08/04/2016.
 */
public interface Display extends ControlHolder{
	
	/** Any time the display or its controls get updated, notify this group. */
	UpdateListenerGroup<Object> getUpdateListenerGroup();
	
	void resolutionUpdate(Resolution newResolution);
}
