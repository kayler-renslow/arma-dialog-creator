package com.armadialogcreator.gui.main.actions.mainMenu;

import com.armadialogcreator.ADCStatic;
import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.core.stringtable.StringTable;
import com.armadialogcreator.data.DefaultStringTableXmlParser;
import com.armadialogcreator.data.xml.StringTableXmlWriter;
import com.armadialogcreator.gui.main.stringtable.StringTableEditorPopup;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.XmlParseException;
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
		StringTable table = null;
		if (table == null) {
			FileChooser chooser = new FileChooser();
			chooser.setTitle(Lang.getBundle("MainMenuBarBundle").getString("Action.edit_string_table-locate"));
			chooser.setInitialDirectory(Workspace.getWorkspace().getWorkspaceDirectory());
			chooser.getExtensionFilters().add(ADCStatic.STRING_TABLE_XML_EXTENSION_FILTER);
			File file = chooser.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
			if (file == null) {
				return;
			}
			try {
				DefaultStringTableXmlParser parser = new DefaultStringTableXmlParser(file);
				table = parser.createStringTableInstance();
				throw new RuntimeException();
//				Project.getCurrentProject().setStringTable(table);
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
