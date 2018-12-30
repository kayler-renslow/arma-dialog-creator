/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */


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
