/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.DefaultComponentContextMenu;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

/**
 The context menu for when a tree item is selected in the tree view

 @author Kayler
 @since 07/13/2016. */
public class ControlEditContextMenu extends ContextMenu {
	public ControlEditContextMenu(EditorComponentTreeView<? extends TreeItemEntry> treeView, @NotNull ControlTreeItemEntry entryClicked) {
		CheckMenuItem checkMenuItemEnable = new CheckMenuItem(Lang.ApplicationBundle().getString("ContextMenu.ControlEdit.enable"));
		checkMenuItemEnable.setSelected(entryClicked.isEnabled());
		checkMenuItemEnable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				entryClicked.setEnabled(checkMenuItemEnable.isSelected());
			}
		});
		getItems().add(checkMenuItemEnable);


		MenuItem miConfigProperties = new MenuItem(Lang.ApplicationBundle().getString("ContextMenu.DefaultComponent.configure"));
		miConfigProperties.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DefaultComponentContextMenu.showControlPropertiesPopup(entryClicked.getMyArmaControl());
			}
		});
		getItems().add(miConfigProperties);

		MenuItem miClearSelection = new MenuItem(Lang.ApplicationBundle().getString("ContextMenu.ControlEdit.clear_selection"));
		miClearSelection.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				treeView.getSelectionModel().clearSelection();
			}
		});
		getItems().add(miClearSelection);
	}
}
