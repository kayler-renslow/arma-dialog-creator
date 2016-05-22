package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

/**
 Created by Kayler on 05/20/2016.
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 Created by Kayler on 05/15/2016.
 */
public class MenuUtil {
	public static MenuItem addOnAction(MenuItem item, EventHandler<ActionEvent> eventHandler) {
		item.setOnAction(eventHandler);
		return item;
	}
}
