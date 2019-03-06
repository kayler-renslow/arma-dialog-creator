package com.armadialogcreator.gui.main.actions.mainMenu.file;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.gui.main.AskSaveProjectDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 10/07/2016.
 */
public class FileExitAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		AskSaveProjectDialog dialog = new AskSaveProjectDialog();
		dialog.show();
		if (dialog.wasCancelled()) {
			return;
		}
		if (dialog.saveProgress()) {
			ApplicationManager.instance.saveProject();
		}
		ApplicationManager.instance.closeApplication();
	}
}
