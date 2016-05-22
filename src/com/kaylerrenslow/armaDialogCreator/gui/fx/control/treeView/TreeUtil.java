package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.TreeItem;

public class TreeUtil {

	/** Gets the root of the given node.
	 *
	 * @param node node to get the root of
	 * @param treeViewRootHidden true if the treeView the given node is in has a hidden root
	 * @return root of the given node or node if node is a root */
	public static TreeItem<TreeItemData> getRoot(TreeItem<TreeItemData> node, boolean treeViewRootHidden) {
		if (node == null) {
			throw new NullPointerException("The TreeItem given can't possibly have a root because it is null.");
		}

		// check to see if node is already the root
		if (treeViewRootHidden) {
			if (node.getParent() == null) {
				throw new IllegalArgumentException("The node given is the hidden root.");
			}
			if (node.getParent().getParent() == null) {
				return node;// is a root!
			}
		} else {
			if (node.getParent() == null) {
				return node;// is a root!
			}
		}

		// go up the node's parent chain to get the root.
		// if the treeView has a hidden root, the children of the hidden root need to be checked to see if they are roots.
		if (treeViewRootHidden) {
			while (node.getParent().getParent() != null) {
				node = node.getParent();
			}
			return node;

		} else {
			while (node.getParent() != null) {
				node = node.getParent();
			}
			return node;
		}
	}

	/** Checks if the given start TreeItem contains the searchingFor TreeItem as a descendant. Checks are done recursively.
	 *
	 * @param startItem where to start searching
	 * @param searchingFor TreeItem to look for
	 * @return if startItem has the descendant searchingFor */
	public static boolean hasDescendant(TreeItem<TreeItemData<?>> startItem, TreeItem<TreeItemData<?>> searchingFor) {
		if (startItem == null) {
			throw new NullPointerException("startItem is null.");
		}
		if (searchingFor == null) {
			throw new NullPointerException("searchingFor is null.");
		}
		if (startItem.equals(searchingFor)) {
			return true;
		}
		if (startItem.getChildren().size() == 0) {
			return false;
		}
		for (TreeItem<TreeItemData<?>> item : startItem.getChildren()) {
			boolean contains = hasDescendant(item, searchingFor);
			if (contains) {
				return true;
			}

		}

		return false;
	}

	/** Goes through all the descendants of startItem and for each descendant it calls foundAction.found with the descendant as its parameter.
	 *
	 * @param startItem where to start the stepping
	 * @param foundAction action to run for each descendant */
	public static<E> void stepThroughDescendants(TreeItem<TreeItemData<E>> startItem, IFoundChild foundAction) {
		if (startItem == null) {
			throw new NullPointerException("startItem is null.");
		}
		if (foundAction == null) {
			throw new NullPointerException("foundAction is null.");
		}
		for (TreeItem<TreeItemData<E>> item : startItem.getChildren()) {
			foundAction.found(item);
			stepThroughDescendants(item, foundAction);
		}
	}
}
