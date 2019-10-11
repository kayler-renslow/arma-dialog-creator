package com.armadialogcreator.layout;

import org.jetbrains.annotations.NotNull;

/**
 Specifies how a {@link LayoutNode} is structured within a layout.

 @author Kayler
 @since 7/23/19. */
public class SimpleBounds implements Bounds {
	private @NotNull Insets margin = Insets.NONE;
	private @NotNull Insets padding = Insets.NONE;

	private double minWidth, width, maxWidth;
	private double minHeight, height, maxHeight;
	private double x, y;

	public SimpleBounds() {
	}

	/**
	 Sets the width for the node

	 @param width the new width for the node
	 */
	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 Sets the height for the node

	 @param height the new height for the node
	 */
	@Override
	public void setHeight(double height) {
		this.height = height;
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
	 Sets the x position of the node

	 @param x the x position
	 */
	@Override
	public void setX(double x) {
		this.x = x;
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
	 Sets the y position of the node

	 @param y the y position
	 */
	@Override
	public void setY(double y) {
		this.y = y;
	}

	/** @return the width of the node */
	@Override
	public double getWidth() {
		return this.width;
	}

	/** @return the height of the node */
	@Override
	public double getHeight() {
		return this.height;
	}

	/** @return the minimum width of the node */
	@Override
	public double getMinWidth() {
		return minWidth;
	}


	/** @see #getMinWidth() */
	@Override
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
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

}