/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.launcher;

import com.kaylerrenslow.armaDialogCreator.launcher.github.ReleaseAsset;
import com.kaylerrenslow.armaDialogCreator.launcher.github.ReleaseInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 Created by Kayler on 10/20/2016.
 */
public class ADCLauncher extends Application {
	private final File adcJarSaveLocation = new File(System.getenv("APPDATA") + "/Arma Dialog Creator/adc.jar");
	//	private final String JSON_RELEASE_INFO = "https://api.github.com/repos/kayler-renslow/arma-dialog-creator/releases/latest";
	private final String JSON_RELEASE_INFO = "https://api.github.com/repos/kayler-renslow/arma-intellij-plugin/releases/latest";

	private final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.launcher.LauncherBundle");


	private Stage primaryStage;

	private ReleaseInfo latestRelease;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.getIcons().add(new Image("/com/kaylerrenslow/armaDialogCreator/launcher/app.png"));
		primaryStage.setTitle(bundle.getString("Launcher.title"));
		if (!adcUpToDate()) {
			downloadAndRunAdc();
		} else {
			launchADC();
		}
	}

	private void downloadAndRunAdc() {
		final VBox root = new VBox(5);
		primaryStage.setScene(new Scene(new StackPane(root)));
		primaryStage.setResizable(false);

		//todo check if disk can fit ADC

		root.setPrefSize(720, 360);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(10));

		root.getChildren().add(new ImageView("/com/kaylerrenslow/armaDialogCreator/launcher/adc_title.png"));

		final ProgressBar progressBar = new ProgressBar(-1);
		progressBar.setMaxWidth(Double.MAX_VALUE);

		root.getChildren().add(progressBar);

		final Label lblDownloading = new Label(String.format(bundle.getString("Launcher.downloading_ADC"), latestRelease.getTagName()));
		root.getChildren().add(lblDownloading);

		ReleaseAsset adcJarAsset = latestRelease.getAssestByName("adc.jar");
		if (adcJarAsset == null) {
			//todo
			throw new RuntimeException("");
		}
		final Task<Boolean> taskDownload = new DownloadADCTask(adcJarSaveLocation, adcJarAsset.getDownloadUrl());
		taskDownload.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
				newValue.printStackTrace();
			}
		});
		taskDownload.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				lblDownloading.setText(bundle.getString("Launcher.download_complete"));

				Task<Object> launchingTask = new Task<Object>() {

					@Override
					protected Object call() throws Exception {
						try {
							Thread.sleep(1000);
							updateMessage(bundle.getString("Launcher.launching"));
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
						launchADC();
						return new Object();
					}
				};
				launchingTask.messageProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						lblDownloading.setText(newValue);
					}
				});
				new Thread(launchingTask).start();
			}
		});
		progressBar.progressProperty().bind(taskDownload.progressProperty());
		new Thread(taskDownload).start();

		primaryStage.show();
	}

	private boolean adcUpToDate() {
		boolean forceUpdate = false;
		String specVersion = null;
		if (!adcJarSaveLocation.exists()) {
			forceUpdate = true;
		} else {
			Manifest m;
			try {
				m = new JarFile(adcJarSaveLocation).getManifest();
				Attributes manifestAttributes = m.getMainAttributes();
				specVersion = manifestAttributes.getValue("Specification-Version");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (specVersion == null) {
				forceUpdate = true;

			}
		}

		ReleaseInfo info;
		try {
			info = new ReleaseInfo(JSON_RELEASE_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			//todo can't get update
			return false;
		}

		latestRelease = info;

		//todo ask user if wants update
		return !forceUpdate && info.getTagName().equals(specVersion);
	}

	private void launchADC() {
		if (!adcJarSaveLocation.exists()) {
			//todo
		}

		Platform.exit();
	}

	private static class DownloadADCTask extends Task<Boolean> {
		private File jarSaveLocation;
		private String downloadUrl;

		public DownloadADCTask(File jarSaveLocation, String downloadUrl) {
			this.jarSaveLocation = jarSaveLocation;
			this.downloadUrl = downloadUrl;
		}

		@Override
		protected Boolean call() throws Exception {
			if (true) {
				for (int i = 0; i <= 100; i++) {
					updateProgress(i, 100);
					Thread.sleep(20);
				}
				return true;
			}
			URL url = new URL(downloadUrl);

			BufferedInputStream in = null;
			FileOutputStream fout = null;
			URLConnection urlConnection = null;
			long workDone = 0;

			try {
				urlConnection = url.openConnection();
				in = new BufferedInputStream(urlConnection.getInputStream());
				fout = new FileOutputStream(jarSaveLocation);

				long downloadSize = urlConnection.getContentLengthLong();

				final byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1) {
					fout.write(data, 0, count);
					workDone += count;
					updateProgress(workDone, downloadSize);
				}
			} finally {
				if (in != null) {
					in.close();
				}
				if (urlConnection != null) {
					urlConnection.getInputStream().close();
				}
				if (fout != null) {
					fout.close();
				}
			}
			return true;
		}
	}
}
