package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemData;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/09/2019 */
public abstract class UINodeTreeItemData extends TreeItemData {
	private final UINode node;

	public UINodeTreeItemData(@NotNull String text, @Nullable Node graphic, @NotNull UINode node) {
		super(text, graphic);
		this.node = node;
	}

	@NotNull
	public UINode getNode() {
		return node;
	}

	/**
	 Invoked when this entry is requested to be duplicated. The request doesn't have to be satisfied.

	 @param treeView the tree view that wants this entry to be duplicated
	 */
	public abstract void duplicate(@NotNull TreeView<? extends UINodeTreeItemData> treeView);
}
