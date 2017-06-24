package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 A simple error dialog that has the option to show/hide an Exception/Throwable

 @author Kayler
 @since 06/23/2017 */
public class SimpleErrorDialog<B extends Node> extends StageDialog<VBox> {
	protected final B myBody;

	public SimpleErrorDialog(@Nullable Stage primaryStage, @Nullable String title, @NotNull Throwable error,
							 @Nullable B body) {
		super(primaryStage, new VBox(10), title, false, true, false);
		this.myBody = body;

		ResourceBundle bundle = Lang.ApplicationBundle();

		final String showStackTraceLblStr = bundle.getString("Popups.SimpleErrorDialog.show_detail");
		final String hideStackTraceLblStr = bundle.getString("Popups.SimpleErrorDialog.hide_detail");

		myStage.setMinWidth(300d);
		myStage.setMinHeight(100d);
		myStage.setMaxWidth(720);

		if (body != null) {
			myRootElement.getChildren().add(body);
		}

		myRootElement.getChildren().add(new Label(error.getMessage()));
		ToggleButton toggleButton = new ToggleButton(showStackTraceLblStr);
		myRootElement.getChildren().add(toggleButton);

		toggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			final TextArea taErrorMessage = ExceptionHandler.getExceptionTextArea(error);
			boolean firstExpansion = true;

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				if (selected) {
					toggleButton.setText(hideStackTraceLblStr);
					myRootElement.getChildren().add(taErrorMessage);
				} else {
					toggleButton.setText(showStackTraceLblStr);
					myRootElement.getChildren().remove(taErrorMessage);
				}
				if (firstExpansion) {
					firstExpansion = false;
					SimpleErrorDialog.this.sizeToScene();
				}
			}
		});


	}
}
