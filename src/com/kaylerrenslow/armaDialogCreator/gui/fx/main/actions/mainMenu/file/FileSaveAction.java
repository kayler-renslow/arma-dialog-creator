package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileSaveAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		try {
			ArmaDialogCreator.getApplicationDataManager().saveProject();
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
	}
}
