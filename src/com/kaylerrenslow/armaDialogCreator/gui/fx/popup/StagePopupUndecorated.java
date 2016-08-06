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
	 Creates a new JavaFX Stage based popup window that has not "decoration" (not minimize button, maximize button, or close button). The window is move-able by dragging the root element, however.

	 @see StagePopup
	 */
	public StagePopupUndecorated(@Nullable Stage primaryStage, E rootElement, String title) {
		super(primaryStage, rootElement, title);
		myStage.initStyle(StageStyle.UNDECORATED);
		rootElement.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		rootElement.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				myStage.setX(event.getScreenX() - xOffset);
				myStage.setY(event.getScreenY() - yOffset);
				stageMoved();
			}
		});
	}

	/** Invoked whenever the stage is moved by user. Default implementation does nothing. */
	protected void stageMoved() {}

}
