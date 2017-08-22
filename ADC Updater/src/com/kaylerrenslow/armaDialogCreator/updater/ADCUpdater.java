package com.kaylerrenslow.armaDialogCreator.updater;

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

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 Created by Kayler on 10/20/2016.
 */
public class ADCUpdater extends Application {
	private static final String ADC_JAR = "adc.jar";
	private static final File ADC_JAR_SAVE_LOCATION = new File("./" + ADC_JAR);
	private static final File ADC_DOWNLOAD_JAR_SAVE_LOCATION = new File(".");
	private static final String JSON_RELEASE_INFO = "https://api.github.com/repos/kayler-renslow/arma-dialog-creator/releases/latest";

	public static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.updater.UpdaterBundle");

	/** Launches the updater as a new Application (DO NOT USE when an application is already running. Just create a new instance!) */
	public static void main(String[] args) {
		launch(args);
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
				), "", event -> {
					boolean error = false;
					if (!ADC_JAR_SAVE_LOCATION.exists()) {
						window.getLblError().setText(bundle.getString("Updater.Fail.adc_didnt_save"));
						error = true;
					} else {
						try {
							Runtime.getRuntime().exec("java -jar " + ADC_JAR, null, ADC_JAR_SAVE_LOCATION.getParentFile());
						} catch (IOException e) {
							window.getLblError().setText(bundle.getString("Updater.Fail.couldnt_start"));
							error = true;
						}
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
