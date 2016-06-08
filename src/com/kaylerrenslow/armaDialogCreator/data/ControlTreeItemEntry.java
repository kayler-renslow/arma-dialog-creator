package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;

/**
 Created by Kayler on 06/07/2016.
 */
public class ControlTreeItemEntry extends TreeItemEntry{
	private final ArmaControl myArmaControl;

	public ControlTreeItemEntry(ArmaControl control) {
		myArmaControl = control;
	}

	public ArmaControl getMyArmaControl() {
		return myArmaControl;
	}

	@Override
	public boolean isStructural() {
		return false;
	}
}
