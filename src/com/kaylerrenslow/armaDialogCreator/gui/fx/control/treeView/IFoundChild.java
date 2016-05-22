package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.TreeItem;

public interface IFoundChild {
	/**
	 This ran when a search through children has begun and a child has been found.

	 @param found the child that was found
	 */
	<E>void found(TreeItem<TreeItemData<E>> found);

}
