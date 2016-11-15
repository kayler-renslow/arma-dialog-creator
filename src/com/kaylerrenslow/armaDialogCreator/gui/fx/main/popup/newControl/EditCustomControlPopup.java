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
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyUpdate;
import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 @author Kayler
 Popup window that allows for editing a {@link CustomControlClass}.
 Created on 11/14/2016. */
public class EditCustomControlPopup extends NewControlPopup {

	private final ControlClass duplicate;
	private final CustomControlClass toEdit;
	private final LinkedList<ControlPropertyUpdate> updates = new LinkedList<>();

	public EditCustomControlPopup(@NotNull CustomControlClass toEdit) {
		this.toEdit = toEdit;
		myStage.setTitle(Lang.ApplicationBundle().getString("Popups.EditCustomControl.popup_title"));

		duplicate = toEdit.getSpecification().constructNewControlClass();
		duplicate.getUpdateGroup().addListener(new UpdateListener<ControlPropertyUpdate>() {
			@Override
			public void update(ControlPropertyUpdate data) {
				updates.add(data);
			}
		});
		setToControlClass(duplicate);
		disableBaseControlMenuButton(true);
	}

	@Override
	protected void ok() {
		for (ControlPropertyUpdate update : updates) {
			toEdit.getControlClass().getUpdateGroup().update(update);
		}
		toEdit.getSpecification().setClassName(duplicate.getClassName());
		close();
	}

}
