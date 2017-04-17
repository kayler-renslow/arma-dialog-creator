package com.kaylerrenslow.armaDialogCreator.installer;

import com.kaylerrenslow.armaDialogCreator.pwindow.ADCStandaloneProgressWindow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.PrintStream;

/** This installer window is used when the install directory is set and the contents to install are in a zipfile */
class InstallerProgressWindow extends ADCStandaloneProgressWindow {

	public InstallerProgressWindow(@NotNull Stage stage) {
		super(stage);
		stage.setTitle(ADCInstaller.bundle.getString("InstallerWindow.window_title"));
	}

	public void runInstall(@NotNull File zipFile, @NotNull File installDir) {
		if (!installDir.isDirectory()) {
			throw new IllegalArgumentException("installDir is not a directory");
		}

		PrintStream ps = System.out;

		ADCInstallerTask task = new ADCInstallerTask(zipFile, installDir, ps);
		this.getProgressBar().progressProperty().bind(task.progressProperty());
		task.messageProperty().addListener((observable, oldValue, newValue) -> {
			this.getLblStatus().setText(newValue);
		});
		task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				InstallerProgressWindow.this.addExitButton(ADCInstaller.bundle.getString("InstallerWindow.close_installer"));
			}
		});
		task.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
				InstallerProgressWindow.this.getLblError().setText(newValue.getMessage());
			}
		});
		task.setOnFailed(task.getOnCancelled());
		task.setOnSucceeded(task.getOnCancelled());

		try {
			task.call();
		} catch (Exception e) {
			e.printStackTrace(ps);
		}
	}
}
