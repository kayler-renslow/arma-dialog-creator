package com.armadialogcreator.gui.fxcontrol.treeView;

import com.armadialogcreator.gui.main.treeview.UINodeTreeItemData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/15/2016.
 */
public class TreeViewMenuItemBuilder {

	private static <Tv, Td extends UINodeTreeItemData> EventHandler<ActionEvent> createEvent(
			@NotNull EditableTreeView<Tv, Td> treeView, @NotNull TreeItemDataCreator<Tv, Td> creator) {
		return new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				Td treeItemData = creator.createNew(treeView);
				if (treeItemData == null) {
					return;
				}
				TreeItem<Td> selected = treeView.getSelectedItem();
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
		};
	}


	/**
	 Adds a "new item" action.

	 @param treeView tree view the action is linked to
	 @param creator data creator that returns a new instance for each item requested to be created
	 @param menuItem menu item that links the click action to the new item action
	 @throws IllegalStateException when the TreeItemData returned doesn't have the correct cell type
	 */
	public static <Tv, E extends UINodeTreeItemData> void setNewItemAction(
			@NotNull EditableTreeView<Tv, E> treeView, @NotNull TreeItemDataCreator<Tv, E> creator,
			@NotNull MenuItem menuItem) {
		menuItem.setOnAction(createEvent(treeView, creator));
	}

}
