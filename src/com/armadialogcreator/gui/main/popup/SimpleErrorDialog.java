package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.gui.popup.StageDialog;
import com.armadialogcreator.main.ExceptionHandler;
import com.armadialogcreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 A simple error dialog that has the option to show/hide an Exception/Throwable

 @author Kayler
 @since 06/23/2017 */
public class SimpleErrorDialog<B extends Node> extends StageDialog<VBox> {
	protected final B myBody;

	/**
	 @param primaryStage the primary stage
	 @param title title of dialog
	 @param error use null if you wish to not show a strack trace to the user
	 @param body body to use, or null to not have a body
	 */
	public SimpleErrorDialog(@Nullable Stage primaryStage, @Nullable String title, @Nullable Throwable error,
							 @Nullable B body) {
		super(primaryStage, new VBox(10), title, false, true, false);
		this.myBody = body;

		ResourceBundle bundle = Lang.ApplicationBundle();

		final String showStackTraceLblStr = bundle.getString("Popups.SimpleErrorDialog.show_detail");
		final String hideStackTraceLblStr = bundle.getString("Popups.SimpleErrorDialog.hide_detail");

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

		if (error != null) {
			Label lblErrMsg = new Label(error.getMessage());
			lblErrMsg.setWrapText(true);
			myRootElement.getChildren().add(lblErrMsg);
			ToggleButton toggleButton = new ToggleButton(showStackTraceLblStr);
			myRootElement.getChildren().add(toggleButton);

			toggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
				final TextArea taErrorMessage = ExceptionHandler.getExceptionTextArea(error);

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
					if (selected) {
						toggleButton.setText(hideStackTraceLblStr);
						myRootElement.getChildren().add(taErrorMessage);
					} else {
						toggleButton.setText(showStackTraceLblStr);
						myRootElement.getChildren().remove(taErrorMessage);
					}
					SimpleErrorDialog.this.sizeToScene();
				}
			});
		}
	}

	@Override
	public void show() {
		super.show();
		sizeToScene();
	}
}
