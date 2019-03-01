package com.armadialogcreator.gui.main.actions.mainMenu.file;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.gui.main.AskSaveProjectDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileCloseProjectAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		AskSaveProjectDialog dialog = new AskSaveProjectDialog();
		if (dialog.wasCancelled()) {
			return;
		}
		if (dialog.saveProgress()) {
			ApplicationManager.instance.saveProject();
		}
		//todo
	}
}
