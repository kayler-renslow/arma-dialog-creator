package com.armadialogcreator.gui.uicanvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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

	/** Draw this region as a crisp rectangle without filling it */
	default void strokeRectangle(GraphicsContext gc) {
		strokeRectangle(gc, getX1(), getY1(), getX2(), getY2());
	}

	/**
	 Draw this region as a crisp rectangle and fills it.
	 Uses {@link GraphicsContext#getStroke()} as fill color
	 */
	default void fillRectangle(GraphicsContext gc) {
		fillRectangle(gc, getX1(), getY1(), getX2(), getY2());
	}

	/** Draw the crisp border of a rectangle without filling it */
	static void strokeRectangle(GraphicsContext gc, int x1, int y1, int x2, int y2) {
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

	/** Draw a crisp line */
	static void strokeLine(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		double x1a = x1 + antiAlias;
		double y1a = y1 + antiAlias;
		double x2a = x2 - antiAlias;
		double y2a = y2 + antiAlias;

		gc.strokeLine(x1a, y1a, x2a, y2a);
	}

	/** Fills a crisp rectangle (will use {@link GraphicsContext#getStroke()}) as fill color */
	static void fillRectangle(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		final double antiAlias = gc.getLineWidth() % 2 != 0 ? 0.5 : 0;
		for (int y = y1; y < y2; y++) {
			gc.strokeLine(x1 + antiAlias, y + antiAlias, x2 - antiAlias, y + antiAlias);
		}
	}

	/**
	 Paint a checkerboard with the given {@link GraphicsContext} and coordinates

	 @param gc context
	 @param x x pos
	 @param y y pos
	 @param w width
	 @param h height
	 @param color1 one of the colors to use
	 @param color2 other color to use
	 @param numBoxes number of boxes across horizontal and vertical
	 */
	static void paintCheckerboard(@NotNull GraphicsContext gc, int x, int y, int w, int h,
								  @NotNull Color color1, @NotNull Color color2, int numBoxes) {
		final int boxWidth = w / numBoxes;
		final int boxHeight = h / numBoxes;
		final int remainderWidth = w - numBoxes * boxWidth;
		final int remainderHeight = h - numBoxes * boxHeight;

		gc.save();

		for (int row = 0; row < numBoxes || (row == numBoxes && remainderHeight > 0); row++) {
			//doing <= to make sure that the full height and width is painted
			//since the aspect ratio of the checkerboard may not be equal to aspect ratio of the given width and height

			int yy = y + row * boxHeight;
			for (int box = 0; box < numBoxes || (box == numBoxes && remainderWidth > 0); box++) {
				gc.setStroke((box + row) % 2 == 0 ? color1 : color2);
				int xx = x + box * boxWidth;
				fillRectangle(
						gc, xx, yy,
						xx + (box == numBoxes ? remainderWidth : boxWidth),
						yy + (row == numBoxes ? remainderHeight : boxHeight)
				);
			}
		}

		gc.restore();
	}


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

}
