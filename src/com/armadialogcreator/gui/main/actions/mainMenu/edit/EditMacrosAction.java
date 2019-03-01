
package com.armadialogcreator.gui.main.actions.mainMenu.edit;

import com.armadialogcreator.core.Macro;
import com.armadialogcreator.gui.main.popup.ChooseMacroDialog;
import com.armadialogcreator.gui.main.popup.EditMacroPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 08/09/2016.
 */
public class EditMacrosAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ChooseMacroDialog chooseMacroPopup = new ChooseMacroDialog(null);
		chooseMacroPopup.showAndWait();
		Macro chosenMacro = chooseMacroPopup.getChosenItem();
		if (chosenMacro == null) {
			return;
		}
		new EditMacroPopup(chosenMacro).showAndWait();
	}
}
