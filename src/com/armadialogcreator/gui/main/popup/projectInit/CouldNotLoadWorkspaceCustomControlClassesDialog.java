package com.armadialogcreator.gui.main.popup.projectInit;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.gui.main.popup.SimpleErrorDialog;
import com.armadialogcreator.lang.Lang;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 @author Kayler
 @since 11/06/2017 */
public class CouldNotLoadWorkspaceCustomControlClassesDialog extends SimpleErrorDialog<Label> {
	public CouldNotLoadWorkspaceCustomControlClassesDialog(@NotNull Exception e) {
		super(ArmaDialogCreator.getPrimaryStage(), null, e, new Label());
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("ProjectLoadError.could_not_load_project"));

		myBody.setText(bundle.getString("WorkspaceLoadError.could_not_load_workspace_custom_control_classes"));
	}
}

