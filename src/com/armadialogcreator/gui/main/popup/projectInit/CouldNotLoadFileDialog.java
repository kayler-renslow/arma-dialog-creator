package com.armadialogcreator.gui.main.popup.projectInit;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.gui.main.popup.SimpleErrorDialog;
import com.armadialogcreator.lang.Lang;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ResourceBundle;

/**
 @author Kayler
 @since 11/23/2016 */
public class CouldNotLoadFileDialog extends SimpleErrorDialog<Label> {
	public CouldNotLoadFileDialog(@NotNull Exception e, @NotNull File f) {
		this(e, f, null);
	}

	public CouldNotLoadFileDialog(@NotNull Exception e, @NotNull File f, @Nullable String additionalInfo) {
		super(ArmaDialogCreator.getPrimaryStage(), null, e, new Label(additionalInfo == null ? "" : additionalInfo));
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(String.format(bundle.getString("FileLoadError.could_not_load_file_f"), f.getName()));

		myBody.setText(String.format(bundle.getString("FileLoadError.could_not_load_file_f"), f.getAbsolutePath()));

	}
}

