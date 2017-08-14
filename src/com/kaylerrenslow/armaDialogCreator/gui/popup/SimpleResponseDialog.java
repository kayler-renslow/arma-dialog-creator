package com.kaylerrenslow.armaDialogCreator.gui.popup;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 A simple response dialog

 @author Kayler
 @since 11/19/2016 */
public class SimpleResponseDialog extends StageDialog<StackPane> {

	public SimpleResponseDialog(Stage primaryStage, String title, String body, boolean canCancel, boolean canOk, boolean hasHelp) {
		super(primaryStage, new StackPane(), title, canCancel, canOk, hasHelp);
		myRootElement.setAlignment(Pos.TOP_LEFT);
		Label lblBody = new Label(body);
		lblBody.setWrapText(true);
		myRootElement.getChildren().add(lblBody);
	}

	@NotNull
	public GenericResponseFooter getFooter() {
		return footer;
	}
}
