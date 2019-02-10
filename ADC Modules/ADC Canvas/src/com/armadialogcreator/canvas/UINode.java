package com.armadialogcreator.canvas;

import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/06/2019 */
public interface UINode {
	/** @return an iterable that iterates all children in an order that doesn't need to matter */
	@NotNull Iterable<? extends UINode> iterateChildNodes();

	/** @return the update group for when a re-render is requested for this node */
	@NotNull UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup();

	/** @return how many children this node has */
	int getChildCount();

	/** @return index of where the child is stored, or -1 if the node is not a child */
	int indexOf(@NotNull UINode child);

	/** @return true if the direct children of this node contains the provided node */
	boolean containsChildNode(@NotNull UINode node);

	/**
	 Appends the provided node to the end of the children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>
	 */
	void addChild(@NotNull UINode node);

	/**
	 Appends the provided node at the specified index.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>
	 */
	void addChild(@NotNull UINode node, int index);

	/**
	 Removes the provided node from the direct children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>null</code>

	 @return true if the node was removed, false if it wasn't in the children
	 */
	boolean removeChild(@NotNull UINode node);

	/**
	 Removes the node at the specified index from the direct children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>null</code>
	 */
	void removeChild(int index);

	/**
	 Moves the provided node to a new parent's children at an index.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 */
	void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex);

	/**
	 Takes the moved child from the old parent and places it in this node's children.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>
	 */
	void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex);

	/**
	 @return an iterable that iterates the all children and descendant nodes in the order such that the first node is rendered first
	 and the last iterated node is rendered last (is on top)
	 */
	@NotNull
	DeepUINodeIterable deepIterateChildren();

	/**
	 @return a {@link CanvasComponent} instance that will render what this node looks like,
	 or null if this node is purely structural
	 */
	@Nullable CanvasComponent getComponent();

	/**
	 @return the top level node, or null if this node doesn't belong to a tree.
	 If this node is the top level node, <code>this</code> will be returned
	 */
	@Nullable UINode getRootNode();

	/** @return the parent node of this node, or {@link #getRootNode()} if is the root node, or null if not member of a tree */
	@Nullable UINode getParentNode();

	/** Sets the parent node */
	void setParentNode(@Nullable UINode newParent);

	/** Sets the root node */
	void setRootNode(@Nullable UINode newParent);

	@Nullable
	default UINode getFirstNonStructureAncestorNode() {
		UINode node = getParentNode();
		while (node != null && node.getComponent() == null && node != node.getRootNode()) {
			node = node.getParentNode();
		}
		return node;
	}

	@NotNull DataContext getUserData();

	@NotNull UpdateListenerGroup<UINodeChange> getUpdateGroup();
}
