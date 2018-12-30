
package com.armadialogcreator.gui.main.actions;

import com.armadialogcreator.gui.main.editor.UICanvasConfiguration;
import com.armadialogcreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 @author Kayler
 @since 11/24/2016 */
public class ToggleViewportSnappingAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		UICanvasConfiguration conf = ArmaDialogCreator.getCanvasView().getConfiguration();
		conf.setViewportSnapEnabled(!conf.viewportSnapEnabled());
	}
}
