package com.armadialogcreator.gui.main.actions.mainMenu.edit;

import com.armadialogcreator.data.Project;
import com.armadialogcreator.gui.main.popup.export.EditExportConfigurationDialog;
import com.armadialogcreator.lang.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 @author Kayler
 @since 09/19/2016
 */
public class EditExportConfigAction implements EventHandler<ActionEvent> {
	public EditExportConfigAction() {
	}

	@Override
	public void handle(ActionEvent event) {
		EditExportConfigurationDialog dialog =
				new EditExportConfigurationDialog(Project.getCurrentProject().getExportConfiguration()) {
					@Override
					public void show() {
						btnOk.setText(Lang.ApplicationBundle().getString("Popups.btn_ok"));
						super.show();
					}
				};
		dialog.show();
		if (dialog.wasCancelled() || dialog.getConfiguration() == null) {
			return;
		}
		Project.getCurrentProject().setExportConfiguration(dialog.getConfiguration());

	}
}
