package com.kaylerrenslow.armaDialogCreator.gui.fx.popup;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 A simple response dialog

 @author Kayler
 @since 11/19/2016 */
public class SimpleResponseDialog extends StageDialog<HBox> {

	public SimpleResponseDialog(Stage primaryStage, String title, String body, boolean canCancel, boolean canOk, boolean hasHelp) {
		super(primaryStage, new HBox(), title, canCancel, canOk, hasHelp);
		final Label lblBody = new Label(body);
		lblBody.setWrapText(true);
		myRootElement.getChildren().add(lblBody);

	}

	@NotNull
	public GenericResponseFooter getFooter() {
		return footer;
	}
}
