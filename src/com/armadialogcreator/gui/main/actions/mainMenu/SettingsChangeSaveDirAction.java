package com.armadialogcreator.gui.main.actions.mainMenu;

import com.armadialogcreator.gui.main.popup.ChangeDirectoriesDialog;
import com.armadialogcreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/26/2016.
 */
public class SettingsChangeSaveDirAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		new ChangeDirectoriesDialog(ArmaDialogCreator.getApplicationDataManager().getArma3ToolsDirectory()).show();
	}
}
