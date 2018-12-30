package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 06/01/2016.
 */
public class ViewFullScreenAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ArmaDialogCreator.getMainWindow().setToFullScreen(true);
	}
}
