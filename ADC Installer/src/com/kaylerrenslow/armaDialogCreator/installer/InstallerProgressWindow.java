package com.kaylerrenslow.armaDialogCreator.installer;

import com.kaylerrenslow.armaDialogCreator.pwindow.ADCStandaloneProgressWindow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/** This installer window is used when the install directory is set and the contents to install are in a zipfile */
class InstallerProgressWindow extends ADCStandaloneProgressWindow {

	public InstallerProgressWindow(@NotNull Stage stage) {
		super(stage);
		stage.setTitle(ADCInstaller.bundle.getString("InstallerWindow.window_title"));
	}

	public void runInstall(@NotNull File installDir) {
		if (!installDir.isDirectory()) {
			throw new IllegalArgumentException("installDir is not a directory");
		}

		ADCInstallerTask task = new ADCInstallerTask(new InstallPackage.JarInstallPackage(), installDir);
		this.getProgressBar().progressProperty().bind(task.progressProperty());
		task.messageProperty().addListener((observable, oldValue, newValue) -> {
			this.getLblStatus().setText(newValue);
		});
		task.setOnCancelled(event -> {
			InstallerProgressWindow.this.addExitButton(ADCInstaller.bundle.getString("InstallerWindow.close_installer"));
		});
		task.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
				InstallerProgressWindow.this.getLblError().setText(newValue.getMessage());
			}
		});
		task.setOnFailed(task.getOnCancelled());
		task.setOnSucceeded(event -> {
			new Thread(() -> {
				try {
					Thread.sleep(1000); //give some time before closing installer to show install completed
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.exit();
			}).start();
		});

		try {
			new Thread(task).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
