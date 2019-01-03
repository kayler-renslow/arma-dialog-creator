package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.control.Macro;
import com.armadialogcreator.lang.Lang;


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
