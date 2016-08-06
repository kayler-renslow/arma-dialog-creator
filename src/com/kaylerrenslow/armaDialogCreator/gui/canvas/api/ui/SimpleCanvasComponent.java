/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui;


import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Region;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Default implementation of CanvasComponent
 Created on 05/12/2016. */
public class SimpleCanvasComponent implements CanvasComponent {

	private static Color randomColor(Object o) {
		int argb = o.hashCode();
		int r = (argb) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = (argb >> 16) & 0xFF;
		final double d = 255.0;
		return Color.color(r / d, g / d, b / d, 1);
	}

	protected int x1, y1, x2, y2;
	protected Color backgroundColor = randomColor(this);
	protected Color textColor = backgroundColor.invert();

	private Border border;
	private boolean isEnabled = true;
	private boolean isVisible = true;

	public SimpleCanvasComponent(int x, int y, int width, int height) {
		this.x1 = x;
		this.y1 = y;
		this.x2 = x + width;
		this.y2 = y + height;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	/**
	 Returns true if the component is invisible and is disabled, false otherwise
	 */
	@Override
	public boolean isGhost() {
		return !isVisible && !isEnabled();
	}


	/**
	 Sets the visibility and enable values. A ghost is not visible and is not enabled.
	 */
	@Override
	public void setGhost(boolean ghost) {
		this.isVisible = !ghost;
		setEnabled(!ghost);
	}

	public void paint(GraphicsContext gc) {
		if (border != null) {
			gc.save();
			gc.setStroke(border.getColor());
			gc.setLineWidth(border.getThickness());
			drawRectangle(gc);
			gc.restore();
		}
		gc.setFill(backgroundColor);
		gc.setStroke(backgroundColor);
		fillRectangle(gc);
	}

	public void setBackgroundColor(@NotNull Color paint) {
		this.backgroundColor = paint;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Border getBorder() {
		return border;
	}

	public void setBorder(@Nullable Border border) {
		this.border = border;
	}

	public void setTextColor(@NotNull Color color) {
		this.textColor = color;
	}

	public Color getTextColor() {
		return textColor;
	}

	@Override
	public int getRenderPriority() {
		return 0;
	}

	@Override
	public int getLeftX() {
		return Math.min(x1, x2);
	}

	@Override
	public int getRightX() {
		return Math.max(x1, x2);
	}

	@Override
	public int getTopY() {
		return Math.min(y1, y2);
	}

	@Override
	public int getBottomY() {
		return Math.max(y1, y2);
	}

	@Override
	public int getArea() {
		return getWidth() * getHeight();
	}

	@Override
	public int getX1() {
		return x1;
	}

	@Override
	public int getY1() {
		return y1;
	}

	@Override
	public int getX2() {
		return x2;
	}

	@Override
	public int getY2() {
		return y2;
	}

	@Override
	public int getWidth() {
		return Math.abs(x2 - x1);
	}

	@Override
	public int getHeight() {
		return Math.abs(y2 - y1);
	}

	/** Set the position equal to the given region */
	@Override
	public void setPosition(Region r) {
		setPosition(r.getX1(), r.getY1(), r.getX2(), r.getY2());
	}

	/** Sets the position based on min x,y values and max x,y values */
	@Override
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
	@Override
	public void setPositionWH(int x1, int y1, int width, int height) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1 + width;
		this.y2 = y1 + height;
	}

	@Override
	public void setX1(int x1) {
		this.x1 = x1;
	}

	@Override
	public void setY1(int y1) {
		this.y1 = y1;
	}

	@Override
	public void setX2(int x2) {
		this.x2 = x2;
	}

	@Override
	public void setY2(int y2) {
		this.y2 = y2;
	}

	@Override
	public int getCenterX() {
		int left = getLeftX();
		return left + (getRightX() - left) / 2;
	}

	@Override
	public int getCenterY() {
		int top = getTopY();
		return top + (getBottomY() - top) / 2;
	}


	/**
	 Translate the region's x and y coordinates relative to the given point. Even if this region isn't allowed to move, this method will work.

	 @param dx change in x
	 @param dy change in y
	 */
	@Override
	public void translate(int dx, int dy) {
		this.x1 += dx;
		this.y1 += dy;
		this.x2 += dx;
		this.y2 += dy;
	}

	@Override
	public void scale(int dxl, int dxr, int dyt, int dyb) {
		this.x1 = getLeftX() + dxl;
		this.x2 = getRightX() + dxr;
		this.y1 = getTopY() + dyt;
		this.y2 = getBottomY() + dyb;
	}

	/** Return true if the point is inside the region, false otherwise */
	@Override
	public boolean containsPoint(int x, int y) {
		if (getLeftX() <= x && getTopY() <= y) {
			if (getRightX() >= x && getBottomY() >= y) {
				return true;
			}
		}
		return false;
	}

	/** Check to see if this region is strictly bigger than the given region and if the given region is inside this one */
	@Override
	public boolean contains(Region r) {
		if (getLeftX() < r.getLeftX() && getTopY() < r.getTopY()) {
			if (getRightX() > r.getRightX() && getBottomY() > r.getBottomY()) {
				return true;
			}
		}
		return false;
	}
}
