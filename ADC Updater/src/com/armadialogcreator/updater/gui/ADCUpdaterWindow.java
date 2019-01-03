package com.armadialogcreator.updater.gui;

import com.armadialogcreator.pwindow.ADCStandaloneProgressWindow;
import com.armadialogcreator.updater.ADCUpdater;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 10/22/2016.
 */
public class ADCUpdaterWindow extends ADCStandaloneProgressWindow{


	private final ChangeListener<? super String> taskMessagePropertyListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			getLblStatus().setText(newValue);
		}
	};
	private final ChangeListener<? super Throwable> taskExceptionPropertyListener = new ChangeListener<Throwable>() {
		@Override
		public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
			getLblError().setText("Error (" + newValue.getClass().getSimpleName() + "): " + newValue.getMessage());
			addExitButton(ADCUpdater.bundle.getString("Updater.exit"));
			newValue.printStackTrace();
		}
	};

	public ADCUpdaterWindow(@NotNull Stage stage) {
		super(stage);
		stage.setTitle(ADCUpdater.bundle.getString("Updater.title"));
	}

	public void show() {
		getStage().show();
	}

	public void loadTask(@NotNull Task<?> task, @Nullable EventHandler<WorkerStateEvent> succeedEvent) {
		getProgressBar().setProgress(-1);
		getLblStatus().setText("");
		task.exceptionProperty().addListener(taskExceptionPropertyListener);
		task.messageProperty().addListener(taskMessagePropertyListener);
		task.setOnSucceeded(succeedEvent);
		getProgressBar().progressProperty().bind(task.progressProperty());
		new Thread(task).start();
	}
}
