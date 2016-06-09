package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.data.FolderTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.data.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemData;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/08/2016.
 */
class FolderTreeItem extends TreeItemData<TreeItemEntry> {
	FolderTreeItem(@NotNull String folderName) {
		super(folderName, CellType.FOLDER, new FolderTreeItemEntry(folderName, null), EditorComponentTreeView.createFolderIcon());
	}
}
