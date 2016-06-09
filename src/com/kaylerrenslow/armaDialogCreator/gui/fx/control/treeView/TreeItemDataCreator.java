package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

/**
 Created by Kayler on 06/08/2016.
 */
public interface TreeItemDataCreator<E> {
	/**
	 Returns a new instance of a TreeItemData instance.

	 @param cellType the cell type that is requested to be created.
	 */
	TreeItemData<E> createNew(CellType cellType);
}
