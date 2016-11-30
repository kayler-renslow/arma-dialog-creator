package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.help;

import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 Created by Kayler on 08/22/2016.
 */
public class WikiUrlAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		BrowserUtil.browse(HelpUrls.WIKI_URL);
	}
}
