package com.armadialogcreator.layout;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 7/23/19. */
public interface LayoutNode {


	/**
	 Assigns this node a {@link NodeBounds} instance.
	 This method is invoked when this {@link LayoutNode} is added to a {@link Layout}'s
	 children via {@link Layout#getChildren()}

	 @param bounds the bounds assigned to this node
	 */
	void assignBounds(@NotNull NodeBounds bounds);

	/** @return the {@link Bounds} instance passed through {@link #assignBounds(NodeBounds)} */
	@NotNull
	NodeBounds getBounds();

}