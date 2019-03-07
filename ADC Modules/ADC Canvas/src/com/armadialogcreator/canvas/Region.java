package com.armadialogcreator.canvas;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/12/2016.
 */
public interface Region {

	int getRenderPriority();

	void setRenderPriority(int priority);

	int getLeftX();

	int getRightX();

	int getTopY();

	int getBottomY();

	int getArea();

	int getX1();

	int getY1();

	int getX2();

	int getY2();

	int getWidth();

	int getHeight();

	/** Set the position equal to the given region */
	void setPosition(Region r);

	/** Sets the position based on min x,y values and max x,y values */
	void setPosition(int x1, int y1, int x2, int y2);

	/**
	 Sets the position of the region based on the given top left corner with width and height values

	 @param x1 top left x coord
	 @param y1 top left y coord
	 @param width new width
	 @param height new height
	 */
	void setPositionWH(int x1, int y1, int width, int height);

	void setX1(int x1);

	void setY1(int y1);

	void setX2(int x2);

	void setY2(int y2);

	int getCenterX();

	int getCenterY();

	/**
	 Translate the region's x and y coordinates relative to the given point. Even if this region isn't allowed to move, this method will work.

	 @param dx change in x
	 @param dy change in y
	 */
	void translate(int dx, int dy);

	void scale(int dxl, int dxr, int dyt, int dyb);

	/**
	 Use this method to check if a given point is inside the component's bounds.

	 @return true if the point is inside the region, false otherwise
	 */
	default boolean containsPoint(int x, int y) {
		if (getLeftX() <= x && getTopY() <= y) {
			if (getRightX() >= x && getBottomY() >= y) {
				return true;
			}
		}
		return false;
	}

	/** Check to see if this region is strictly bigger than the given region and if the given region is inside this one */
	default boolean contains(@NotNull Region r) {
		if (getLeftX() < r.getLeftX() && getTopY() < r.getTopY()) {
			if (getRightX() > r.getRightX() && getBottomY() > r.getBottomY()) {
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
	default Edge getEdgeForPoint(int x, int y, int leeway) {
		boolean top = false;
		boolean right = false;
		boolean bottom = false;
		boolean left = false;

		if (y >= getTopY() - leeway && y <= getTopY() + leeway) {
			top = true;
		}
		if (x >= getRightX() - leeway && x <= getRightX() + leeway) {
			right = true;
		}
		if (y >= getBottomY() - leeway && y <= getBottomY() + leeway) {
			bottom = true;
		}
		if (x >= getLeftX() - leeway && x <= getLeftX() + leeway) {
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

		boolean xinside = x > getLeftX() && x < getRightX();
		if (top && xinside) {
			return Edge.Top;
		}
		if (bottom && xinside) {
			return Edge.Bottom;
		}

		boolean yinside = y > getTopY() && y < getBottomY();
		if (right && yinside) {
			return Edge.Right;
		}
		if (left && yinside) {
			return Edge.Left;
		}
		return Edge.None;
	}

}
