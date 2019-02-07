package com.armadialogcreator.canvas;

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

	/**
	 @return an iterable that iterates the child nodes in the order such that the first node is rendered first
	 and the last iterated node is rendered last (is on top)
	 */
	@NotNull
	DeepUINodeIterable deepIterateChildren();

	/**
	 @return a {@link CanvasComponent} instance that will render what this node looks like,
	 or null if this node is purely structural
	 */
	@Nullable CanvasComponent getComponent();

	/** @return the top level node, or null if this node doesn't belong to a tree */
	@Nullable UINode getRootNode();
}
