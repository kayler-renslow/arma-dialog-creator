package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;


/**
 @author Kayler
 Popup for editing an existing macro.
 Created on 07/10/2016. */
public class EditMacroPopup extends MacroEditBasePopup {

	private final Macro editing;

	public EditMacroPopup(@NotNull Macro toEdit) {
		this.editing = toEdit;
		setToMacro(editing);
	}

	@Override
	protected void ok() {
		Macro macro = getMacro();
		if (macro != null) {
			ArmaDialogCreator.getApplicationData().getMacroRegistry().getMacros().remove(editing);
			ArmaDialogCreator.getApplicationData().getMacroRegistry().getMacros().add(macro);
		}
		close();
	}
}
