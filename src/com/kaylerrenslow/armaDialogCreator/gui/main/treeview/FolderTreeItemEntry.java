package com.kaylerrenslow.armaDialogCreator.gui.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.CellType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public class FolderTreeItemEntry extends TreeItemEntry {

	public FolderTreeItemEntry(@NotNull String folderText) {
		super(folderText, CellType.FOLDER, EditorComponentTreeView.createFolderIcon());
	}
}
