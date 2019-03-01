package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.core.Macro;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.data.MacroRegistry;
import com.armadialogcreator.lang.Lang;


/**
 Popup for creating a new macro.

 @author Kayler
 @since 07/10/2016. */
public class NewMacroPopup extends MacroEditBasePopup {

	public NewMacroPopup() {
		super(ExpressionEnvManager.instance.getEnv());
		myStage.setTitle(Lang.ApplicationBundle().getString("Popups.MacroNew.popup_title"));
	}

	@Override
	protected void ok() {
		if (checkFields()) {
			Macro macro = getNewMacro();
			if (macro != null) {
				MacroRegistry.instance.getProjectMacros().addMacro(macro);
			}
			close();
		}
	}
}
