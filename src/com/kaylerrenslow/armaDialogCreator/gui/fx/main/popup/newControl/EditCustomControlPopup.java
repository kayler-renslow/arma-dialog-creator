/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassUpdate;
import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 Popup window that allows for editing a {@link CustomControlClass}.

 @author Kayler
 @since 11/14/2016. */
public class EditCustomControlPopup extends NewCustomControlPopup {

	private final CustomControlClass toEdit;
	private final LinkedList<ControlClassUpdate> updates = new LinkedList<>();

	public EditCustomControlPopup(@NotNull CustomControlClass toEdit) {
		this.toEdit = toEdit;
		myStage.setTitle(Lang.ApplicationBundle().getString("Popups.EditCustomControl.popup_title"));

		ControlClass duplicate = toEdit.getSpecification().constructNewControlClass();
		duplicate.getControlClassUpdateGroup().addListener(new UpdateListener<ControlClassUpdate>() {
			@Override
			public void update(ControlClassUpdate data) {
				updates.add(data);
			}
		});

		setToControlClass(duplicate);
		getTaComment().setText(toEdit.getComment());
		hideBaseControlMenuButton(true);
	}

	@Override
	protected void ok() {
		for (ControlClassUpdate update : updates) {
			toEdit.getControlClass().getControlClassUpdateGroup().update(update);
		}
		toEdit.setComment(getTaComment().getText());
		close();
	}

}
