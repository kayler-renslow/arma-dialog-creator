/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
