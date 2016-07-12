package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create;

import com.kaylerrenslow.armaDialogCreator.data.Macro;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.NewMacroPopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/26/2016.
 */
public class CreateMacroAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		NewMacroPopup popup = new NewMacroPopup();
		popup.showAndWait();
		Macro macro = popup.getCreatedMacro();
		if (macro != null) {
			ArmaDialogCreator.getApplicationData().getMacroRegistry().getMacros().add(macro);
		}
	}
}
