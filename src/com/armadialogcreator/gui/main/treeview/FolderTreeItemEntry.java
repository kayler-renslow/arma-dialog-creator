package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.control.FolderUINode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public class FolderTreeItemEntry extends UINodeTreeItemData {

	public FolderTreeItemEntry(@NotNull FolderUINode node) {
		super(node.getFolderName(), EditorComponentTreeView.createFolderIcon(),
				node
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void duplicate(@NotNull TreeView<? extends UINodeTreeItemData> treeView) {
		treeView.getRoot().getChildren().add(new TreeItem(
				new FolderTreeItemEntry((FolderUINode) getNode().deepCopy())
		));
	}
}
