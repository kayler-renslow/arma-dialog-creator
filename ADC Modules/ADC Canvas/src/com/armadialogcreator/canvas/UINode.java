package com.armadialogcreator.canvas;

import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/06/2019 */
public interface UINode {
	/** @return an iterable that iterates all children in an order that doesn't need to matter */
	@NotNull Iterable<UINode> iterateChildNodes();

	/** @return the update group for when a re-render is requested for this node */
	@NotNull UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup();

	/**
	 Appends a child to this node (the index or whatever doesn't matter)

	 @throws IllegalStateException if {@link #acceptsChildren()} returns false
	 */
	void addChild(@NotNull UINode child);

	/**
	 Removes the given children from this node

	 @throws IllegalStateException if {@link #acceptsChildren()} returns false
	 */
	void removeChild(@NotNull UINode child);

	/** @return how many children this node has */
	int getChildCount();

	/** @return true if this node can have children, false if it can't */
	boolean acceptsChildren();

	/**
	 @return an iterable that iterates the child nodes in the order such that the first node is rendered first
	 and the last iterated node is rendered last (is on top)
	 */
	@NotNull
	Iterable<UINode> iterateChildrenInRenderOrder();

	/**
	 @return a {@link CanvasComponent} instance that will render what this node looks like,
	 or null if this node is purely structural
	 */
	@Nullable CanvasComponent getComponent();

	/** @return the top level node, or null if this node doesn't belong to a tree */
	@Nullable UINode getRootNode();
}
