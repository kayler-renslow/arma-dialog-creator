package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.gui.fxcontrol.treeView.CellType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public class FolderTreeItemEntry extends TreeItemEntry {

	public FolderTreeItemEntry(@NotNull String folderText) {
		super(folderText, CellType.FOLDER, EditorComponentTreeView.createFolderIcon());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void duplicate(@NotNull TreeView<? extends TreeItemEntry> treeView) {
		treeView.getRoot().getChildren().add(new TreeItem(
				new FolderTreeItemEntry(this.getText() + "copy")
		));
	}
}
