/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 Created by Kayler on 10/16/2016.
 */
public class ADCMustRestartDialog extends StageDialog<VBox> {

	public ADCMustRestartDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("Popups.generic_popup_title"), true, true, false);
		footer.getBtnOk().setText(Lang.ApplicationBundle().getString("Popups.MustRestart.restart"));
		myRootElement.getChildren().add(new Label(Lang.ApplicationBundle().getString("Popups.MustRestart.must_restart_for_changes")));
	}

	/** Shows a new dialog asking user if they want to restart. Will return true if the user wants to restart, false if they don't */
	public static boolean getResponse() {
		ADCMustRestartDialog dialog = new ADCMustRestartDialog();
		dialog.show();
		return !dialog.wasCancelled();
	}
}
