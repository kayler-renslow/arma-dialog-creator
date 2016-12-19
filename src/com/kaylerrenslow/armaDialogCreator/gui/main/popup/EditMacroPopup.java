package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
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
	}

	@Override
	protected void ok() {
		if (checkFields()) {
			Macro macro = getMacro();
			if (macro != null) {
				editing.setValue(macro.getValue());
				editing.setComment(macro.getComment());
				editing.setKey(macro.getKey());
			}
			close();
		}
	}
}
