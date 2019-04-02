package com.armadialogcreator.gui;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.gui.main.popup.editor.ConfigPropertiesConfigPopup;
import com.armadialogcreator.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;


/**
 Created by Kayler on 05/27/2016.
 */
public class DefaultComponentContextMenu extends ContextMenu {

	private static LinkedList<ConfigPropertiesConfigPopup> createdPopups = new LinkedList<>();

	public DefaultComponentContextMenu(@NotNull ArmaControl c) {
		MenuItem configure = new MenuItem(Lang.ApplicationBundle().getString("ContextMenu.DefaultComponent.configure"));

		/*
		Counter counter = new Counter(0, 0, Integer.MAX_VALUE, 1.0, true);
		counter.addUpdateButton(-10, "-10", 0);
		counter.addUpdateButton(10, "+10", 100); //put on end
		CustomMenuItem renderQueueItem = new CustomMenuItem(counter, false);

		Menu renderQueueMenu = new Menu(Lang.ApplicationBundle().getString("ContextMenu.DefaultComponent.render_queue"), null, renderQueueItem);
		*/
		getItems().addAll(/*renderQueueMenu */configure);
		configure.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				showControlPropertiesPopup(c);
			}
		});
	}

	public static void showControlPropertiesPopup(@NotNull ArmaControl c) {
		for (ConfigPropertiesConfigPopup popup : createdPopups) {
			if (popup.getControl() == c && popup.isShowing()) {
				popup.beepFocus();
				return;
			}
		}
		ConfigPropertiesConfigPopup popup = new ConfigPropertiesConfigPopup(c);
		popup.getOnHiddenProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends EventHandler<WindowEvent>> observable, EventHandler<WindowEvent> oldValue, EventHandler<WindowEvent> newValue) {
				createdPopups.remove(popup);
			}
		});
		createdPopups.add(popup);
		popup.show();
	}
}
