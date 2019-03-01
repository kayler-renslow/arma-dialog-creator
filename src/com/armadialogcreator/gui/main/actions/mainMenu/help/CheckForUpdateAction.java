package com.armadialogcreator.gui.main.actions.mainMenu.help;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.main.AskSaveProjectDialog;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.updater.github.ReleaseInfo;
import com.armadialogcreator.updater.tasks.AdcVersionCheckTask;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 @author Kayler
 @since 08/22/2017 */
public class CheckForUpdateAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		CheckForUpdateDialog d = new CheckForUpdateDialog();
		d.show();
		if (d.wasCancelled() || d.getReleaseInfo() == null || !d.isUpdateAvailable()) {
			return;
		}
		AskSaveProjectDialog dialog = new AskSaveProjectDialog();
		dialog.showAndWait();
		if (dialog.wasCancelled()) {
			return;
		}
		if (dialog.saveProgress()) {
			ApplicationManager.instance.saveProject();
		}
		ApplicationManager.instance.closeApplication();
		try {
			//get the update
			Runtime.getRuntime().exec("java -jar adc_updater.jar", null);
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
	}

	private static class CheckForUpdateDialog extends StageDialog<VBox> {

		private ReleaseInfo releaseInfo;
		private final Label lblStatus = new Label();
		private final ResourceBundle bundle = Lang.ApplicationBundle();
		private boolean updateAvailable = false;

		public CheckForUpdateDialog() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, false);

			setTitle(bundle.getString("Popups.CheckForUpdate.popup-title"));

			myRootElement.getChildren().add(lblStatus);
			lblStatus.setText(bundle.getString("Popups.CheckForUpdate.checking"));

			footer.getBtnCancel().setDisable(true);
			footer.getBtnOk().setDisable(true);

			setResizable(false);
			myRootElement.setPrefWidth(400);
			myRootElement.setPrefHeight(60);
		}

		@Override
		public void show() {
			Task<ReleaseInfo> task = new Task<ReleaseInfo>() {
				@Override
				protected ReleaseInfo call() throws Exception {
					return AdcVersionCheckTask.getLatestRelease();
				}
			};
			task.stateProperty().addListener((observable, oldValue, state) -> {
				if (state == Worker.State.CANCELLED || state == Worker.State.FAILED || state == Worker.State.SUCCEEDED) {
					releaseInfo = task.getValue();
					if (releaseInfo == null || releaseInfo.getTagName().equals(Lang.Application.VERSION)) {
						if (task.getState() == Worker.State.FAILED) {
							lblStatus.setText(bundle.getString("Popups.CheckForUpdate.failed-to-get-info"));
						} else {
							lblStatus.setText(bundle.getString("Popups.CheckForUpdate.no-update"));
						}
					} else {
						updateAvailable = true;
						lblStatus.setText(
								String.format(
										bundle.getString("Popups.CheckForUpdate.update-available-f"),
										releaseInfo.getTagName()
								)
						);
					}
					footer.getBtnCancel().setDisable(false);
					footer.getBtnOk().setDisable(false);
				}
			});
			Thread thread = new Thread(task);
			thread.setName("Arma Dialog Creator - Check For Update Task");
			thread.setDaemon(true);
			thread.start();

			//place super.show() at bottom or the dialog will freeze the current thread until dialog is closed
			super.show();
		}

		@Nullable
		public ReleaseInfo getReleaseInfo() {
			return releaseInfo;
		}

		public boolean isUpdateAvailable() {
			return updateAvailable;
		}
	}
}
