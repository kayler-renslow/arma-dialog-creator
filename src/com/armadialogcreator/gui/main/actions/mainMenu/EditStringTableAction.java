package com.armadialogcreator.gui.main.actions.mainMenu;

import com.armadialogcreator.arma.stringtable.StringTable;
import com.armadialogcreator.data.Project;
import com.armadialogcreator.data.Workspace;
import com.armadialogcreator.data.xml.DefaultStringTableXmlParser;
import com.armadialogcreator.data.xml.StringTableXmlWriter;
import com.armadialogcreator.data.xml.XmlParseException;
import com.armadialogcreator.gui.main.stringtable.StringTableEditorPopup;
import com.armadialogcreator.main.ADCStatic;
import com.armadialogcreator.main.ArmaDialogCreator;
import com.armadialogcreator.main.ExceptionHandler;
import com.armadialogcreator.main.Lang;
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
