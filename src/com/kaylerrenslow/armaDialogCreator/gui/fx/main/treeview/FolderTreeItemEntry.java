package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public class FolderTreeItemEntry extends TreeItemEntry {

	public FolderTreeItemEntry(@NotNull String folderText) {
		super(folderText, CellType.FOLDER, EditorComponentTreeView.createFolderIcon());
	}

	@Override
	public @NotNull String getTreeItemText() {
		return getText();
	}

	@Override
	public boolean isPhantom() {
		return true;
	}
}
