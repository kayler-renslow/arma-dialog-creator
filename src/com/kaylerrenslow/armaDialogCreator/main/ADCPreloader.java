/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.gui.img.Images;
import javafx.application.Preloader;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Map;
import java.util.jar.Attributes;

/**
 Created by Kayler on 10/07/2016.
 */
public class ADCPreloader extends Preloader {

	private Stage preloaderStage;
	private ProgressIndicator progressIndicator;
	private final String[] progressText = {"Bamboozling", "Loading the Dinghy", "Doodling", "Counting to Infinity", "Finished Counting to Infinity", "Boondoggling"};
	private Label lblProgressText = new Label(progressText[0]);

	public ADCPreloader() {
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		if (info instanceof ProgressNotification) {
			handleProgressNotificationCustom((ProgressNotification) info);
		}
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
			closePreloader();
		}
	}

	private void handleProgressNotificationCustom(ProgressNotification info) {
		progressIndicator.setProgress(info.getProgress());
		int progressTextInd = (int) (info.getProgress() * this.progressText.length);
		lblProgressText.setText(progressText[progressTextInd]);
	}

	private void closePreloader() {
		preloaderStage.close();
		preloaderStage = null;
	}


	@Override
	public void start(Stage preloaderStage) throws Exception {
		this.preloaderStage = preloaderStage;
		preloaderStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});

		preloaderStage.setTitle(Lang.Application.APPLICATION_TITLE);
		preloaderStage.getIcons().add(Images.IMAGE_ADC_ICON);
		progressIndicator = new ProgressIndicator(-1);
		progressIndicator.setMaxWidth(48d);
		progressIndicator.setMaxHeight(progressIndicator.getMaxWidth());

		VBox vBox = new VBox(5, progressIndicator, lblProgressText);
		vBox.setAlignment(Pos.CENTER);
		VBox.setVgrow(progressIndicator, Priority.ALWAYS);

		Map<String, Attributes> entries = ArmaDialogCreator.getManifest().getEntries();
		final Label lblBuild = new Label("Build: " + ArmaDialogCreator.getManifest().getMainAttributes().getValue("Build-Number"));
		final BorderPane borderPane = new BorderPane(vBox, null, null, lblBuild, null);
		borderPane.setPadding(new Insets(5));

		StackPane.setMargin(borderPane, new Insets(248, 0, 0, 0));
		StackPane stackpane = new StackPane(new ImageView(ImagePaths.PRELOAD_SCREEN), borderPane);
		Scene scene = new Scene(stackpane);
		preloaderStage.initStyle(StageStyle.UNDECORATED);
		preloaderStage.setScene(scene);
		preloaderStage.sizeToScene();
		preloaderStage.show();
	}

}
