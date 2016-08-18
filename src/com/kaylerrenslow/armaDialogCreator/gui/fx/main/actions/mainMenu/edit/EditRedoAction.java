/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.edit;

import com.kaylerrenslow.armaDialogCreator.data.ChangeUpdateFailedException;
import com.kaylerrenslow.armaDialogCreator.data.Changelog;
import com.kaylerrenslow.armaDialogCreator.data.ChangelogUpdate;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 Created by Kayler on 05/20/2016.
 */
public class EditRedoAction implements EventHandler<ActionEvent> {
	private final MenuItem editRedoMenuItem;

	public EditRedoAction(MenuItem editRedoMenuItem) {
		this.editRedoMenuItem = editRedoMenuItem;
		editRedoMenuItem.setDisable(true);
		final Changelog changelog = Changelog.getInstance();
		changelog.getChangeUpdateGroup().addListener(new UpdateListener<ChangelogUpdate>() {
			@Override
			public void update(ChangelogUpdate newChange) {
				switch (newChange.getType()) {
					case CHANGE_ADDED: {
						editRedoMenuItem.setDisable(true);
						editRedoMenuItem.setText(Lang.MainMenuBar.EDIT_REDO);
						break;
					}
					case REDO: //intentional fall through
					case UNDO: {
						if (changelog.getToRedo() == null) {
							editRedoMenuItem.setText(Lang.MainMenuBar.EDIT_REDO);
						} else {
							editRedoMenuItem.setText(String.format(Lang.MainMenuBar.EDIT_REDO_F, changelog.getToRedo().getShortName()));
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
		final Changelog changelog = Changelog.getInstance();
		try {
			changelog.redo();
		} catch (ChangeUpdateFailedException e) {
			ExceptionHandler.getInstance().uncaughtException(e);
		}
		editRedoMenuItem.setDisable(changelog.getToRedo() == null);
	}
}
