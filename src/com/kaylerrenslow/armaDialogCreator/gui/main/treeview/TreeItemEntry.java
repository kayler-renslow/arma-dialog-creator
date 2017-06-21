package com.kaylerrenslow.armaDialogCreator.gui.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.TreeItemData;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/07/2016.
 */
public abstract class TreeItemEntry extends TreeItemData {
	public TreeItemEntry(@NotNull String text, @NotNull CellType cellType, @Nullable Node graphic) {
		super(text, cellType, graphic);
	}

	/**
	 Invoked when this entry is requested to be duplicated. The request doesn't have to be satisfied.

	 @param treeView the tree view that wants this entry to be duplicated
	 */
	public abstract void duplicate(@NotNull TreeView<? extends TreeItemEntry> treeView);

}
