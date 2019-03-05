

package com.armadialogcreator.gui.fxcontrol;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 Created by Kayler on 05/15/2016.
 */
public class MenuUtil {
	public static <E extends MenuItem> E addOnAction(E item, MenuItemEventHandler<E> eventHandler) {
		item.setOnAction(eventHandler);
		eventHandler.setMenuItem(item);
		return item;
	}

	public static <E extends MenuItem> E addOnAction(E item, EventHandler<ActionEvent> eventHandler) {
		item.setOnAction(eventHandler);
		return item;
	}
}
