package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.canvas.UICanvasConfiguration;
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
