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
import javafx.application.Preloader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 Created by Kayler on 10/07/2016.
 */
public class ADCPreloader extends Preloader {

	private Stage preloaderStage;
	private ProgressIndicator progressIndicator;

	public ADCPreloader() {
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		if (info instanceof ProgressNotification) {
			handleProgressNotificationCustom((ProgressNotification) info);
		}
	}

	private void handleProgressNotificationCustom(ProgressNotification info) {
		if (info.getProgress() >= 1.0) {
			preloaderStage.close();
		}
		progressIndicator.setProgress(info.getProgress());
	}


	@Override
	public void start(Stage preloaderStage) throws Exception {
		this.preloaderStage = preloaderStage;
		progressIndicator = new ProgressIndicator(-1);
		progressIndicator.setMaxWidth(48);

		StackPane.setMargin(progressIndicator, new Insets(256, 0, 0, 0));
		StackPane stackpane = new StackPane(new ImageView(ImagePaths.PRELOAD_SCREEN), progressIndicator);
		Scene scene = new Scene(stackpane);
		preloaderStage.initStyle(StageStyle.UNDECORATED);
		preloaderStage.setScene(scene);
		preloaderStage.sizeToScene();
		preloaderStage.show();
	}
}
