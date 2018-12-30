package com.armadialogcreator.gui.fxcontrol.treeView;

import com.armadialogcreator.gui.main.treeview.TreeItemEntry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/15/2016.
 */
public class TreeViewMenuItemBuilder {

	private static <Tv, Td extends TreeItemEntry> EventHandler<ActionEvent> createEvent(@NotNull EditableTreeView<Tv, Td> treeView, @NotNull TreeItemDataCreator<Tv, Td> creator, CellType cellType) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Td treeItemData = creator.createNew(treeView);
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
	public static <Tv, E extends TreeItemEntry> void setNewFolderAction(@NotNull EditableTreeView<Tv, E> treeView, @NotNull TreeItemDataCreator<Tv, E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.FOLDER));
	}

	/**
	 Adds a "new item" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each item requested to be created
	 @param menuItem menu item that links the click action to the new item action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <Tv, E extends TreeItemEntry> void setNewItemAction(@NotNull EditableTreeView<Tv, E> treeView, @NotNull TreeItemDataCreator<Tv, E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.LEAF));
	}

	/**
	 Adds a "new composite item" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each folder requested to be created
	 @param menuItem menu item that links the click action to the new folder action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <Tv, E extends TreeItemEntry> void setNewCompositeItemAction(@NotNull EditableTreeView<Tv, E> treeView, @NotNull TreeItemDataCreator<Tv, E> creator, @NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator, CellType.COMPOSITE));
	}

	private static <Tv, E extends TreeItemEntry> void addChild(@NotNull EditableTreeView<Tv, E> treeView, @NotNull E treeItemData) {
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
