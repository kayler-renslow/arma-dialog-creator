package com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui;

import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/12/2016.
 */
public class Region {

	protected int x1, y1, x2, y2;

	public Region(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public int getLeftX() {
		return Math.min(x1, x2);
	}

	public int getRightX() {
		return Math.max(x1, x2);
	}

	public int getTopY() {
		return Math.min(y1, y2);
	}

	public int getBottomY() {
		return Math.max(y1, y2);
	}

	public int getArea(){
		return getWidth() * getHeight();
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public int getWidth() {
		return Math.abs(x2 - x1);
	}

	public int getHeight() {
		return Math.abs(y2 - y1);
	}

	/** Set the position equal to the given region */
	public void setPosition(Region r) {
		setPosition(r.x1, r.y1, r.x2, r.y2);
	}

	/** Sets the position based on min x,y values and max x,y values */
	public void setPosition(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	/**
	 Sets the position of the region based on the given top left corner with width and height values

	 @param x1 top left x coord
	 @param y1 top left y coord
	 @param width new width
	 @param height new height
	 */
	public void setPositionWH(int x1, int y1, int width, int height) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1 + width;
		this.y2 = y1 + height;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getCenterX() {
		int left = getLeftX();
		return left + (getRightX() - left) / 2;
	}

	public int getCenterY() {
		int top = getTopY();
		return top + (getBottomY() - top) / 2;
	}

	/** Draw this region as a rectangle without filling it */
	public void drawRectangle(GraphicsContext gc) {
		drawRectangle(gc, getX1(), getY1(), getX2(), getY2());
	}

	public void fillRectangle(GraphicsContext gc) {
		fillRectangle(gc, getX1(), getY1(), getX2(), getY2());
	}

	/** Draw the border of a rectangle without filling it */
	public static void drawRectangle(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		double x1a = x1 + antiAlias;
		double y1a = y1 + antiAlias;
		double x2a = x2 - antiAlias;
		double y2a = y2 - antiAlias;

		gc.strokeLine(x1a, y1a, x2a, y1a); //top left to top right
		gc.strokeLine(x2a, y1a, x2a, y2a); //top right to bottom right
		gc.strokeLine(x2a, y2a, x1a, y2a); //bottom right to bottom left
		gc.strokeLine(x1a, y2a, x1a, y1a); //bottom left to top left
	}

	public static void fillRectangle(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		for (int y = y1; y < y2; y++) {
			gc.strokeLine(x1 + antiAlias, y + antiAlias, x2 - antiAlias, y + antiAlias);
		}
	}

	/**
	 Translate the region's x and y coordinates relative to the given point. Even if this region isn't allowed to move, this method will work.

	 @param dx change in x
	 @param dy change in y
	 */
	public void translate(int dx, int dy) {
		this.x1 += dx;
		this.y1 += dy;
		this.x2 += dx;
		this.y2 += dy;
	}

	public void scale(int dxl, int dxr, int dyt, int dyb){
		this.x1 = getLeftX() + dxl;
		this.x2 = getRightX() + dxr;
		this.y1 = getTopY() + dyt;
		this.y2 = getBottomY() + dyb;
	}

	/** Return true if the point is inside the region, false otherwise */
	public boolean containsPoint(int x, int y) {
		if (getLeftX() <= x && getTopY() <= y) {
			if (getRightX() >= x && getBottomY() >= y) {
				return true;
			}
		}
		return false;
	}

	/** Gets the edge(s) that the given point is closest to.
	 @param x x coord relative to this's position
	 @param y y coord relative to this's position
	 @param leeway how close the point needs to be to get the edge (strictly positive)
	 @return the edge the point is closest to
	 */
	@NotNull
	public Edge getEdgeForPoint(int x, int y, int leeway) {
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
			return Edge.TOP_LEFT;
		}
		if (top && right) {
			return Edge.TOP_RIGHT;
		}
		if (bottom && left) {
			return Edge.BOTTOM_LEFT;
		}
		if (bottom && right) {
			return Edge.BOTTOM_RIGHT;
		}

		boolean xinside = x > getLeftX() && x < getRightX();
		if (top && xinside) {
			return Edge.TOP;
		}
		if (bottom && xinside) {
			return Edge.BOTTOM;
		}

		boolean yinside = y > getTopY() && y < getBottomY();
		if (right && yinside) {
			return Edge.RIGHT;
		}
		if (left && yinside) {
			return Edge.LEFT;
		}
		return Edge.NONE;
	}

	/** Check to see if this region is strictly bigger than the given region and if the given region is inside this one */
	public boolean contains(Region r) {
		if (getLeftX() < r.getLeftX() && getTopY() < r.getTopY()) {
			if (getRightX() > r.getRightX() && getBottomY() > r.getBottomY()) {
				return true;
			}
		}
		return false;
	}
}
