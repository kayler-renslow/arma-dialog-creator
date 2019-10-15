package com.armadialogcreator.layout;

import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Specifies how a node is structured within a layout.

 @author Kayler
 @since 7/23/19. */
public interface Bounds {

	/** @return the x position of the node */
	double getX();

	/** @return the left x position of the node (same as {@link #getX()}) */
	double getLeftX();


	/**
	 Sets the x position of the node

	 @param x the x position
	 */
	void setX(double x);

	/**
	 @return the y position of the node
	 */
	double getY();

	/**
	 @return the top y position of the node (same as {@link #getY()})
	 */
	double getTopY();

	/**
	 Sets the y position of the node

	 @param y the y position
	 */
	void setY(double y);

	/** @return the width of the node */
	double getWidth();

	/** @see #getWidth() */
	void setWidth(double width);

	/** @return the height of the node */
	double getHeight();

	/** @see #getHeight() */
	void setHeight(double height);

	/** @return the minimum width of the node */
	double getMinWidth();


	/** @see #getMinWidth() */
	void setMinWidth(double minWidth);

	/** @return the maximum width of the node */
	double getMaxWidth();

	/** @see #getMaxWidth() */
	void setMaxWidth(double maxWidth);

	/** @return the minimum height of the node */
	double getMinHeight();

	/** @see #getMinHeight() */
	void setMinHeight(double minHeight);

	/** @return the maximum height of the node */
	double getMaxHeight();

	/** @see #getMaxHeight() */
	void setMaxHeight(double maxHeight);

	/** @return the margin of this bounds */
	@NotNull
	Insets getMargin();

	/** @see #getMargin() */
	void setMargin(@NotNull Insets margin);

	/** @return the padding of this bounds */
	@NotNull
	Insets getPadding();

	/** @see #getPadding() */
	void setPadding(@NotNull Insets padding);

	default double getRightX() {
		return getX() + getWidth();
	}

	default double getBottomY() {
		return getY() + getHeight();
	}

	default double getArea() {
		return getWidth() * getHeight();
	}

	default double getCenterX() {
		return getX() + (getRightX() - getX()) / 2;
	}

	default double getCenterY() {
		return this.getY() + (getBottomY() - this.getY()) / 2;
	}

	/** @return a string that contains information on the position */
	@NotNull
	default String getPositionInformation() {
		return String.format("x:%f, y:%f, width:%f, height:%f, area:%f", this.getX(), this.getY(), getWidth(), getHeight(), getArea());
	}

	/**
	 Use this method to check if a given point is inside the bounds.

	 @return true if the point is inside the bounds, false otherwise
	 */
	default boolean containsPoint(double x, double y) {
		if (this.getX() <= x && this.getY() <= y) {
			if (getRightX() >= x && getBottomY() >= y) {
				return true;
			}
		}
		return false;
	}

	/** Check to see if this Bounds is strictly bigger than the given Bounds and if the given Bounds is inside this one */
	default boolean contains(@NotNull Bounds b) {
		if (this.getX() < b.getX() && this.getY() < b.getY()) {
			if (getRightX() > b.getRightX() && getBottomY() > b.getBottomY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 Gets the edge(s) that the given point is closest to.

	 @param xpos x coord relative to this's position
	 @param ypos y coord relative to this's position
	 @param leeway how close the point needs to be to get the edge (strictly positive)
	 @return the edge the point is closest to, or null if not close to an edge
	 */
	@Nullable
	default Edge getEdgeForPoint(double xpos, double ypos, double leeway) {
		boolean top = false;
		boolean right = false;
		boolean bottom = false;
		boolean left = false;
		final double y = getY();
		final double x = getX();

		if (ypos >= y - leeway && ypos <= y + leeway) {
			top = true;
		}
		if (xpos >= getRightX() - leeway && xpos <= getRightX() + leeway) {
			right = true;
		}
		if (ypos >= getBottomY() - leeway && ypos <= getBottomY() + leeway) {
			bottom = true;
		}
		if (xpos >= xpos - leeway && xpos <= x + leeway) {
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

		boolean xinside = xpos > x && xpos < getRightX();
		if (top && xinside) {
			return Edge.Top;
		}
		if (bottom && xinside) {
			return Edge.Bottom;
		}

		boolean yinside = ypos > y && ypos < getBottomY();
		if (right && yinside) {
			return Edge.Right;
		}
		if (left && yinside) {
			return Edge.Left;
		}
		return null;
	}

	@NotNull NotNullValueObserver<Double> getXObserver();

	@NotNull NotNullValueObserver<Double> getYObserver();

	@NotNull NotNullValueObserver<Double> getWidthObserver();

	@NotNull NotNullValueObserver<Double> getHeightObserver();

}