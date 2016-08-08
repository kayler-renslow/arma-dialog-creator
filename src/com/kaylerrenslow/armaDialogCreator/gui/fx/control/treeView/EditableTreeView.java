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

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Creates a new EditableTreeView with a root node already in place. This class extends javafx.scene.control.TreeView of type TreeItemData */
public class EditableTreeView<E extends TreeItemData> extends javafx.scene.control.TreeView<E> {
	
	public EditableTreeView(@Nullable TreeCellSelectionUpdate selectionUpdate) {
		super(new TreeItem<>());
		this.showRootProperty().set(false);
		
		this.setEditable(true);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setCellSelectionUpdate(selectionUpdate);
	}
	
	public void setCellSelectionUpdate(@Nullable TreeCellSelectionUpdate selectionUpdate) {
		setCellFactory(new TreeFactoryGen<>(new EditableTreeCellFactory<>(this, selectionUpdate)));
	}
	
	/** Clears the TreeView and loads the given tree structure. If treeStructure is null, will just clear the tree */
	public void loadStructure(@Nullable TreeStructure treeStructure) {
		if (treeStructure == null) {
			setRoot(new TreeItem<>());
			return;
		}
		setRoot(new TreeItem<>((E) treeStructure.getRoot().getData()));
		TreeItem<E> parent = getRoot();
		TreeStructure.TreeNode<E> parentNode = treeStructure.getRoot();
		for (TreeStructure.TreeNode<E> node : parentNode.getChildren()) {
			loadStructure(parent, node);
		}
	}
	
	private void loadStructure(TreeItem<E> parent, TreeStructure.TreeNode<E> parentNode) {
		TreeItem<E> newItem = new TreeItem<>(parentNode.getData());
		addChildToParent(parent, newItem);
		for (TreeStructure.TreeNode<E> childNode : parentNode.getChildren()) {
			loadStructure(newItem, childNode);
		}
	}
	
	public TreeStructure<E> exportStructure() {
		return TreeStructure.getStructure(this);
	}
	
	/**
	 Adds a new TreeItem to the tree.
	 
	 @param data data inside tree node
	 */
	public void addChildDataToRoot(E data) {
		addChildToRoot(new TreeItem<>(data));
	}
	
	/**
	 Adds a new TreeItem to the tree.
	 
	 @param index where to add the child at
	 @param data data inside node
	 */
	public void addChildDataToRoot(int index, E data) {
		if (index < 0) {
			addChildDataToRoot(data);
			return;
		}
		addChildToRoot(index, new TreeItem<>(data));
	}
	
	/** Return the most recent selected index */
	public int getSelectedIndex() {
		return getSelectionModel().getSelectedIndex();
	}
	
	/**
	 Removes the specified child from the tree of the given parent.
	 
	 @param parent what TreeItem the item is a child of
	 @param toRemove item to remove
	 */
	protected void removeChild(@NotNull TreeItem<E> parent, @NotNull TreeItem<E> toRemove) {
		FoundChild<E> found = new FoundChild<E>() {
			@Override
			public void found(TreeItem<E> found) {
				found.getValue().delete();
			}
		};
		TreeUtil.stepThroughDescendants(toRemove, found);
		toRemove.getValue().delete();
		parent.getChildren().remove(toRemove);
	}
	
	
	/**
	 Adds a child to a designated parent. This simply calls addChildToParent(TreeItem<E> parent, TreeItem<E> child, int index) with index set to parent.getChildren().size()
	 
	 @param parent parent node
	 @param child node to be made the child of parent
	 */
	protected void addChildToParent(@NotNull TreeItem<E> parent, @NotNull TreeItem<E> child) {
		addChildToParent(parent, child, parent.getChildren().size());
	}
	
	/**
	 Adds a child to a designated parent.
	 
	 @param parent parent node
	 @param child node to be made the child of parent
	 @param index index for where child is to be inserted (use child count to add to end)
	 */
	protected void addChildToParent(@NotNull TreeItem<E> parent, @NotNull TreeItem<E> child, int index) {
		if (index >= parent.getChildren().size()) {
			parent.getChildren().add(child);
		} else {
			parent.getChildren().add(index, child);
		}
	}
	
	/**
	 Puts the child data into a TreeItem and then adds to designated parent.
	 
	 @param parent parent node
	 @param childData node to be made the child of parent
	 */
	protected void addChildDataToParent(@NotNull TreeItem<E> parent, @NotNull E childData) {
		addChildToParent(parent, new TreeItem<>(childData));
	}
	
	/**
	 Adds a child to the root
	 
	 @param item item to be added
	 */
	protected void addChildToRoot(@NotNull TreeItem<E> item) {
		getRoot().getChildren().add(item);
	}
	
	/**
	 Adds a new TreeItem to the tree's root.
	 
	 @param index where to add the child at
	 @param item tree item to add
	 */
	protected void addChildToRoot(int index, @NotNull TreeItem<E> item) {
		if (index < 0) {
			addChildToRoot(item);
			return;
		}
		getRoot().getChildren().add(index, item);
	}
	
	@Nullable
	protected TreeItem<E> getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}
	
	
}
