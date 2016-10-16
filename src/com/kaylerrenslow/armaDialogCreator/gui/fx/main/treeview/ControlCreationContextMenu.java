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

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeViewMenuItemBuilder;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import static com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.EditorComponentTreeView.createFolderIcon;

/**
 @author Kayler
 ContextMenu for inserting a new control into the EditorComponentTreeView
 Created on 06/20/2016. */
public class ControlCreationContextMenu extends ContextMenu {

	public ControlCreationContextMenu(EditorComponentTreeView treeView, boolean showNewFolderOption) {
		if (showNewFolderOption) {
			MenuItem newFolder = new MenuItem(Lang.ApplicationBundle().getString("ContextMenu.ComponentTreeView.new_folder"), createFolderIcon());
			getItems().add(newFolder);
			TreeViewMenuItemBuilder.setNewFolderAction(treeView, new TreeItemDataCreator<TreeItemEntry>() {
				@Override
				public TreeItemEntry createNew(CellType cellType) {
					return new FolderTreeItemEntry(newFolder.getText());
				}
			}, newFolder);
		}
		Menu groupMenu;
		MenuItem menuItemType;
		for (ControlType.TypeGroup group : ControlType.TypeGroup.values()) {
			groupMenu = new Menu(group.displayName);
			getItems().add(groupMenu);
			for (ControlTreeItemDataCreatorLookup creator : ControlTreeItemDataCreatorLookup.values()) {
				ControlType controlType = creator.controlType;
				if (controlType.group != group) {
					continue;
				}
				if (!controlType.betaSupported()) {
					continue;
				}
				final StackPane stackPaneControlIcon = new StackPane(new ImageView(ArmaControlLookup.findByControlType(controlType).controlIcon));
				stackPaneControlIcon.setStyle("-fx-background-color:#b3b3b3,white;-fx-background-insets:0,20;-fx-padding:3px;");
				menuItemType = new MenuItem(controlType.fullDisplayText(), stackPaneControlIcon);
				if (controlType.deprecated) {
					menuItemType.getStyleClass().add("deprecated-menu-item");
				}
				if (creator.allowsSubControls) {
					TreeViewMenuItemBuilder.setNewCompositeItemAction(treeView, creator.creator, menuItemType);
				} else {
					TreeViewMenuItemBuilder.setNewItemAction(treeView, creator.creator, menuItemType);
				}

				groupMenu.getItems().add(menuItemType);
			}
			if (groupMenu.getItems().size() == 0) {
				MenuItem miNone = new MenuItem(Lang.ApplicationBundle().getString("Misc.no_items_available"));
				miNone.setDisable(true);
				groupMenu.getItems().add(miNone);
			}
		}
	}
}
