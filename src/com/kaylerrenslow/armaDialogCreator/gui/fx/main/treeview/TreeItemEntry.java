package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemData;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/07/2016.
 */
public abstract class TreeItemEntry extends TreeItemData{
	public TreeItemEntry(@NotNull String text, @NotNull CellType cellType, @Nullable Node graphic) {
		super(text, cellType, graphic);
	}

}
