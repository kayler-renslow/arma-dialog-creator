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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 @author Kayler
 A wrapper class for {@link StagePopup} that is for dialogs.
 Created on 08/20/2016. */
public class StageDialog<T extends Parent> {
	private final StagePopup<VBox> popupBase;
	/** Root element of the dialog */
	protected final T myRootElement;

	public StageDialog(Stage primaryStage, String title, T myRootElement, boolean canCancel, boolean canOk, boolean hasHelp) {
		popupBase = new StagePopup<>(primaryStage, new VBox(5), title);
		popupBase.myRootElement.setPadding(new Insets(10));
		this.myRootElement = myRootElement;
		popupBase.myRootElement.getChildren().addAll(myRootElement, new Separator(Orientation.HORIZONTAL), popupBase.getResponseFooter(canCancel, canOk, hasHelp));
		popupBase.myStage.initModality(Modality.APPLICATION_MODAL);
	}

	/**@see Stage#sizeToScene() */
	public void sizeToScene() {
		popupBase.myStage.sizeToScene();
	}

	public void setStageSize(double w, double h) {
		popupBase.myStage.setWidth(w);
		popupBase.myStage.setHeight(h);
	}

	/** @see Stage#initStyle(StageStyle) */
	protected void setInitStageStyle(StageStyle stageStyle) {
		popupBase.myStage.initStyle(stageStyle);
	}

	protected void setOkButtonText(String text) {
		popupBase.btnOk.setText(text);
	}

	protected void setCancelButtonText(String text) {
		popupBase.btnCancel.setText(text);
	}

	protected void setHelpButtonText(String text) {
		popupBase.btnHelp.setText(text);
	}

	/** Called when the cancel button is pressed. Default implementation closes the dialog */
	protected void cancel() {
		popupBase.close();
	}

	/** Called when the ok button is pressed. Default implementation closes the dialog. */
	protected void ok() {
		popupBase.close();
	}

	/** Called when the help button is pressed. Default implementation does nothing. */
	protected void help() {
	}

	/** Implementation is: {@link Stage#showAndWait()} */
	public void show() {
		popupBase.showAndWait();
	}
}
