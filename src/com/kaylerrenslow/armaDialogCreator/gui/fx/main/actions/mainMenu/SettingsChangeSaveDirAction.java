package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/26/2016.
 */
public class SettingsChangeSaveDirAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		ArmaDialogCreator.showNewSaveLocationsPopup();
	}
}