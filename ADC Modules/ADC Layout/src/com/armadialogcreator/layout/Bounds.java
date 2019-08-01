package com.armadialogcreator.layout;

import org.jetbrains.annotations.NotNull;

/**
 Specifies how a node is structured within a layout.

 @author Kayler
 @since 7/23/19. */
public class Bounds {
	private Insets margin = Insets.NONE;
	private Insets padding = Insets.NONE;

	private double minWidth, width, maxWidth;
	private double minHeight, height, maxHeight;
	private double x, y;
	private final LayoutNode node;
	private final Layout layout;

	Bounds(@NotNull LayoutNode node, @NotNull Layout layout) {
		this.node = node;
		this.layout = layout;
	}

	/** @return the layout for which this bounds belongs to */
	@NotNull
	public Layout getLayout() {
		return layout;
	}

	/**
	 Sets the width for the node

	 @param width the new width for the node
	 */
	void setWidth(double width) {
		this.width = width;
	}

	/**
	 Sets the height for the node

	 @param height the new height for the node
	 */
	void setHeight(double height) {
		this.height = height;
	}

	/** @return the x position of the node */
	public double getX() {
		return x;
	}

	/**
	 Sets the x position of the node

	 @param x the x position
	 */
	void setX(double x) {
		this.x = x;
	}

	/**
	 @return the y position of the node
	 */
	public double getY() {
		return y;
	}

	/**
	 Sets the y position of the node

	 @param y the y position
	 */
	void setY(double y) {
		this.y = y;
	}

	/** @return the width of the node */
	public double getWidth() {
		return this.width;
	}

	/** @return the height of the node */
	public double getHeight() {
		return this.height;
	}

	/** @return the minimum width of the node */
	public double getMinWidth() {
		return minWidth;
	}

	private void recomputeLayout() {
		this.layout.recomputePositions();
	}

	/** @see #getMinWidth() */
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
		recomputeLayout();
	}

	/** @return the maximum width of the node */
	public double getMaxWidth() {
		return maxWidth;
	}

	/** @see #getMaxWidth() */
	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
		recomputeLayout();
	}

	/** @return the minimum height of the node */
	public double getMinHeight() {
		return minHeight;
	}

	/** @see #getMinHeight() */
	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
		recomputeLayout();
	}

	/** @return the maximum height of the node */
	public double getMaxHeight() {
		return maxHeight;
	}

	/** @see #getMaxHeight() */
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
		recomputeLayout();
	}

	/** @return the margin of this bounds */
	@NotNull
	public Insets getMargin() {
		return margin;
	}

	/** @see #getMargin()  */
	public void setMargin(@NotNull Insets margin) {
		this.margin = margin;
		recomputeLayout();
	}

	/** @return the padding of this bounds */
	@NotNull
	public Insets getPadding() {
		return padding;
	}

	/** @see #getPadding() */
	public void setPadding(@NotNull Insets padding) {
		this.padding = padding;
		recomputeLayout();
	}
}