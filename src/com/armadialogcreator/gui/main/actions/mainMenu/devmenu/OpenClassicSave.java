package com.armadialogcreator.gui.main.actions.mainMenu.devmenu;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.application.ADCFile;
import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.application.XmlConfigurableLoader;
import com.armadialogcreator.data.ClassicProjectSaveLoader;
import com.armadialogcreator.util.XmlParseException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import java.io.File;

/**
 @author kayler
 @since 4/16/19 */
public class OpenClassicSave implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
		if (file == null) {
			return;
		}
		try {
			Configurable conf = XmlConfigurableLoader.load(ADCFile.toADCFile(file));
			ClassicProjectSaveLoader loader = new ClassicProjectSaveLoader(conf);
			loader.load();
		} catch (XmlParseException e) {
			return;
		}

	}
}
