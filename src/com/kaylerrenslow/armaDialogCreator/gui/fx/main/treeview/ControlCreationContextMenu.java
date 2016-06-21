package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeViewMenuItemBuilder;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import static com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.EditorComponentTreeView.createFolderIcon;

/**
 @author Kayler
 ContextMenu for inserting a new control into the EditorComponentTreeView
 Created on 06/20/2016.
 */
public class ControlCreationContextMenu extends ContextMenu {

	public ControlCreationContextMenu(EditorComponentTreeView treeView, boolean showNewFolderOption) {
		if (showNewFolderOption) {
			MenuItem newFolder = new MenuItem(Lang.ContextMenu.ComponentTreeView.NEW_FOLDER, createFolderIcon());
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
				menuItemType = new MenuItem(controlType.displayName + " (" + controlType.typeId + ")");
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
		}
	}
}
