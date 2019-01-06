package com.armadialogcreator.gui.main.actions.mainMenu.create;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.impl.ArmaControlLookup;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.DataKeys;
import com.armadialogcreator.data.Project;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
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
		ArmaControl control = ArmaControl.createControl(className, lookup, DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()),
				Env.ENV.get(ArmaDialogCreator.getApplicationData()), Project.getCurrentProject()
		);
		if (backgroundControl) {
			Project.getCurrentProject().getEditingDisplay().getBackgroundControls().add(control);
		} else {
			Project.getCurrentProject().getEditingDisplay().getControls().add(control);
		}
	}
}
