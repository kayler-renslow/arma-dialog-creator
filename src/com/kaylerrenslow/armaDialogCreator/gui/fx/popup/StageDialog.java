/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.popup;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 @author Kayler
 A wrapper class for {@link StagePopup} that is for dialogs.
 Created on 08/20/2016. */
public class StageDialog<T extends Parent> extends StagePopup<VBox>{

	/** Root element of the dialog body. This is the element where all content should be placed. */
	protected final T myRootElement;

	public StageDialog(Stage primaryStage, T myRootDialogElement, String title, boolean canCancel, boolean canOk, boolean hasHelp) {
		super(primaryStage, new VBox(5), title);
		this.myRootElement = myRootDialogElement;
		super.myRootElement.setPadding(new Insets(10));
		super.myRootElement.getChildren().addAll(myRootDialogElement, new Separator(Orientation.HORIZONTAL), getResponseFooter(canCancel, canOk, hasHelp));
		myStage.initModality(Modality.APPLICATION_MODAL);
	}

	@Override
	protected final BorderPane getResponseFooter(boolean addCancel, boolean addOk, boolean addHelpButton) {
		return super.getResponseFooter(addCancel, addOk, addHelpButton);
	}

	/** Implementation is: {@link Stage#showAndWait()} */
	@Override
	public void show() {
		showAndWait();
	}
}
