package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 7/23/19. */
public interface Layout {
	double computeWidth(@NotNull LayoutNode node);

	double computeHeight(@NotNull LayoutNode node);

	double computeX(@NotNull LayoutNode node);

	double computeY(@NotNull LayoutNode node);

	void recomputePositions();

	@NotNull ListObserver<LayoutNode> getChildren();

	/**
	 @return a {@link Shape} instance for the node
	 @throws IllegalArgumentException if the node doesn't belong to the layout
	 */
	@NotNull Shape getShape(@NotNull LayoutNode node);
}
