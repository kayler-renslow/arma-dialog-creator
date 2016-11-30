package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 @author Kayler
 @since 11/23/2016 */
public class CouldNotLoadProjectDialog extends StageDialog<VBox> {
	public CouldNotLoadProjectDialog(Exception e) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("ProjectInitWindow.could_not_load_project"), false, true, false);
		final TextArea taError = new TextArea(e.getMessage());
		taError.setEditable(false);
		myStage.setResizable(false);
		myRootElement.getChildren().addAll(
				new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.could_not_load_project")),
				new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.reason")),
				taError
		);
	}
}

