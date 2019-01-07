package com.armadialogcreator.gui.main.actions.mainMenu.file;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.data.export.ProjectExporter;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.main.popup.export.EditExportConfigurationDialog;
import com.armadialogcreator.gui.notification.BoxNotification;
import com.armadialogcreator.gui.notification.Notifications;
import com.armadialogcreator.lang.Lang;
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
import java.util.ResourceBundle;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileExportAction implements EventHandler<ActionEvent> {

	private final ResourceBundle bundle = Lang.ApplicationBundle();

	@Override
	public void handle(ActionEvent event) {
		EditExportConfigurationDialog dialog = new EditExportConfigurationDialog(Project.getCurrentProject().getExportConfiguration());
		dialog.show();
		if (dialog.getConfiguration() == null || dialog.wasCancelled()) {
			return;
		}
		try {
			ProjectExporter.exportProject(dialog.getConfiguration());
			File exportDir = dialog.getConfiguration().getExportDirectory();

			Notifications.showNotification(
					new BoxNotification(
							bundle.getString("Popups.ExportAftermathPopup.dialog_title_success"),
							String.format(bundle.getString("Popups.ExportAftermathPopup.export_complete_f"), exportDir.getPath()),
							8 * 1000
					)
			);
		} catch (IOException e) {
			ExportErrorAftermathDialog exportErrorAftermathDialog = new ExportErrorAftermathDialog(e);
			exportErrorAftermathDialog.show();
		}


	}

	private class ExportErrorAftermathDialog extends StageDialog<VBox> {

		public ExportErrorAftermathDialog(Throwable error) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), "", false, true, false);
			myStage.setMinWidth(300d);
			myStage.setMinHeight(100d);

			setTitle(bundle.getString("Popups.ExportAftermathPopup.dialog_title_failed"));
			myRootElement.getChildren().add(new Label(bundle.getString("Popups.ExportAftermathPopup.export_failed")));
			myRootElement.getChildren().add(new Label(error.getMessage()));
			final ToggleButton toggleButton = new ToggleButton(bundle.getString("Popups.ExportAftermathPopup.show_error_message"));
			myRootElement.getChildren().add(toggleButton);

			toggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
				final TextArea taErrorMessage = ExceptionHandler.getExceptionTextArea(error);
				boolean firstExpansion = true;

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
					if (selected) {
						toggleButton.setText(bundle.getString("Popups.ExportAftermathPopup.hide_error_message"));
						myRootElement.getChildren().add(taErrorMessage);
					} else {
						toggleButton.setText(bundle.getString("Popups.ExportAftermathPopup.show_error_message"));
						myRootElement.getChildren().remove(taErrorMessage);
					}
					if (firstExpansion) {
						firstExpansion = false;
						ExportErrorAftermathDialog.this.sizeToScene();
					}
				}
			});

		}
	}

}
