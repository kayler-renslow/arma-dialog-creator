package com.armadialogcreator.layout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Specifies how a {@link LayoutNode} is structured within a layout.

 @author Kayler
 @since 7/23/19. */
public class NodeBounds implements Bounds {
	private @NotNull Insets margin = Insets.NONE;
	private @NotNull Insets padding = Insets.NONE;

	protected double minWidth, width, maxWidth;
	protected double minHeight, height, maxHeight;
	protected double x, y;
	private final @NotNull LayoutNode node;
	private final @Nullable Layout layout;

	NodeBounds(@NotNull LayoutNode node, @Nullable Layout layout) {
		this.node = node;
		this.layout = layout;
	}

	/**
	 Creates a new instance from an existing instance. The following attributes will be copied from the
	 provided bounds into this bounds:
	 <ul>
	 <li>{@link #getMargin()}</li>
	 <li>{@link #getPadding()}</li>
	 <li>{@link #getMinHeight()}</li>
	 <li>{@link #getHeight()}</li>
	 <li>{@link #getMaxHeight()}</li>
	 <li>{@link #getMinWidth()}</li>
	 <li>{@link #getWidth()}</li>
	 <li>{@link #getMaxWidth()}</li>
	 </ul>

	 @param copy instance to copy attributes from
	 @param node node to assign the bounds to
	 @param layout layout that owns this bounds, or null if no layout owns this bounds
	 */
	NodeBounds(@NotNull NodeBounds copy, @NotNull LayoutNode node, @Nullable Layout layout) {
		this.node = node;
		this.layout = layout;
		this.margin = copy.margin;
		this.padding = copy.padding;
		this.minHeight = copy.minHeight;
		this.minWidth = copy.minWidth;
		this.width = copy.width;
		this.height = copy.height;
		this.maxHeight = copy.maxHeight;
		this.maxWidth = copy.maxWidth;
	}

	/**
	 Sets the width for the node

	 @param width the new width for the node
	 */
	@Override
	public void setWidth(double width) {
		this.width = width;
		this.recomputePositionFromLayout();
	}

	/**
	 Sets the height for the node

	 @param height the new height for the node
	 */
	@Override
	public void setHeight(double height) {
		this.height = height;
		this.recomputePositionFromLayout();
	}

	/** @return the x position of the node */
	@Override
	public double getX() {
		return x;
	}

	/** @return the left x position of the node (same as {@link #getX()}) */
	@Override
	public double getLeftX() {
		return x;
	}

	/**
	 Invoking this method will have no effect

	 @param x the x position
	 */
	@Override
	public void setX(double x) {
		//do nothing
	}

	/**
	 @return the y position of the node
	 */
	@Override
	public double getY() {
		return y;
	}

	/**
	 @return the top y position of the node (same as {@link #getY()})
	 */
	@Override
	public double getTopY() {
		return y;
	}

	/**
	 Invoking this method will have no effect

	 @param y the y position
	 */
	@Override
	public void setY(double y) {
		// do nothing
	}

	/** @return the width of the node */
	@Override
	public double getWidth() {
		if (width > maxWidth) {
			return maxWidth;
		}
		return this.width;
	}

	/** @return the height of the node */
	@Override
	public double getHeight() {
		if (height > maxHeight) {
			return maxHeight;
		}
		return height;
	}

	/** @return the minimum width of the node */
	@Override
	public double getMinWidth() {
		return minWidth;
	}

	private void recomputePositionFromLayout() {
		if (this.layout != null) {
			this.layout.recomputePositions();
		}
	}

	/** @see #getMinWidth() */
	@Override
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
		recomputePositionFromLayout();
	}

	/** @return the maximum width of the node */
	@Override
	public double getMaxWidth() {
		return maxWidth;
	}

	/** @see #getMaxWidth() */
	@Override
	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
		recomputePositionFromLayout();
	}

	/** @return the minimum height of the node */
	@Override
	public double getMinHeight() {
		return minHeight;
	}

	/** @see #getMinHeight() */
	@Override
	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
		recomputePositionFromLayout();
	}

	/** @return the maximum height of the node */
	@Override
	public double getMaxHeight() {
		return maxHeight;
	}

	/** @see #getMaxHeight() */
	@Override
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
		recomputePositionFromLayout();
	}

	/** @return the margin of this bounds */
	@Override
	@NotNull
	public Insets getMargin() {
		return margin;
	}

	/** @see #getMargin() */
	@Override
	public void setMargin(@NotNull Insets margin) {
		this.margin = margin;
		recomputePositionFromLayout();
	}

	/** @return the padding of this bounds */
	@Override
	@NotNull
	public Insets getPadding() {
		return padding;
	}

	/** @see #getPadding() */
	@Override
	public void setPadding(@NotNull Insets padding) {
		this.padding = padding;
		recomputePositionFromLayout();
	}

	@Override
	public double getRightX() {
		return this.x + this.width;
	}

	@Override
	public double getBottomY() {
		return this.y + this.height;
	}

	@Override
	public double getCenterX() {
		return x + (getRightX() - x) / 2;
	}

	@Override
	public double getCenterY() {
		return this.y + (getBottomY() - this.y) / 2;
	}

	@NotNull
	public LayoutNode getNode() {
		return node;
	}

	/**
	 Get A new instance with the attributes copied and assign the optional new layout.
	 The new instance will have the same {@link #getNode()} instance
	 */
	@NotNull
	public NodeBounds copy(@Nullable Layout newLayout) {
		return new NodeBounds(this, this.node, newLayout);
	}
}