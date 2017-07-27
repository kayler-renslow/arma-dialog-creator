package com.kaylerrenslow.armaDialogCreator.gui.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.gui.main.popup.SimpleErrorDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 @author Kayler
 @since 11/23/2016 */
public class CouldNotLoadProjectDialog extends SimpleErrorDialog<Label> {
	public CouldNotLoadProjectDialog(@NotNull Exception e) {
		super(ArmaDialogCreator.getPrimaryStage(), null, e, new Label());
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("ProjectLoadError.could_not_load_project"));

		myBody.setText(bundle.getString("ProjectLoadError.could_not_load_project"));
	}
}

