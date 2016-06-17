package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.TreeItem;

public interface FoundChild<E> {
	/**
	 This ran when a search through children has begun and a child has been found.

	 @param found the child that was found
	 */
	void found(TreeItem<E> found);

}
