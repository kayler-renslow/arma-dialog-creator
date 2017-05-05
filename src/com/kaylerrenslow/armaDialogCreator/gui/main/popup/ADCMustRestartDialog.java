package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

/**
 Created by Kayler on 10/16/2016.
 */
public class ADCMustRestartDialog extends StageDialog<VBox> {

	public ADCMustRestartDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, false);
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("Popups.generic_popup_title"));
		footer.getBtnOk().setText(bundle.getString("Popups.MustRestart.restart"));
		myRootElement.getChildren().add(new Label(bundle.getString("Popups.MustRestart.must_restart_for_changes")));
	}

	/** Shows a new dialog asking user if they want to restart. Will return true if the user wants to restart, false if they don't */
	public static boolean getResponse() {
		ADCMustRestartDialog dialog = new ADCMustRestartDialog();
		dialog.show();
		return !dialog.wasCancelled();
	}
}
