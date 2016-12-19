
package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.edit;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.ChooseMacroPopup;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.EditMacroPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 08/09/2016.
 */
public class EditMacrosAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ChooseMacroPopup<SerializableValue> chooseMacroPopup = new ChooseMacroPopup<>(SerializableValue.class);
		chooseMacroPopup.showAndWait();
		Macro<SerializableValue> chosenMacro = chooseMacroPopup.getChosenItem();
		if (chosenMacro == null) {
			return;
		}
		new EditMacroPopup(chosenMacro).showAndWait();
	}
}
