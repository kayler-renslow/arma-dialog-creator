package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.TreeItem;

/**
 Created by Kayler on 05/15/2016.
 */
class MoveableTreeItem<E> extends TreeItem<TreeItemData<E>> {
	MoveableTreeItem(TreeItemData<E> data) {
		super(data, data.getGraphic());
	}

	/**Creates a tree item that has a placeholder value*/
	MoveableTreeItem(){
		setValue(TreeItemData.getPlaceHolder());
		setGraphic(getValue().getGraphic());
	}

}
