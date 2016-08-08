/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 @author Kayler
 Used for setting the entries in a TreeView
 Created on 08/07/2016. */
public class TreeStructure<E extends TreeItemData> {
	
	private final TreeNode<E> root;
	
	public TreeStructure(TreeNode<E> root) {
		this.root = root;
	}
	
	public TreeNode<E> getRoot() {
		return root;
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends TreeItemData> TreeStructure getStructure(@NotNull TreeView<E> treeView) {
		LinkedList<TreeItem<E>> q = new LinkedList<>();
		q.addAll(treeView.getRoot().getChildren());
		TreeItem<E> pop = treeView.getRoot();
		TreeNode<E> parent = new TreeNode<>(treeView.getRoot().getValue());
		TreeNode<E> created = parent;
		TreeNode<E> root = parent;
		int parentInd = 0;
		int childCount = parent.getChildren().size();
		while (q.size() > 0) {
			if(parentInd >= childCount){
				parentInd = 0;
				childCount = pop.getChildren().size();
				parent = created;
			}
			pop = q.pop();
			q.addAll(pop.getChildren());
			created = new TreeNode<>(pop.getValue());
			parent.getChildren().add(created);
			parentInd++;
		}
		
		return new TreeStructure<>(root);
	}
	
	public static class TreeNode<E extends TreeItemData> {
		public static TreeNode[] EMPTY = new TreeNode[0];
		
		private final LinkedList<TreeNode<E>> children = new LinkedList<>();
		private final E data;
		
		public TreeNode(@Nullable E data) {
			this.data = data;
		}
		
		@Nullable
		public E getData() {
			return data;
		}
		
		public LinkedList<TreeNode<E>> getChildren() {
			return children;
		}
		
		/** Return true if the node has no children (getChildren().length==0), or false if it has children */
		public boolean isLeaf() {
			return children.size() == 0;
		}
	}
	
}
