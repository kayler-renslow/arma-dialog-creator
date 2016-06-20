package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.Counter;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor.ControlPropertiesConfigPopup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;

/**
 Created by Kayler on 05/27/2016.
 */
public class DefaultComponentContextMenu extends ContextMenu {

	private static ArrayList<ControlPropertiesConfigPopup> createdPopups = new ArrayList<>();

	public DefaultComponentContextMenu(ArmaControl c) {
		MenuItem configure = new MenuItem(Lang.ContextMenu.DefaultComponent.CONFIGURE);

		Counter counter = new Counter(0, 0, Integer.MAX_VALUE, 1.0, true);
		counter.addUpdateButton(-10, "-10", 0);
		counter.addUpdateButton(10, "+10", 100); //put on end
		CustomMenuItem renderQueueItem = new CustomMenuItem(counter, false);

		Menu renderQueueMenu = new Menu(Lang.ContextMenu.DefaultComponent.RENDER_QUEUE, null, renderQueueItem);

		getItems().addAll(renderQueueMenu, configure);
		configure.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showControlPropertiesPopup(c);
			}
		});
	}

	public static void showControlPropertiesPopup(ArmaControl c) {
		for (ControlPropertiesConfigPopup popup : createdPopups) {
			if (popup.getControl() == c && popup.isShowing()) {
				popup.beepFocus();
				return;
			} else if (!popup.isShowing() && popup.getControl() == c) {
				popup.initializeToControl(c);
			}
		}
		ControlPropertiesConfigPopup popup = new ControlPropertiesConfigPopup(c);
		createdPopups.add(popup);
		popup.show();
	}
}
