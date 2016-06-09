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

	private static <E> EventHandler<ActionEvent> createEvent(@NotNull EditableTreeView treeView, @NotNull TreeItemDataCreator<E> creator, CellType cellType) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TreeItemData<E> treeItemData = creator.createNew(cellType);
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
	public static <E> void setNewFolderAction(@NotNull EditableTreeView treeView, @NotNull TreeItemDataCreator<E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.FOLDER));
	}

	/**
	 Adds a "new item" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each item requested to be created
	 @param menuItem menu item that links the click action to the new item action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <E> void setNewItemAction(@NotNull EditableTreeView treeView, @NotNull TreeItemDataCreator<E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.LEAF));
	}

	/**
	 Adds a "new composite item" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each folder requested to be created
	 @param menuItem menu item that links the click action to the new folder action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <E> void setNewCompositeItemAction(@NotNull EditableTreeView treeView, @NotNull TreeItemDataCreator<E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.COMPOSITE));
	}

	private static void addChild(@NotNull EditableTreeView treeView, @NotNull TreeItemData<?> treeItemData) {
		TreeItem<TreeItemData> selected = treeView.getSelectedItem();
		if (selected != null) {
			if (selected.getValue().canHaveChildren()) {
				treeView.addChildToParent(selected, treeItemData);
			} else {
				treeView.addChildToParent(selected.getParent(), treeItemData);
			}
		} else {
			treeView.addChildToRoot(treeView.getSelectedIndex(), treeItemData);
		}
	}
}
