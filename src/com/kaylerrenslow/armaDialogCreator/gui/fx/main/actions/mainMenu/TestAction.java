package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.DefaultStringTableXmlParser;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.stringtable.StringTableEditorPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;

/**
 @author Kayler
 Implementation varies. Used for debugging/testing specific features
 Created on 09/14/2016. */
public class TestAction implements EventHandler<ActionEvent> {
	private static int i = 0;

	@Override
	public void handle(ActionEvent event) {
		try {
			StringTable table = new DefaultStringTableXmlParser(new File("D:\\Archive\\Intellij Files\\Arma Tools\\Arma Dialog " +
					"Creator\\tests\\com\\kaylerrenslow\\armaDialogCreator\\data\\io\\xml\\stringtable.xml")).createStringTableInstance();
			new StringTableEditorPopup(table).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
