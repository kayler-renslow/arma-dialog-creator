package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl.NewCustomControlPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 07/11/2016.
 */
public class CreateNewCustomControlAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		new NewCustomControlPopup().show();
	}
}
