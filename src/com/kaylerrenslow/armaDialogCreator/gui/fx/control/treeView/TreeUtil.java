package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

public class TreeUtil {



	/**
	 Checks if the given start TreeItem contains the searchingFor TreeItem as a descendant. Checks are done recursively.

	 @param startItem where to start searching
	 @param searchingFor TreeItem to look for
	 @return if startItem has the descendant searchingFor
	 */
	public static boolean hasDescendant(@NotNull TreeItem<?> startItem, @NotNull TreeItem<?> searchingFor) {
		if (startItem.equals(searchingFor)) {
			return true;
		}
		if (startItem.getChildren().size() == 0) {
			return false;
		}
		for (TreeItem<?> item : startItem.getChildren()) {
			boolean contains = hasDescendant(item, searchingFor);
			if (contains) {
				return true;
			}

		}

		return false;
	}

	/**
	 Goes through all the descendants of startItem and for each descendant it calls foundAction.found with the descendant as its parameter.

	 @param startItem where to start the stepping
	 @param foundAction action to run for each descendant
	 */
	public static <E> void stepThroughDescendants(@NotNull TreeItem<E> startItem, @NotNull FoundChild<E> foundAction) {
		for (TreeItem<E> item : startItem.getChildren()) {
			foundAction.found(item);
			stepThroughDescendants(item, foundAction);
		}
	}
}
