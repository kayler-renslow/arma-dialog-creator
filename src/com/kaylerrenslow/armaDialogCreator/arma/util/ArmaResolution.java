/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.util;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.UIScale;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;

/**
 Stores screen resolution information and methods for retrieving viewport width and height as well as the viewport x and y positions
 @author Kayler
 @since 05/18/2016. */
public class ArmaResolution implements Resolution {
	private int screenWidth, screenHeight;
	private UIScale uiScale;
	
	private int vw, vh, vx, vy;
	private double vwd, vhd, vxd, vyd;
	private double safeZoneX, safeZoneY, safeZoneW, safeZoneH;
	private final UpdateListenerGroup<Resolution> updateGroup = new UpdateListenerGroup<>();
	
	/**
	 Construct a resolution width the given screen dimension and ui scale
	 
	 @param screenDimension screen width and height (<b>must be 16:9 ratio</b>)
	 @param scale ui scale constant
	 */
	public ArmaResolution(ScreenDimension screenDimension, ArmaUIScale scale) {
		this.screenWidth = screenDimension.width;
		this.screenHeight = screenDimension.height;
		this.uiScale = scale;
		recalc();
	}
	
	/** Get the screen width */
	@Override
	public int getScreenWidth() {
		return screenWidth;
	}
	
	/** Get the screen height */
	@Override
	public int getScreenHeight() {
		return screenHeight;
	}
	
	/** Get the ui scale constant (based upon Arma 3 values) */
	@Override
	public UIScale getUIScale() {
		return uiScale;
	}
	
	/** Set the screen dimension (must be 16:9 ratio) */
	public void setScreenDimension(ScreenDimension dimension) {
		this.screenWidth = dimension.width;
		this.screenHeight = dimension.height;
		recalc();
	}
	
	/** Set the ui scale constant */
	public void setUIScale(UIScale uiScale) {
		this.uiScale = uiScale;
		recalc();
	}
	
	@Override
	public void setTo(Resolution r) {
		this.screenWidth = r.getScreenWidth();
		this.screenHeight = r.getScreenHeight();
		this.uiScale = r.getUIScale();
		recalc();
	}
	
	/** Get the viewport screen x position (this is where the viewport's x is zeroed) */
	public int getViewportX() {
		return vx;
	}
	
	/** Get the viewport screen y position (this is where the viewport's y is zeroed) */
	public int getViewportY() {
		return vy;
	}
	
	/** Get the viewport screen width (at this width, 1.0 (percent as decimal) corresponds to this width. 2.0 is 2x this width) */
	public int getViewportWidth() {
		return vw;
	}
	
	/** Get the viewport screen height (at this height, 1.0 (percent as decimal) corresponds to this height. 2.0 is 2x this height) */
	public int getViewportHeight() {
		return vh;
	}
	
	/** Get the viewport screen x position as a floating point number */
	public double getViewportXF() {
		return vxd;
	}
	
	/** Get the viewport screen y position as a floating point number */
	public double getViewportYF() {
		return vyd;
	}
	
	/** Get the viewport screen width as a floating point number */
	public double getViewportWidthF() {
		return vwd;
	}
	
	/** Get the viewport screen height as a floating point number */
	public double getViewportHeightF() {
		return vhd;
	}
	
	/**
	 Get the safe zone x value (percentage as decimal such that: safeZoneX = 0 screenX (also known as the left of the screen))
	 <br> For more information, go to https://community.bistudio.com/wiki/SafeZone
	 */
	public double getSafeZoneX() {
		return safeZoneX;
	}
	
	/**
	 Get the safe zone y value (percentage as decimal such that: safeZoneY = 0 screenY (also known as the top of the screen))
	 <br> For more information, go to https://community.bistudio.com/wiki/SafeZone
	 */
	public double getSafeZoneY() {
		return safeZoneY;
	}
	
	/**
	 Get the safe zone width value (percentage as decimal such that it equals the screen width)
	 <br> For more information, go to https://community.bistudio.com/wiki/SafeZone
	 */
	public double getSafeZoneW() {
		return safeZoneW;
	}
	
	/**
	 Get the safe zone height value  (percentage as decimal such that it equals the screen height)
	 <br> For more information, go to https://community.bistudio.com/wiki/SafeZone
	 */
	public double getSafeZoneH() {
		return safeZoneH;
	}
	
	/**
	 Returns a string that is formatted like Arma's getResolution command.
	 <br> For more information on the command, go to https://community.bistudio.com/wiki/getResolution
	 */
	public String toArmaFormattedString() {
		return String.format("[%d,%d,%d,%d,%f,%f]", getScreenWidth(), getScreenHeight(), getViewportWidth(), getViewportHeight(), (getScreenWidth() * 1.0 / getScreenHeight()), uiScale.getValue());
	}
	
	@Override
	public String toString() {
		return toArmaFormattedString() + " safeZoneX=" + safeZoneX + " safeZoneW=" + safeZoneW + " safeZoneY=" + safeZoneY + " safeZoneH=" + safeZoneH;
	}
	
	/** Recalculate and set the cached values */
	private void recalc() {
		//viewport width and height need to be calculated first
		this.vw = calcViewportWidth();
		this.vh = calcViewportHeight();
		
		this.vx = calcViewportX();
		this.vy = calcViewportY();
		
		this.vwd = vw;
		this.vhd = vh;
		this.vxd = vx;
		this.vyd = vy;
		
		//safe zone values need to be calculated last
		this.safeZoneX = calcSafeZoneX();
		this.safeZoneY = calcSafeZoneY();
		this.safeZoneW = calcSafeZoneW();
		this.safeZoneH = calcSafeZoneH();
		
		updateGroup.update(this);
	}
	
	/** Get the update group. This update group is notified of updates whenever the resolution changes viewport size, screen size, or safeZone positions */
	public UpdateListenerGroup<Resolution> getUpdateGroup() {
		return updateGroup;
	}
	
	private int calcViewportX() {
		return (screenWidth - getViewportWidth()) / 2;
	}
	
	private int calcViewportY() {
		return (screenHeight - getViewportHeight()) / 2;
	}
	
	private int calcViewportWidth() {
		return (int) (screenWidth * 3 / 4 * uiScale.getValue());
	}
	
	private int calcViewportHeight() {
		return (int) (screenHeight * uiScale.getValue());
	}
	
	private double calcSafeZoneX() {
		return -1.0 * getViewportX() / getViewportWidth();
	}
	
	private double calcSafeZoneY() {
		return -1.0 * getViewportY() / getViewportHeight();
	}
	
	private double calcSafeZoneW() {
		return screenWidth * 1.0 / getViewportWidth();
	}
	
	private double calcSafeZoneH() {
		return screenHeight * 1.0 / getViewportHeight();
	}
}
