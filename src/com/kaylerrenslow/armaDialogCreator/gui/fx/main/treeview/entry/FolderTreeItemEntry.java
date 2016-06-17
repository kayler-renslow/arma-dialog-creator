package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public class FolderTreeItemEntry implements GroupedTreeItemEntry {

	private final String folderText;

	public FolderTreeItemEntry(@NotNull String folderText) {
		this.folderText = folderText;
	}

	@Override
	public @NotNull String getTreeItemText() {
		return folderText;
	}


	@Override
	public boolean isPhantom() {
		return true;
	}
}
