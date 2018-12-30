package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.gui.main.editor.UICanvasConfiguration;
import com.armadialogcreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/20/2016.
 */
public class ViewShowGridAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		UICanvasConfiguration conf = ArmaDialogCreator.getCanvasView().getConfiguration();
		conf.setShowGrid(!conf.showGrid());
	}
}
