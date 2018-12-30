package com.armadialogcreator.gui.main.actions.mainMenu.edit;

import com.armadialogcreator.gui.main.popup.ViewChangesPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/20/2016.
 */
public class EditViewChangesAction implements EventHandler<ActionEvent>{
	@Override
	public void handle(ActionEvent event) {
		ViewChangesPopup popup = new ViewChangesPopup();
		popup.show();
	}
}
