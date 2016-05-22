package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.IGraphicCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/15/2016.
 */
public class TreeViewMenuItemBuilder {

	private static <E> EventHandler<ActionEvent> createEvent(@NotNull EditableTreeView treeView, @NotNull String text, @NotNull E data, @NotNull CellType cellType, @Nullable IGraphicCreator creator) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TreeItemData<E> treeItemData = new TreeItemData<E>(text, cellType, data, creator);
				addChild(treeView, treeItemData);
			}
		};
	}

	public static <E> void setNewFolderAction(@NotNull EditableTreeView treeView, @NotNull MenuItem menuItem, @NotNull String defaultFolderName, @NotNull E data, @Nullable IGraphicCreator creator) {
		menuItem.setOnAction(createEvent(treeView, defaultFolderName, data, CellType.FOLDER, creator));
	}

	public static <E> void setNewItemAction(@NotNull EditableTreeView treeView, @NotNull MenuItem menuItem, @NotNull String itemName, @NotNull E data, @Nullable IGraphicCreator creator) {
		menuItem.setOnAction(createEvent(treeView, itemName, data, CellType.LEAF, creator));
	}

	public static <E> void setNewCompositeItemAction(@NotNull EditableTreeView treeView, @NotNull MenuItem menuItem, @NotNull String itemName, @NotNull E data, @Nullable IGraphicCreator creator) {
		menuItem.setOnAction(createEvent(treeView, itemName, data, CellType.COMPOSITE, creator));
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
