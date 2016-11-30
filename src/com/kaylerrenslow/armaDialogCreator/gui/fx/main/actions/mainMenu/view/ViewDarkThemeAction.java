package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 05/24/2016.
 */
public class ViewDarkThemeAction implements EventHandler<ActionEvent> {
	private boolean checked = false;
	
	public ViewDarkThemeAction(boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public void handle(ActionEvent event) {
		checked = !checked;
		ArmaDialogCreator.setToDarkTheme(checked);
	}
}
