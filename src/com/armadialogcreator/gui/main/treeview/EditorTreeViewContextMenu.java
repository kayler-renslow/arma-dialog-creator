package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.core.ControlTypeGroup;
import com.armadialogcreator.gui.fxcontrol.BorderedImageView;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeViewMenuItemBuilder;
import com.armadialogcreator.lang.Lang;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 ContextMenu for inserting a new control into the EditorComponentTreeView

 @author Kayler
 @since 06/20/2016. */
public class EditorTreeViewContextMenu extends ContextMenu {

	public EditorTreeViewContextMenu(@NotNull EditorComponentTreeView treeView) {
		ResourceBundle bundle = Lang.ApplicationBundle();
		MenuItem newFolder = new MenuItem(bundle.getString("ContextMenu.ComponentTreeView.new_folder"), EditorComponentTreeView.createFolderIcon());
		getItems().add(newFolder);
		TreeViewMenuItemBuilder.setNewFolderAction(treeView, new TreeItemDataCreator<ArmaControl, TreeItemEntry>() {
			@NotNull
			@Override
			public TreeItemEntry createNew(@NotNull EditableTreeView treeView) {
				return new FolderTreeItemEntry(newFolder.getText());
			}

		}, newFolder);

		Menu groupMenu;
		MenuItem menuItemType;
		for (ControlTypeGroup group : ControlTypeGroup.values()) {
			groupMenu = new Menu(group.getDisplayName());
			for (ControlTreeItemDataCreatorLookup creator : ControlTreeItemDataCreatorLookup.values()) {
				ControlType controlType = creator.controlType;
				if (controlType.getGroup() != group) {
					continue;
				}
				if (!controlType.betaSupported()) {
					continue;
				}
				menuItemType = new MenuItem(controlType.fullDisplayText(), new BorderedImageView(controlType.getIcon()));
				if (controlType.isDeprecated()) {
					menuItemType.getStyleClass().add("deprecated-menu-item");
				}
				if (creator.allowsSubControls) {
					TreeViewMenuItemBuilder.setNewCompositeItemAction(treeView, creator.creator, menuItemType);
				} else {
					TreeViewMenuItemBuilder.setNewItemAction(treeView, creator.creator, menuItemType);
				}

				groupMenu.getItems().add(menuItemType);
			}
			if (groupMenu.getItems().size() > 0) {
				getItems().add(groupMenu);
			}
			//			if (groupMenu.getItems().size() == 0) {
			//				MenuItem miNone = new MenuItem(bundle.getString("Misc.no_items_available"));
			//				miNone.setDisable(true);
			//				groupMenu.getItems().add(miNone);
			//			}
		}

		MenuItem miClearSelection = new MenuItem(bundle.getString("ContextMenu.ControlEdit.clear_selection"));
		miClearSelection.setOnAction(e -> {
			treeView.getSelectionModel().clearSelection();
		});
		getItems().add(miClearSelection);
	}
}
