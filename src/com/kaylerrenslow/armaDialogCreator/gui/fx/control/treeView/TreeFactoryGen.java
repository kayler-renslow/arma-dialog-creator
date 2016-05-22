package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.TreeCell;
import javafx.util.Callback;

class TreeFactoryGen<E> implements Callback<javafx.scene.control.TreeView<TreeItemData<E>>, TreeCell<TreeItemData<E>>> {
	private EditableTreeCellFactory factory;

	TreeFactoryGen(EditableTreeCellFactory factory) {
		this.factory = factory;
	}

	@Override
	public TreeCell<TreeItemData<E>> call(javafx.scene.control.TreeView<TreeItemData<E>> view) {
		return this.factory.getNewInstance();
	}
}
