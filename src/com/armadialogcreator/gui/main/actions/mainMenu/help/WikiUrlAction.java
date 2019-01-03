package com.armadialogcreator.gui.main.actions.mainMenu.help;

import com.armadialogcreator.HelpUrls;
import com.armadialogcreator.gui.main.BrowserUtil;
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
