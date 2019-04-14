package com.armadialogcreator.gui.main.actions.mainMenu.create;

import com.armadialogcreator.gui.main.actions.NewControlAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 @author Kayler
 @since 11/15/2016 */
public class CreateNewControlAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		NewControlAction action = new NewControlAction(true);
		action.doAction();
	}
}
