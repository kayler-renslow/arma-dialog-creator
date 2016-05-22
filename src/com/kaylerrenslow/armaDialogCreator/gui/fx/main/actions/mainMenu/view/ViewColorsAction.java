package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.CanvasViewColorsPopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/20/2016.
 */
public class ViewColorsAction implements EventHandler<ActionEvent>{
	@Override
	public void handle(ActionEvent event) {
		CanvasViewColorsPopup popup = new CanvasViewColorsPopup(ArmaDialogCreator.getPrimaryStage());
		popup.show();
	}
}
