package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public final class ApplicationData {

	private ArmaDisplay editingDisplay = new ArmaDisplay(0);


	/** Get the display that the dialog creator is editing right now. */
	@NotNull
	public ArmaDisplay getEditingDisplay() {
		return editingDisplay;
	}

	/** Set the display that the dialog creator is to edit. */
	public void setEditingDisplay(@NotNull ArmaDisplay display) {
		this.editingDisplay = display;
	}

}
