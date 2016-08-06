/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/15/2016.
 */
public class TreeViewMenuItemBuilder {

	private static <E extends TreeItemData> EventHandler<ActionEvent> createEvent(@NotNull EditableTreeView<E> treeView, @NotNull TreeItemDataCreator<E> creator, CellType cellType) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				E treeItemData = creator.createNew(cellType);
				if (treeItemData.getCellType() != cellType) {
					throw new IllegalStateException("Cell type doesn't match. Current type:" + treeItemData.getCellType() + ". Requested type:" + cellType);
				}
				addChild(treeView, treeItemData);
			}
		};
	}

	/**
	 Adds a "new folder" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each folder requested to be created
	 @param menuItem menu item that links the click action to the new folder action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <E extends TreeItemData> void setNewFolderAction(@NotNull EditableTreeView<E> treeView, @NotNull TreeItemDataCreator<E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.FOLDER));
	}

	/**
	 Adds a "new item" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each item requested to be created
	 @param menuItem menu item that links the click action to the new item action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <E extends TreeItemData> void setNewItemAction(@NotNull EditableTreeView<E> treeView, @NotNull TreeItemDataCreator<E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.LEAF));
	}

	/**
	 Adds a "new composite item" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each folder requested to be created
	 @param menuItem menu item that links the click action to the new folder action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <E extends TreeItemData> void setNewCompositeItemAction(@NotNull EditableTreeView<E> treeView, @NotNull TreeItemDataCreator<E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.COMPOSITE));
	}

	private static<E extends TreeItemData> void addChild(@NotNull EditableTreeView<E> treeView, @NotNull E treeItemData) {
		TreeItem<E> selected = treeView.getSelectedItem();
		if (selected != null) {
			if (selected.getValue().canHaveChildren()) {
				treeView.addChildDataToParent(selected, treeItemData);
			} else {
				treeView.addChildDataToParent(selected.getParent(), treeItemData);
			}
		} else {
			treeView.addChildDataToRoot(treeView.getSelectedIndex(), treeItemData);
		}
	}
}
