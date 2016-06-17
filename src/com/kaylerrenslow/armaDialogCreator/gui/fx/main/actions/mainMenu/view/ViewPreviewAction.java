package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.gui.fx.preview.PreviewPopupWindow;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 06/14/2016.
 */
public class ViewPreviewAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		PreviewPopupWindow.getInstance().showDisplay(ArmaDialogCreator.getApplicationData().getEditingDisplay());
	}
}
