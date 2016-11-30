
package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.edit;

import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.ChooseCustomControlDialog;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl.EditCustomControlPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 11/13/2016.
 */
public class EditCustomControlAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ChooseCustomControlDialog dialog = new ChooseCustomControlDialog();
		dialog.show();
		CustomControlClass controlClass = dialog.getChosenItem();
		if (dialog.wasCancelled() || controlClass == null) {
			return;
		}
		new EditCustomControlPopup(controlClass).show();
	}
}
