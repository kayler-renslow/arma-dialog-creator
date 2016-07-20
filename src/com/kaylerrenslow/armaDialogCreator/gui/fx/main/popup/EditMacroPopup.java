package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.data.MacroRegistry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Popup for editing an existing macro.
 Created on 07/10/2016. */
public class EditMacroPopup extends MacroEditBasePopup {
	
	private final Macro editing;
	
	public EditMacroPopup(@NotNull Macro toEdit) {
		super(ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment());
		this.editing = toEdit;
		setToMacro(editing);
	}
	
	@Override
	protected void ok() {
		if (checkFields()) {
			Macro macro = getMacro();
			if (macro != null) {
				MacroRegistry macroRegistry = ArmaDialogCreator.getApplicationData().getCurrentProject().getMacroRegistry();
				macroRegistry.getMacros().remove(editing);
				macroRegistry.getMacros().add(macro);
			}
			close();
		}
	}
}
