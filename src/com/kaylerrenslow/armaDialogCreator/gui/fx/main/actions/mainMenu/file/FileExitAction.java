package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 10/07/2016.
 */
public class FileExitAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ArmaDialogCreator.closeApplication();
	}
}
