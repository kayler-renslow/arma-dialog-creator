package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;

/**
 Created by Kayler on 08/05/2016.
 */
public interface ControlHolder {
	ReadOnlyList<? extends Control> getControls();
	
	void addControl(Control control);
	void addControl(int index, Control toAdd);
	int indexOf(Control control);
	boolean removeControl(Control control);
}
