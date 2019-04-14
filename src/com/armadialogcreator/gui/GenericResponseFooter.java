package com.armadialogcreator.gui;

import com.armadialogcreator.lang.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

/**
 A generic footer that provides "help" button, "cancel" button, and "ok" button implementations.

 @author Kayler
 @since 10/07/2016. */
public class GenericResponseFooter extends BorderPane {

	public static final double PREFFERED_BUTTON_OK_WIDTH = 100d;
	public static final double PREFFERED_BUTTON_CANCEL_WIDTH = 75d;

	private final HBox rightContainer;
	private final HBox leftContainer;

	/** The buttons used for the footer */
	protected final Button btnOk, btnCancel, btnHelp;

	public GenericResponseFooter(boolean addCancel, boolean addOk, boolean addHelpButton, EventHandler<ActionEvent> helpEvent, EventHandler<ActionEvent> cancelEvent, EventHandler<ActionEvent> okEvent
	) {
		rightContainer = new HBox(5);
		leftContainer = new HBox(5);
		if (addHelpButton) {
			btnHelp = new Button(Lang.ApplicationBundle().getString("Popups.btn_help"));
			btnHelp.setTooltip(new Tooltip(Lang.ApplicationBundle().getString("Popups.btn_help_tooltip")));
			btnHelp.setOnAction(helpEvent);
			btnHelp.setPrefWidth(50d);
			leftContainer.getChildren().add(btnHelp);
			setLeft(leftContainer);
		} else {
			btnHelp = null;
		}
		if (addCancel) {
			btnCancel = new Button(Lang.ApplicationBundle().getString("Popups.btn_cancel"));
			btnCancel.setOnAction(cancelEvent);
			btnCancel.setPrefWidth(PREFFERED_BUTTON_CANCEL_WIDTH);
			rightContainer.getChildren().add(btnCancel);
		} else {
			btnCancel = null;
		}
		if (addOk) {
			btnOk = new Button(Lang.ApplicationBundle().getString("Popups.btn_ok"));
			btnOk.setOnAction(okEvent);
			btnOk.setPrefWidth(PREFFERED_BUTTON_OK_WIDTH);
			rightContainer.getChildren().add(btnOk);
		} else {
			btnOk = null;
		}

		rightContainer.setAlignment(Pos.BOTTOM_RIGHT);
		rightContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		setRight(rightContainer);
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}

	@NotNull
	public HBox getLeftContainer() {
		return leftContainer;
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
