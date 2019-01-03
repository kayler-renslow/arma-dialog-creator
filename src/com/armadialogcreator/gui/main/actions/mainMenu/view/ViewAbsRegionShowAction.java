package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/20/2016.
 */
public class ViewAbsRegionShowAction implements EventHandler<ActionEvent> {
	private int showing = 1;

	@Override
	public void handle(ActionEvent event) {
		if (showing == 1) {
			showing = 0;
		} else {
			showing = 1;
		}
		ArmaDialogCreator.getCanvasView().updateAbsRegion(-1, showing);
	}
}
