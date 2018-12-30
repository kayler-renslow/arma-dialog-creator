package com.armadialogcreator.gui.main.actions.mainMenu.file;

import com.armadialogcreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 10/16/2016.
 */
public class FileRestartAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ArmaDialogCreator.restartApplication(true);
	}
}
