package com.armadialogcreator.layout;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 7/23/19. */
public class Bounds {
	private Insets margin = Insets.NONE;
	private Insets padding = Insets.NONE;

	private double minWidth, width, maxWidth;
	private double minHeight, height, maxHeight;
	private double x, y;
	private final LayoutNode node;

	Bounds(@NotNull LayoutNode node) {
		this.node = node;
	}

	void setWidth(double width) {
		this.width = width;
	}

	void setHeight(double height) {
		this.height = height;
	}

	public double getX() {
		return x;
	}

	void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public double getMinWidth() {
		return minWidth;
	}

	private void recomputeLayout() {
		this.node.layout.recomputePositions();
	}

	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
		recomputeLayout();
	}

	public double getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
		recomputeLayout();
	}

	public double getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
		recomputeLayout();
	}

	public double getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
		recomputeLayout();
	}

	@NotNull
	public Insets getMargin() {
		return margin;
	}

	public void setMargin(@NotNull Insets margin) {
		this.margin = margin;
		recomputeLayout();
	}

	@NotNull
	public Insets getPadding() {
		return padding;
	}

	public void setPadding(@NotNull Insets padding) {
		this.padding = padding;
		recomputeLayout();
	}
}