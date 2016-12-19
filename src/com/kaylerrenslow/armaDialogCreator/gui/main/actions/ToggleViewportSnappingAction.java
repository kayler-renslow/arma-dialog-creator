
package com.kaylerrenslow.armaDialogCreator.gui.main.actions;

import com.kaylerrenslow.armaDialogCreator.gui.main.editor.UICanvasConfiguration;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
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
