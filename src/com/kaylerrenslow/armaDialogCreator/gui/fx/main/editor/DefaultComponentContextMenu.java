/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.Counter;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor.ControlPropertiesConfigPopup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 Created by Kayler on 05/27/2016.
 */
public class DefaultComponentContextMenu extends ContextMenu {

	private static LinkedList<ControlPropertiesConfigPopup> createdPopups = new LinkedList<>();

	public DefaultComponentContextMenu(ArmaControl c) {
		MenuItem configure = new MenuItem(Lang.ApplicationBundle().getString("ContextMenu.DefaultComponent.configure"));

		Counter counter = new Counter(0, 0, Integer.MAX_VALUE, 1.0, true);
		counter.addUpdateButton(-10, "-10", 0);
		counter.addUpdateButton(10, "+10", 100); //put on end
		CustomMenuItem renderQueueItem = new CustomMenuItem(counter, false);

		Menu renderQueueMenu = new Menu(Lang.ApplicationBundle().getString("ContextMenu.DefaultComponent.render_queue"), null, renderQueueItem);

		getItems().addAll(/*renderQueueMenu, */configure);
		configure.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showControlPropertiesPopup(c);
			}
		});
	}

	public static void showControlPropertiesPopup(@NotNull ArmaControl c) {
		for (ControlPropertiesConfigPopup popup : createdPopups) {
			if (popup.getControl() == c && popup.isShowing()) {
				popup.beepFocus();
				return;
			}
		}
		ControlPropertiesConfigPopup popup = new ControlPropertiesConfigPopup(c);
		popup.getOnHiddenProperty().addListener(new ChangeListener<EventHandler<WindowEvent>>() {
			@Override
			public void changed(ObservableValue<? extends EventHandler<WindowEvent>> observable, EventHandler<WindowEvent> oldValue, EventHandler<WindowEvent> newValue) {
				createdPopups.remove(popup);
			}
		});
		createdPopups.add(popup);
		popup.show();
	}
}
