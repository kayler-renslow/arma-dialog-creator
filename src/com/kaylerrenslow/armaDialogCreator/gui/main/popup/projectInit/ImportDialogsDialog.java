package com.kaylerrenslow.armaDialogCreator.gui.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

/**
 @author Kayler
 @since 05/05/2017 */
public class ImportDialogsDialog extends StageDialog<VBox> {
	private final ResourceBundle bundle = Lang.getBundle("ProjectInitWindowBundle");

	public ImportDialogsDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null, true, true, true);
		setTitle(bundle.getString("ImportDialogs.window_title"));
	}
}
