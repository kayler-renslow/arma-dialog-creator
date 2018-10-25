package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;


/**
 Popup for creating a new macro.

 @author Kayler
 @since 07/10/2016. */
public class NewMacroPopup extends MacroEditBasePopup {

	public NewMacroPopup() {
		super(ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment());
		myStage.setTitle(Lang.ApplicationBundle().getString("Popups.MacroNew.popup_title"));
	}

	@Override
	protected void ok() {
		if (checkFields()) {
			Macro macro = getNewMacro();
			if (macro != null) {
				ArmaDialogCreator.getApplicationData().getCurrentProject().getMacroRegistry().addMacro(macro);
			}
			close();
		}
	}
}
