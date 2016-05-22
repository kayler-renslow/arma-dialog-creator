package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.header.IHeaderEntry;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;

/**
 @author Kayler
 The base class for all controls
 Created on 05/20/2016.
 */
public abstract class ArmaControl {
	protected final ControlType myType;
	protected final ArmaControlRenderer renderer;
	protected final Resolution resolution;

	protected double x, y, width, height;
	protected int idc;

	public ArmaControl(ControlType type, Resolution resolution, ArmaControlRenderer renderer, int idc) {
		this.myType = type;
		this.renderer = renderer;
		this.resolution = resolution;
		this.idc = idc;
	}

	public void setX(double x) {
		this.x = x;
		renderer.setX1(PositionCalculator.getScreenX(resolution, x));
	}

	public void setY(double y) {
		this.y = y;
		renderer.setY1(PositionCalculator.getScreenY(resolution, y));
	}

	public void setWidth(double width) {
		this.width = width;
		int w = PositionCalculator.getScreenWidth(resolution, width);
		renderer.setX2(renderer.getX1() + w);
	}

	public void setHeight(double height) {
		this.height = height;
		int h = PositionCalculator.getScreenHeight(resolution, height);
		renderer.setY2(renderer.getY1() + h);
	}

	public void updateResolution(Resolution r){
		this.resolution.setTo(r);
		setX(this.x);
		setY(this.y);
		setWidth(this.width);
		setHeight(this.height);
	}

	public int getIdc() {
		return idc;
	}

	public void setIdc(int idc) {
		this.idc = idc;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public abstract IHeaderEntry getProperties();
}
