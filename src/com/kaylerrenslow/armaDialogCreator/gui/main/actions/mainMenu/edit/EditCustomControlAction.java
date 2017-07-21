
package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.edit;

import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.ChooseCustomControlClassDialog;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.newControl.EditCustomControlClassDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 11/13/2016.
 */
public class EditCustomControlAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ChooseCustomControlClassDialog dialog = new ChooseCustomControlClassDialog();
		dialog.show();
		CustomControlClass controlClass = dialog.getChosenItem();
		if (dialog.wasCancelled() || controlClass == null) {
			return;
		}
		new EditCustomControlClassDialog(controlClass).show();
	}
}
