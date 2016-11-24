/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 @author Kayler
 @since 11/23/2016 */
public class CouldNotLoadProjectDialog extends StageDialog<VBox> {
	public CouldNotLoadProjectDialog(Exception e) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("ProjectInitWindow.could_not_load_project"), false, true, false);
		final TextArea taError = new TextArea(e.getMessage());
		taError.setEditable(false);
		myStage.setResizable(false);
		myRootElement.getChildren().addAll(
				new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.could_not_load_project")),
				new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.reason")),
				taError
		);
	}
}

