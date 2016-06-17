package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public interface TreeItemEntry {
	/** Get the text to display in the tree */
	@NotNull
	String getTreeItemText();

	/** Return true if the tree item is used only as a visual indicator and doesn't represent the end product. */
	boolean isPhantom();

}
