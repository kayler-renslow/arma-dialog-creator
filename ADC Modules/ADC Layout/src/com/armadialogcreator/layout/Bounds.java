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

	public double getRightX() {
		return this.x + this.width;
	}

	public double getBottomY() {
		return this.y + this.height;
	}

	public double getArea() {
		return getWidth() * getHeight();
	}

	public double getCenterX() {
		return x + (getRightX() - x) / 2;
	}

	public double getCenterY() {
		return this.y + (getBottomY() - this.y) / 2;
	}
	/** @return a string that contains information on the position */
	@NotNull
	public String getPositionInformation() {
		return String.format("x:%f, y:%f, width:%f, height:%f, area:%f", this.x, this.y, getWidth(), getHeight(), getArea());
	}

	/**
	 Use this method to check if a given point is inside the bounds.

	 @return true if the point is inside the bounds, false otherwise
	 */
	public boolean containsPoint(double x, double y) {
		if (this.x <= x && this.y <= y) {
			if (getRightX() >= x && getBottomY() >= y) {
				return true;
			}
		}
		return false;
	}

	/** Check to see if this Bounds is strictly bigger than the given Bounds and if the given Bounds is inside this one */
	public boolean contains(@NotNull Bounds b) {
		if (this.x < b.x && this.y < b.y) {
			if (getRightX() > b.getRightX() && getBottomY() > b.getBottomY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 Gets the edge(s) that the given point is closest to.

	 @param x x coord relative to this's position
	 @param y y coord relative to this's position
	 @param leeway how close the point needs to be to get the edge (strictly positive)
	 @return the edge the point is closest to
	 */
	@NotNull
	public Edge getEdgeForPoint(double x, double y, double leeway) {
		boolean top = false;
		boolean right = false;
		boolean bottom = false;
		boolean left = false;

		if (y >= this.y - leeway && y <= this.y + leeway) {
			top = true;
		}
		if (x >= getRightX() - leeway && x <= getRightX() + leeway) {
			right = true;
		}
		if (y >= getBottomY() - leeway && y <= getBottomY() + leeway) {
			bottom = true;
		}
		if (x >= x - leeway && x <= this.x + leeway) {
			left = true;
		}

		if (top && left) {
			return Edge.TopLeft;
		}
		if (top && right) {
			return Edge.TopRight;
		}
		if (bottom && left) {
			return Edge.BottomLeft;
		}
		if (bottom && right) {
			return Edge.BottomRight;
		}

		boolean xinside = x > this.x && x < getRightX();
		if (top && xinside) {
			return Edge.Top;
		}
		if (bottom && xinside) {
			return Edge.Bottom;
		}

		boolean yinside = y > this.y && y < getBottomY();
		if (right && yinside) {
			return Edge.Right;
		}
		if (left && yinside) {
			return Edge.Left;
		}
		return Edge.None;
	}
}