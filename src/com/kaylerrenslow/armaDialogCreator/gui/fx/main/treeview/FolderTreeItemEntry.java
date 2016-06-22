package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public class FolderTreeItemEntry extends TreeItemEntry {

	private static int id = 0; //delete later since this is for testing

	public FolderTreeItemEntry(@NotNull String folderText) {
		super(folderText + id, CellType.FOLDER, EditorComponentTreeView.createFolderIcon());
		id++;
	}
}
