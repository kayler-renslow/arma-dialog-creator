package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public final class ApplicationData {

	private ValueObserver<ArmaDisplay> editingDisplay = new ValueObserver<>(new ArmaDisplay(0));


	/** Get the display that the dialog creator is editing right now. */
	@NotNull
	public ArmaDisplay getEditingDisplay() {
		return editingDisplay.getValue();
	}

	@NotNull
	public ValueObserver<ArmaDisplay> getEditingDisplayObserver() {
		return editingDisplay;
	}

	/** Set the display that the dialog creator is to edit (notifies the valueObserver for the display as well). */
	public void setEditingDisplay(@NotNull ArmaDisplay display) {
		this.editingDisplay.updateValue(display);
	}

	public MacroRegistry getMacroRegistry() {
		return MacroRegistry.INSTANCE;
	}

}
