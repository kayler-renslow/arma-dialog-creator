/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExporter;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

/**
 @author Kayler
 Implementation varies. Used for debugging/testing specific features
 Created on 09/14/2016. */
public class TestAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		File f = new DirectoryChooser().showDialog(ArmaDialogCreator.getPrimaryStage());
		if (f == null) {
			return;
		}
		ProjectExporter exporter = new ProjectExporter(new ProjectExportConfiguration("testDialog", f, ApplicationDataManager.getInstance().getApplicationData().getCurrentProject(), true));
		try {
			exporter.export("test.h", null);
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
	}
}
