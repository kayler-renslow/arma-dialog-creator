package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.edit;

import com.kaylerrenslow.armaDialogCreator.data.ChangeUpdateFailedException;
import com.kaylerrenslow.armaDialogCreator.data.Changelog;
import com.kaylerrenslow.armaDialogCreator.data.ChangelogUpdate;
import com.kaylerrenslow.armaDialogCreator.gui.notification.Notification;
import com.kaylerrenslow.armaDialogCreator.gui.notification.Notifications;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/20/2016.
 */
public class EditRedoAction implements EventHandler<ActionEvent> {
	private final MenuItem editRedoMenuItem;

	public EditRedoAction(MenuItem editRedoMenuItem) {
		this.editRedoMenuItem = editRedoMenuItem;
		editRedoMenuItem.setDisable(true);
		final Changelog changelog = Changelog.getInstance();
		changelog.getChangeUpdateGroup().addListener(new UpdateGroupListener<ChangelogUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ChangelogUpdate> group, ChangelogUpdate newChange) {
				switch (newChange.getType()) {
					case CHANGE_ADDED: {
						editRedoMenuItem.setDisable(true);
						editRedoMenuItem.setText(Lang.ApplicationBundle().getString("MainMenuBar.edit_redo"));
						break;
					}
					case REDO: //intentional fall through
					case UNDO: {
						if (changelog.getToRedo() == null) {
							editRedoMenuItem.setText(Lang.ApplicationBundle().getString("MainMenuBar.edit_redo"));
						} else {
							editRedoMenuItem.setText(String.format(Lang.ApplicationBundle().getString("MainMenuBar.edit_redo_f"), changelog.getToRedo().getShortName()));
						}
						editRedoMenuItem.setDisable(changelog.getToRedo() == null);
						break;
					}
					default: {
						throw new IllegalArgumentException("unknown update type:" + newChange.getType());
					}
				}
			}
		});
	}

	@Override
	public void handle(ActionEvent event) {
		Changelog changelog = Changelog.getInstance();

		try {
			changelog.redo();
		} catch (Exception e) {
			if (e instanceof ChangeUpdateFailedException) {
				Notifications.showNotification(new Notification(
								Lang.ApplicationBundle().getString("Notifications.RedoOperation.notification_title"),
								String.format(Lang.ApplicationBundle().getString("Notifications.RedoOperation.notification_body_f"), e.getMessage()),
								Notification.DEFAULT_DURATION,
								true
						)
				);
			} else {
				ExceptionHandler.error(e);
			}
		}

		editRedoMenuItem.setDisable(changelog.getToRedo() == null);
	}
}
