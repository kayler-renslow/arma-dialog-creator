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

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 A generic footer that provides "help" button, "cancel" button, and "ok" button implementations.
 Created on 10/07/2016. */
public class GenericResponseFooter extends BorderPane {

	private final HBox rightContainer;

	/** The buttons used for the footer */
	protected final Button btnOk, btnCancel, btnHelp;

	public GenericResponseFooter(boolean addCancel, boolean addOk, boolean addHelpButton, EventHandler<ActionEvent> helpEvent, EventHandler<ActionEvent> cancelEvent, EventHandler<ActionEvent> okEvent
	) {
		rightContainer = new HBox(5);
		if (addHelpButton) {
			btnHelp = new Button(Lang.ApplicationBundle.getString("Popups.btn_help"));
			btnHelp.setTooltip(new Tooltip(Lang.ApplicationBundle.getString("Popups.btn_help_tooltip")));
			btnHelp.setOnAction(helpEvent);
			btnHelp.setPrefWidth(50d);
			setLeft(btnHelp);
		} else {
			btnHelp = null;
		}
		if (addCancel) {
			btnCancel = new Button(Lang.ApplicationBundle.getString("Popups.btn_cancel"));
			btnCancel.setOnAction(cancelEvent);
			btnCancel.setPrefWidth(75d);
			rightContainer.getChildren().add(btnCancel);
		} else {
			btnCancel = null;
		}
		if (addOk) {
			btnOk = new Button(Lang.ApplicationBundle.getString("Popups.btn_ok"));
			btnOk.setOnAction(okEvent);
			btnOk.setPrefWidth(100d);
			rightContainer.getChildren().add(btnOk);
		} else {
			btnOk = null;
		}
		rightContainer.setAlignment(Pos.BOTTOM_RIGHT);
		setRight(rightContainer);
	}

	@NotNull
	public HBox getRightContainer() {
		return rightContainer;
	}

	public Button getBtnOk() {
		return btnOk;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnHelp() {
		return btnHelp;
	}
}
