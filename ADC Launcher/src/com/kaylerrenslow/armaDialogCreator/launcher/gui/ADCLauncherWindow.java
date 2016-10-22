/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.launcher.gui;

import com.kaylerrenslow.armaDialogCreator.launcher.ADCLauncher;
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
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 10/22/2016.
 */
public class ADCLauncherWindow {
	private final Stage primaryStage;

	private final ProgressBar progressBar = new ProgressBar(-1);
	private final Label lblStatus = new Label();
	private final VBox root;
	private boolean exitButtonAdded = false;

	public ADCLauncherWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;

		primaryStage.getIcons().add(new Image("/com/kaylerrenslow/armaDialogCreator/launcher/app.png"));
		primaryStage.setTitle(ADCLauncher.bundle.getString("Launcher.title"));

		root = new VBox(5);
		primaryStage.setScene(new Scene(new StackPane(root)));
		primaryStage.setResizable(false);

		root.setPrefSize(720, 360);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(10));

		root.getChildren().add(new ImageView("/com/kaylerrenslow/armaDialogCreator/launcher/adc_title.png"));

		progressBar.setMaxWidth(Double.MAX_VALUE);
		root.getChildren().add(progressBar);
		root.getChildren().add(lblStatus);
	}

	@NotNull
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	@NotNull
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	@NotNull
	public Label getLblStatus() {
		return lblStatus;
	}

	public void addExitButton() {
		if (exitButtonAdded) {
			return;
		}
		exitButtonAdded = true;
		final Button btnExit = new Button(ADCLauncher.bundle.getString("Launcher.exit"));
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
