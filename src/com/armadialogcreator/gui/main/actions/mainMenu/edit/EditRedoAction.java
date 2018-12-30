package com.armadialogcreator.gui.main.actions.mainMenu.edit;

import com.armadialogcreator.data.ChangeUpdateFailedException;
import com.armadialogcreator.data.Changelog;
import com.armadialogcreator.data.ChangelogUpdate;
import com.armadialogcreator.gui.notification.BoxNotification;
import com.armadialogcreator.gui.notification.Notification;
import com.armadialogcreator.gui.notification.Notifications;
import com.armadialogcreator.main.ExceptionHandler;
import com.armadialogcreator.main.Lang;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 Created by Kayler on 05/20/2016.
 */
public class EditRedoAction implements EventHandler<ActionEvent> {
	private final MenuItem editRedoMenuItem;
	private final ResourceBundle bundle = Lang.getBundle("MainMenuBarBundle");

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
						editRedoMenuItem.setText(bundle.getString("edit_redo"));
						break;
					}
					case REDO: //intentional fall through
					case UNDO: {
						if (changelog.getToRedo() == null) {
							editRedoMenuItem.setText(bundle.getString("edit_redo"));
						} else {
							editRedoMenuItem.setText(String.format(bundle.getString("edit_redo_f"), changelog.getToRedo().getShortName()));
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
				Notifications.showNotification(new BoxNotification(
						bundle.getString("Action.RedoOperation.notification_title"),
						String.format(bundle.getString("Action.RedoOperation.notification_body_f"), e.getMessage()),
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
