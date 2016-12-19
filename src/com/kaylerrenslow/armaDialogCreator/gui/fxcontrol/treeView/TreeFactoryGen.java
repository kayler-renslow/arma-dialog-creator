package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;

import javafx.scene.control.TreeCell;
import javafx.util.Callback;

class TreeFactoryGen<E extends TreeItemData> implements Callback<javafx.scene.control.TreeView<E>, TreeCell<E>> {
	private EditableTreeCellFactory factory;

	TreeFactoryGen(EditableTreeCellFactory factory) {
		this.factory = factory;
	}

	@Override
	public TreeCell<E> call(javafx.scene.control.TreeView<E> view) {
		return this.factory.getNewInstance();
	}
}
