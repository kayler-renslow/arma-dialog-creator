package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor.ControlPropertiesConfigPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 Created by Kayler on 05/27/2016.
 */
public class DefaultComponentContextMenu extends ContextMenu {

	private MenuItem configure = new MenuItem("Configure Properties");

	public DefaultComponentContextMenu(ArmaControl c) {
		getItems().add(configure);

		configure.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new ControlPropertiesConfigPopup(c).show();
			}
		});
	}
}
