/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */


package com.kaylerrenslow.armaDialogCreator.updater;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.updater.gui.ADCUpdaterWindow;
import com.kaylerrenslow.armaDialogCreator.updater.tasks.AdcVersionCheckTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ResourceBundle;

/**
 Created by Kayler on 10/20/2016.
 */
public class ADCUpdater extends Application {
	private static final String ADC_JAR = "adc.jar";
	private static final File ADC_JAR_SAVE_LOCATION = new File("./" + ADC_JAR);
	private static final File ADC_DOWNLOAD_JAR_SAVE_LOCATION = new File(".");
	private static final String JSON_RELEASE_INFO = "https://api.github.com/repos/kayler-renslow/arma-dialog-creator/releases/latest";

	public static final ResourceBundle bundle = Lang.getBundle("UpdaterBundle");

	/** Launches the updater as a new Application (DO NOT USE when an application is already running. Just create a new instance!) */
	public static void main(String[] args) {
		System.err.println("change this so it supports the installer instead");
		//launch(args);
	}

	private ADCUpdaterWindow window;
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
			window.addExitButton(ADCUpdater.bundle.getString("Updater.exit"));
			newValue.printStackTrace();
		}
	};

	@NotNull
	public ADCUpdaterWindow getWindow() {
		return window;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.window = new ADCUpdaterWindow(primaryStage);

		Thread.currentThread().setName("Arma Dialog Creator Updater - JavaFX Thread");
		primaryStage.show();
		runVersionTask();
	}

	private void runVersionTask() {
		loadTask(
				new AdcVersionCheckTask(ADC_JAR_SAVE_LOCATION,
						ADC_DOWNLOAD_JAR_SAVE_LOCATION,
						JSON_RELEASE_INFO
				), "", new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						boolean error = false;
						if (!ADC_JAR_SAVE_LOCATION.exists()) {
							window.getLblError().setText(bundle.getString("Updater.Fail.adc_didnt_save"));
							error = true;
						}
						final boolean finalError = error;
						new Thread(new Runnable() {
							@Override
							public void run() {
								if (finalError) {
									try {
										Thread.sleep(3000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								Platform.exit();
							}
						}).start();
					}
				}
		);
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
