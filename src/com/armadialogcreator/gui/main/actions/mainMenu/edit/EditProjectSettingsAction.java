package com.armadialogcreator.gui.main.actions.mainMenu.edit;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.data.Project;
import com.armadialogcreator.gui.main.popup.EditProjectSettingsDialog;
import com.armadialogcreator.gui.main.popup.SimpleErrorDialog;
import com.armadialogcreator.lang.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 @author Kayler
 @since 7/30/2017 */
public class EditProjectSettingsAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		Project project = Project.getCurrentProject();
		EditProjectSettingsDialog dialog = new EditProjectSettingsDialog(project);
		dialog.show();
		if (dialog.wasCancelled()) {
			return;
		}
		project.setProjectName(dialog.getProjectName());
		project.setProjectDescription(dialog.getProjectDescription());
		if (dialog.moveProjectDirectory()) {
			try {
				project.setProjectDirectoryName(project.getProjectName());
			} catch (IOException ignore) {
				ResourceBundle bundle = Lang.ApplicationBundle();
				new SimpleErrorDialog<>(
						ArmaDialogCreator.getPrimaryStage(),
						bundle.getString("Popups.EditProjectSettings.couldnt_move_project_dir_short"),
						null,
						new Label(bundle.getString("Popups.EditProjectSettings.couldnt_move_project_dir"))
				).show();
			}
		}

	}
}
