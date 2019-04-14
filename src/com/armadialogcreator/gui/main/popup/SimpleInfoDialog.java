package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.gui.StageDialog;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

/**
 A simple info dialog that shows a block of text

 @author Kayler
 @since 06/23/2017 */
public class SimpleInfoDialog<B extends Node> extends StageDialog<VBox> {
	protected final B myBody;

	/**
	 @param primaryStage the primary stage
	 @param title title of dialog
	 @param body body to use, or null to not have a body
	 */
	public SimpleInfoDialog(@Nullable Stage primaryStage, @Nullable String title, @Nullable B body) {
		super(primaryStage, new VBox(10), title, false, true, false);
		this.myBody = body;

		myStage.setMinWidth(300d);
		myStage.setMinHeight(200d);
		myStage.setMaxWidth(720);

		if (body != null) {
			StackPane stackPane = new StackPane(body);
			stackPane.setAlignment(Pos.TOP_LEFT);
			stackPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			myRootElement.getChildren().add(stackPane);
			VBox.setVgrow(stackPane, Priority.ALWAYS);
			body.autosize();
		}

	}

	@Override
	public void show() {
		super.show();
		sizeToScene();
	}

	public static class AsTextArea extends SimpleInfoDialog<javafx.scene.control.TextArea> {

		/**
		 @param primaryStage the primary stage
		 @param title title of dialog
		 @param text text to use
		 */
		public AsTextArea(@Nullable Stage primaryStage, @Nullable String title, @Nullable String text) {
			super(primaryStage, title, new TextArea(text));
		}
	}
}
