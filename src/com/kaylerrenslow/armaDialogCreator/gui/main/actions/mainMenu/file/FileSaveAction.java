package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.file;

import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.notification.BoxNotification;
import com.kaylerrenslow.armaDialogCreator.gui.notification.Notification;
import com.kaylerrenslow.armaDialogCreator.gui.notification.Notifications;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileSaveAction implements EventHandler<ActionEvent> {
	private final ResourceBundle bundle = Lang.ApplicationBundle();

	@Override
	public void handle(ActionEvent event) {
		Notification resultNotification;

		try {
			ApplicationDataManager.getInstance().saveProject();
			File saveFile = Project.getCurrentProject().getProjectSaveDirectory();
			resultNotification = new BoxNotification(
					bundle.getString("Notifications.ProjectSave.Success.notification_title"),
					String.format(bundle.getString("Notifications.ProjectSave.Success.notification_body_f"), saveFile.getAbsolutePath())
			);
		} catch (IOException e) {
			e.printStackTrace();
			String reason = e.getMessage() != null && e.getMessage().length() > 0 ? e.getMessage() : bundle.getString("Notifications.ProjectSave.Fail.unknown_reason");
			resultNotification = new BoxNotification(
					bundle.getString("Notifications.ProjectSave.Fail.notification_title"),
					String.format(bundle.getString("Notifications.ProjectSave.Fail.notification_body_f"), reason),
					10 * 1000, true
			);
		}
		Notifications.showNotification(resultNotification);
	}
}
