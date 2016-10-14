/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExporter;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.export.EditExportConfigurationDialog;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileExportAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		EditExportConfigurationDialog dialog = new EditExportConfigurationDialog(ApplicationDataManager.getInstance().getCurrentProject());
		dialog.show();
		if (dialog.getConfiguration() == null) {
			return;
		}
		ExportAftermathDialog exportAftermathDialog;
		try {
			ProjectExporter.export(dialog.getConfiguration());
			exportAftermathDialog = new ExportAftermathDialog(dialog.getConfiguration().getExportLocation(), null);
		} catch (IOException e) {
			exportAftermathDialog = new ExportAftermathDialog(null, e);
		}
		exportAftermathDialog.show();

	}

	private static class ExportAftermathDialog extends StageDialog<VBox> {

		public ExportAftermathDialog(File exportDir, Throwable error) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), "", false, true, false);
			myStage.setMinWidth(300d);
			myStage.setMinHeight(100d);
			if (error == null) {
				setTitle(Lang.Popups.ExportProject.ExportAftermathPopup.DIALOG_TITLE_SUCCESS);
				myRootElement.getChildren().add(new Label(String.format(Lang.Popups.ExportProject.ExportAftermathPopup.EXPORT_COMPLETE_F, exportDir.getPath())));
			} else {
				setTitle(Lang.Popups.ExportProject.ExportAftermathPopup.DIALOG_TITLE_FAILED);
				myRootElement.getChildren().add(new Label(Lang.Popups.ExportProject.ExportAftermathPopup.EXPORT_FAILED));
				final ToggleButton toggleButton = new ToggleButton(Lang.Popups.ExportProject.ExportAftermathPopup.SHOW_ERROR_MESSAGE);
				myRootElement.getChildren().add(toggleButton);

				toggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
					final TextArea taErrorMessage = ExceptionHandler.getExceptionTextArea(error);
					boolean firstExpansion = true;

					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
						if (selected) {
							toggleButton.setText(Lang.Popups.ExportProject.ExportAftermathPopup.HIDE_ERROR_MESSAGE);
							myRootElement.getChildren().add(taErrorMessage);
						} else {
							toggleButton.setText(Lang.Popups.ExportProject.ExportAftermathPopup.SHOW_ERROR_MESSAGE);
							myRootElement.getChildren().remove(taErrorMessage);
						}
						if (firstExpansion) {
							firstExpansion = false;
							ExportAftermathDialog.this.sizeToScene();
						}
					}
				});
			}

		}
	}

}
