package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.NewMacroPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/26/2016.
 */
public class CreateMacroAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		NewMacroPopup popup = new NewMacroPopup();
		popup.showAndWait();
	}
}
