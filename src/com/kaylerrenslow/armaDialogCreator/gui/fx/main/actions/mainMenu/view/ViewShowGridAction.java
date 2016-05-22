package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/20/2016.
 */
public class ViewShowGridAction implements EventHandler<ActionEvent> {
	private boolean showing = true;

	@Override
	public void handle(ActionEvent event) {
		showing = !showing;
		ArmaDialogCreator.getCanvasView().showGrid(showing);
	}
}
