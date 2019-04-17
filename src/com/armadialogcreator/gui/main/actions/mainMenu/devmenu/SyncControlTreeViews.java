package com.armadialogcreator.gui.main.actions.mainMenu.devmenu;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.gui.main.CanvasView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 @author K
 @since 4/17/19 */
public class SyncControlTreeViews implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		CanvasView canvasView = ArmaDialogCreator.getMainWindow().getCanvasView();
		ArmaDisplay display = EditorManager.instance.getEditingDisplay();
		canvasView.getMainControlTreeView().setToUINode(display.getControlNodes());
		canvasView.getBackgroundControlTreeView().setToUINode(display.getBackgroundControlNodes());
	}

}
