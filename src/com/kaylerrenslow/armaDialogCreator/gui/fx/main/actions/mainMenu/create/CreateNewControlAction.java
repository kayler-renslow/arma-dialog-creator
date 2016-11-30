package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl.NewControlDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 @author Kayler
 @since 11/15/2016 */
public class CreateNewControlAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		NewControlDialog dialog = new NewControlDialog(null, null);
		dialog.show();
		if (dialog.wasCancelled()) {
			return;
		}
		String className = dialog.getClassName();
		ControlType controlType = dialog.getControlType();
		boolean backgroundControl = dialog.isBackgroundControl();
		ArmaControlLookup lookup = ArmaControlLookup.findByControlType(controlType);
		ArmaControl control = ArmaControl.createControl(controlType, className, lookup, DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()),
				DataKeys.ENV.get(ArmaDialogCreator.getApplicationData()), Project.getCurrentProject()
		);
		if (backgroundControl) {
			Project.getCurrentProject().getEditingDisplay().getBackgroundControls().add(control);
		} else {
			Project.getCurrentProject().getEditingDisplay().getControls().add(control);
		}
	}
}
