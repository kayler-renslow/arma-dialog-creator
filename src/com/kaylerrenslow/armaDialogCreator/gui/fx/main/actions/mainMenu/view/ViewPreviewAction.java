package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.gui.fx.preview.PreviewPopupWindow;
import com.kaylerrenslow.armaDialogCreator.io.ApplicationDataManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 06/14/2016.
 */
public class ViewPreviewAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		PreviewPopupWindow.getInstance().showDisplay(ApplicationDataManager.applicationData.getEditingDisplay());
	}
}
