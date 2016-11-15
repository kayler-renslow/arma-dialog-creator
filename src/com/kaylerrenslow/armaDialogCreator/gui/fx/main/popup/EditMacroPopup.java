/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

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
