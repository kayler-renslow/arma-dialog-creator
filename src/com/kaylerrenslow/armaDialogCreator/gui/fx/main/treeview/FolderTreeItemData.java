package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemData;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.FolderTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.TreeItemEntry;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/08/2016.
 */
class FolderTreeItemData extends TreeItemData<TreeItemEntry> {
	FolderTreeItemData(@NotNull String folderName) {
		super(folderName, CellType.FOLDER, new FolderTreeItemEntry(folderName, null), EditorComponentTreeView.createFolderIcon());
	}
}
