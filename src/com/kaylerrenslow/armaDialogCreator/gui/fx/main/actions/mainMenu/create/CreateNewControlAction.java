/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
