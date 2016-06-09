package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/07/2016.
 */
public class FolderTreeItemEntry extends GroupedTreeItemEntry {

	private final String folderText;

	public FolderTreeItemEntry(@NotNull String folderText, @Nullable TreeItemEntry... items) {
		super(items);
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
