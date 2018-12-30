package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.control.Macro;
import com.armadialogcreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;

/**
 Popup for editing an existing macro.

 @author Kayler
 @since 07/10/2016. */
public class EditMacroPopup extends MacroEditBasePopup {

	private final Macro editing;

	public EditMacroPopup(@NotNull Macro toEdit) {
		super(ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment());
		this.editing = toEdit;
		setToMacro(editing);
		addDeleteButton();
	}

	@Override
	protected void ok() {
		if (checkFields()) {
			Macro macro = getNewMacro();
			if (macro != null) {
				editing.setValue(macro.getValue());
				editing.setComment(macro.getComment());
				editing.setKey(macro.getKey());
			}
			close();
		}
	}
}
