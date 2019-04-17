package com.armadialogcreator.gui.main.actions.mainMenu.devmenu;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.application.ADCFile;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.XmlConfigurableLoader;
import com.armadialogcreator.data.ClassicWorkspaceSaveLoader;
import com.armadialogcreator.util.XmlParseException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import java.io.File;

/**
 @author K
 @since 4/17/19 */
public class OpenClassicWorkspaceSave implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
		if (file == null) {
			return;
		}
		try {
			Configurable conf = XmlConfigurableLoader.load(ADCFile.toADCFile(file));
			ClassicWorkspaceSaveLoader loader = new ClassicWorkspaceSaveLoader(conf);
			loader.load();
		} catch (XmlParseException e) {
			ExceptionHandler.error(e);
		}

	}
}
