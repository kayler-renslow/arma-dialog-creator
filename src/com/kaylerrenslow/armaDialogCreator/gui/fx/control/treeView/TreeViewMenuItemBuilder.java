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
				E treeItemData = creator.createNew(cellType, treeView);
				if (treeItemData == null) {
					return;
				}
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
