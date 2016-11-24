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
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 Popup window that allows for editing a {@link CustomControlClass}.

 @author Kayler
 @since 11/14/2016. */
public class EditCustomControlPopup extends NewCustomControlPopup {

	private final CustomControlClass toEdit;
	private final LinkedList<ControlClassUpdate> updates = new LinkedList<>();
	private final ControlClass duplicate;

	public EditCustomControlPopup(@NotNull CustomControlClass toEdit) {
		this.toEdit = toEdit;
		myStage.setTitle(Lang.ApplicationBundle().getString("Popups.EditCustomControl.popup_title"));

		duplicate = toEdit.getSpecification().constructNewControlClass(Project.getCurrentProject());
		duplicate.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
				updates.add(data);
			}
		});

		getTaComment().setText(toEdit.getComment()); //set before control class to make sure preview is correct
		setToControlClass(duplicate);
		hideBaseControlMenuButton(true);
	}

	@Override
	protected void ok() {
		for (ControlClassUpdate update : updates) {
			toEdit.getControlClass().update(update, false);
		}
		toEdit.setComment(getTaComment().getText());
		close();
	}

}
