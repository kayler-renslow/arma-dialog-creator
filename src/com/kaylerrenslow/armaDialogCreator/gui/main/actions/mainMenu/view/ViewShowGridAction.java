package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.gui.main.editor.UICanvasConfiguration;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
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
