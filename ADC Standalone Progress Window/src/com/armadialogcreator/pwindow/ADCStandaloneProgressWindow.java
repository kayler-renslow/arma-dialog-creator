package com.armadialogcreator.pwindow;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 @author kayler
 @since April 10, 2017
 */
public class ADCStandaloneProgressWindow {
	private final Stage stage;

	private final ProgressBar progressBar = new ProgressBar(-1);
	private final Label lblStatus = new Label();
	private final Label lblError = new Label();
	private final VBox root;
	private boolean exitButtonAdded = false;

	public ADCStandaloneProgressWindow(@NotNull Stage stage) {
		this.stage = stage;

		stage.getIcons().add(new Image("/com/armadialogcreator/pwindow/app.png"));

		root = new VBox(5);
		stage.setScene(new Scene(new StackPane(root)));
		stage.setResizable(false);

		root.setPrefSize(720, 360);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(10));

		root.getChildren().add(new ImageView("/com/armadialogcreator/pwindow/adc_title.png"));

		progressBar.setMaxWidth(Double.MAX_VALUE);
		root.getChildren().add(progressBar);
		root.getChildren().add(lblStatus);
		lblError.setTextFill(Color.RED);
		root.getChildren().add(lblError);
	}

	@NotNull
	public Stage getStage() {
		return stage;
	}

	@NotNull
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	@NotNull
	public Label getLblError() {
		return lblError;
	}

	@NotNull
	public Label getLblStatus() {
		return lblStatus;
	}

	public void addExitButton(@NotNull String exitBtnText) {
		if (exitButtonAdded) {
			return;
		}
		exitButtonAdded = true;
		final Button btnExit = new Button(exitBtnText);
		btnExit.setPrefWidth(120d);
		btnExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		root.getChildren().add(btnExit);
	}
}
