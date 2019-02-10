package com.armadialogcreator.gui.main.actions.mainMenu.edit;

import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.data.olddata.ChangeUpdateFailedException;
import com.armadialogcreator.data.olddata.Changelog;
import com.armadialogcreator.data.olddata.ChangelogUpdate;
import com.armadialogcreator.gui.notification.BoxNotification;
import com.armadialogcreator.gui.notification.Notification;
import com.armadialogcreator.gui.notification.Notifications;
import com.armadialogcreator.lang.Lang;
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
public class EditUndoAction implements EventHandler<ActionEvent> {
	private final MenuItem undoMenuItem;

	private final ResourceBundle bundle = Lang.getBundle("MainMenuBarBundle");

	public EditUndoAction(MenuItem undoMenuItem) {
		this.undoMenuItem = undoMenuItem;
		this.undoMenuItem.setDisable(true);
		final Changelog changelog = Changelog.getInstance();
		changelog.getChangeUpdateGroup().addListener(new UpdateGroupListener<ChangelogUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ChangelogUpdate> group, @NotNull ChangelogUpdate newChange) {
				switch (newChange.getType()) {
					case CHANGE_ADDED: {
						undoMenuItem.setDisable(false);
						undoMenuItem.setText(String.format(bundle.getString("edit_undo_f"), newChange.getChange().getShortName()));
						break;
					}
					case REDO: //intentional fall through
					case UNDO: {
						if (changelog.getToUndo() == null) {
							undoMenuItem.setText(bundle.getString("edit_undo"));
						} else {
							undoMenuItem.setText(String.format(bundle.getString("edit_undo_f"), changelog.getToUndo().getShortName()));
						}
						undoMenuItem.setDisable(changelog.getToUndo() == null);
						break;
					}
					default: {
						throw new IllegalArgumentException("unexpected update type:" + newChange.getType());
					}
				}
			}
		});
	}

	@Override
	public void handle(ActionEvent event) {
		Changelog changelog = Changelog.getInstance();
		try {
			changelog.undo();
		} catch (Exception e) {
			if (e instanceof ChangeUpdateFailedException) {
				Notifications.showNotification(new BoxNotification(
						bundle.getString("Action.UndoOperation.notification_title"),
						String.format(bundle.getString("Action.UndoOperation.notification_body_f"), e.getMessage()),
								Notification.DEFAULT_DURATION,
								true
						)
				);
			} else {
				ExceptionHandler.error(e);
			}
		}
		undoMenuItem.setDisable(changelog.getToUndo() == null);
	}
}
