package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.Workspace;
import com.kaylerrenslow.armaDialogCreator.data.xml.DefaultStringTableXmlParser;
import com.kaylerrenslow.armaDialogCreator.data.xml.StringTableXmlWriter;
import com.kaylerrenslow.armaDialogCreator.data.xml.XmlParseException;
import com.kaylerrenslow.armaDialogCreator.gui.main.stringtable.StringTableEditorPopup;
import com.kaylerrenslow.armaDialogCreator.main.ADCStatic;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 @author Kayler
 @since 12/30/2016 */
public class EditStringTableAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		StringTable table = Project.getCurrentProject().getStringTable();
		if (table == null) {
			FileChooser chooser = new FileChooser();
			chooser.setTitle(Lang.ApplicationBundle().getString("StringTable.locate_stringtable"));
			chooser.setInitialDirectory(Workspace.getWorkspace().getWorkspaceDirectory());
			chooser.getExtensionFilters().add(ADCStatic.STRING_TABLE_XML_EXTENSION_FILTER);
			File file = chooser.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
			if (file == null) {
				return;
			}
			try {
				DefaultStringTableXmlParser parser = new DefaultStringTableXmlParser(file);
				table = parser.createStringTableInstance();
				Project.getCurrentProject().setStringTable(table);
			} catch (XmlParseException | IOException e) {
				ExceptionHandler.error(e);
				return;
			}
		}
		try {
			StringTableEditorPopup popup = new StringTableEditorPopup(table, new StringTableXmlWriter(), new DefaultStringTableXmlParser(table.getFile()));
			popup.show();
		} catch (XmlParseException e) {
			ExceptionHandler.error(e);
		}
	}
}
