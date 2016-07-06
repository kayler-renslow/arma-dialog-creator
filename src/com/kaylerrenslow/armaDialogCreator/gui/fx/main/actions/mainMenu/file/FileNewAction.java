package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileNewAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		//todo THIS IS TEST CODE. DELETE THIS LATER (written July 5 2016)
		try {
			StagePopup popup = StagePopup.newFxmlInstance(ArmaDialogCreator.getPrimaryStage(), getClass().getResource("/com/kaylerrenslow/armaDialogCreator/gui/fx/popup/newControl.fxml"), "test");
			popup.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//todo end test code
	}
}
