package com.armadialogcreator.gui.main.popup.projectInit;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.gui.main.popup.SimpleErrorDialog;
import com.armadialogcreator.lang.Lang;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 @author Kayler
 @since 11/23/2016 */
public class CouldNotLoadProjectDialog extends SimpleErrorDialog<Label> {
	public CouldNotLoadProjectDialog(@NotNull Exception e) {
		this(e, null);
	}

	public CouldNotLoadProjectDialog(@NotNull Exception e, @Nullable String additionalInfo) {
		super(ArmaDialogCreator.getPrimaryStage(), null, e, new Label(additionalInfo == null ? "" : additionalInfo));
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("ProjectLoadError.could_not_load_project"));

		myBody.setText(bundle.getString("ProjectLoadError.could_not_load_project"));

	}
}

