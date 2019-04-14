package com.armadialogcreator.gui.fxcontrol.treeView;

import javafx.scene.control.TreeItem;

public interface FoundChild<E> {
	/**
	 This ran when a search through children has begun and a child has been found.
	 
	 @param found the child that was found
	 @return false if tree items should continue to be discovered, true if not
	 */
	boolean found(TreeItem<E> found);
	
}
