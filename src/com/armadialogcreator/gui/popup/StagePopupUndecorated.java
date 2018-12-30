package com.armadialogcreator.gui.popup;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/30/2016.
 */
public class StagePopupUndecorated<E extends Parent> extends StagePopup<E> {
	private double xOffset = 0;
	private double yOffset = 0;


	/**
	 Creates a new JavaFX Stage based popup window that has not "decoration"
	 (not minimize button, maximize button, or close button).
	 The window is move-able by dragging the root element, however.

	 @see StagePopup
	 */
	public StagePopupUndecorated(@Nullable Stage primaryStage, E rootElement, String title) {
		super(primaryStage, rootElement, title);
		myStage.initStyle(StageStyle.UNDECORATED);
		rootElement.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					return;
				}
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		rootElement.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					return;
				}
				myStage.setX(event.getScreenX() - xOffset);
				myStage.setY(event.getScreenY() - yOffset);
				stageMoved();
			}
		});
	}

	/** Invoked whenever the stage is moved by user. Default implementation does nothing. */
	protected void stageMoved() {
	}

}
