/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import javafx.scene.canvas.GraphicsContext;
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

	/** Draw this region as a rectangle without filling it */
	default void drawRectangle(GraphicsContext gc){
		drawRectangle(gc, getX1(), getY1(), getX2(), getY2());
	}

	/** Draw this region as a rectangle and fills it */
	default void fillRectangle(GraphicsContext gc){
		fillRectangle(gc, getX1(), getY1(), getX2(), getY2());
	}

	/** Draw the border of a rectangle without filling it */
	static void drawRectangle(GraphicsContext gc, int x1, int y1, int x2, int y2) {
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

	static void fillRectangle(GraphicsContext gc, int x1, int y1, int x2, int y2) {
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
	void translate(int dx, int dy);

	void scale(int dxl, int dxr, int dyt, int dyb);

	/** Return true if the point is inside the region, false otherwise */
	boolean containsPoint(int x, int y);

	/** Gets the edge(s) that the given point is closest to.
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
	boolean contains(Region r);
}
