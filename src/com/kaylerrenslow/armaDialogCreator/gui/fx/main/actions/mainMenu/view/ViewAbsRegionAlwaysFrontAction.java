package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/20/2016.
 */
public class ViewAbsRegionAlwaysFrontAction implements EventHandler<ActionEvent> {
	private int alwaysFront = 1;

	@Override
	public void handle(ActionEvent event) {
		if (alwaysFront == 1) {
			alwaysFront = 0;
		} else {
			alwaysFront = 1;
		}
		ArmaDialogCreator.getCanvasView().updateAbsRegion(alwaysFront, -1);
	}
}
