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

import com.kaylerrenslow.armaDialogCreator.launcher.gui.ADCLauncherWindow;
import com.kaylerrenslow.armaDialogCreator.launcher.tasks.AdcVersionCheckTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 Created by Kayler on 10/20/2016.
 */
public class ADCLauncher extends Application {
	//	private static final String ADC_JAR = "Arma.Intellij.Plugin_v1.0.5_2.jar";
	//	private static final String JSON_RELEASE_INFO = "https://api.github.com/repos/kayler-renslow/arma-intellij-plugin/releases/latest";
	private static final String ADC_JAR = "adc.jar";
	private static final File ADC_JAR_SAVE_LOCATION = new File(System.getenv("APPDATA") + "/Arma Dialog Creator/" + ADC_JAR);
	private static final String JSON_RELEASE_INFO = "https://api.github.com/repos/kayler-renslow/arma-dialog-creator/releases/latest";

	public static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.launcher.LauncherBundle");

	public static void main(String[] args) {
		launch(args);
	}

	private ADCLauncherWindow window;
	private final ChangeListener<? super String> taskMessagePropertyListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			window.getLblStatus().setText(newValue);
		}
	};
	private final ChangeListener<? super Throwable> taskExceptionPropertyListener = new ChangeListener<Throwable>() {
		@Override
		public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
			window.getLblError().setText("Error (" + newValue.getClass().getSimpleName() + "): " + newValue.getMessage());
			window.addExitButton();
			newValue.printStackTrace();
		}
	};


	@Override
	public void start(Stage primaryStage) throws Exception {
		this.window = new ADCLauncherWindow(primaryStage);
		Thread.currentThread().setName("Arma Dialog Creator Launcher - JavaFX Thread");
		primaryStage.show();
		runVersionTask();
	}

	private void runVersionTask() {
		loadTask(new AdcVersionCheckTask(ADC_JAR_SAVE_LOCATION, JSON_RELEASE_INFO), "", new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				launchADC();
			}
		});
	}


	private void launchADC() {
		if (!ADC_JAR_SAVE_LOCATION.exists()) {
			window.getLblError().setText(bundle.getString("Launcher.Fail.adc_didnt_save"));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Runtime.getRuntime().exec("java -jar " + ADC_JAR, null, ADC_JAR_SAVE_LOCATION.getParentFile());
		} catch (IOException e) {
			e.printStackTrace();
			window.getLblError().setText("ERROR: " + e.getMessage());
		}
		Platform.exit();
	}

	private void loadTask(Task<?> task, String initStatusText, EventHandler<WorkerStateEvent> succeedEvent) {
		window.getProgressBar().setProgress(-1);
		window.getLblStatus().setText(initStatusText);
		task.exceptionProperty().addListener(taskExceptionPropertyListener);
		task.messageProperty().addListener(taskMessagePropertyListener);
		task.setOnSucceeded(succeedEvent);
		window.getProgressBar().progressProperty().bind(task.progressProperty());
		new Thread(task).start();
	}

}
