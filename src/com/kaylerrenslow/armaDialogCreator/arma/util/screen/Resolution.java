package com.kaylerrenslow.armaDialogCreator.arma.util.screen;

/**
 @author Kayler
 Stores screen resolution information and methods for retrieving viewport width and height as well as the viewport x and y positions
 Created on 05/18/2016. */
public class Resolution {
	private int screenWidth, screenHeight;
	private UIScale uiScale;

	private int vw, vh, vx, vy;
	private double vwd, vhd, vxd, vyd;
	private double safeZoneX, safeZoneY, safeZoneW, safeZoneH;

	/**
	 Construct a resolution width the given screen dimension and ui scale

	 @param screenDimension screen width and height (<b>must be 16:9 ratio</b>)
	 @param scale ui scale constant
	 */
	public Resolution(ScreenDimension screenDimension, UIScale scale) {
		this.screenWidth = screenDimension.width;
		this.screenHeight = screenDimension.height;
		this.uiScale = scale;
		recalc();
	}

	/** Get the screen width */
	public int getScreenWidth() {
		return screenWidth;
	}

	/** Get the screen height */
	public int getScreenHeight() {
		return screenHeight;
	}

	/** Get the ui scale constant (based upon Arma 3 values) */
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

	public void setTo(Resolution r) {
		this.screenWidth = r.screenWidth;
		this.screenHeight = r.screenHeight;
		this.uiScale = r.uiScale;
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
		return String.format("[%d,%d,%d,%d,%f,%f]", getScreenWidth(), getScreenHeight(), getViewportWidth(), getViewportHeight(), (getScreenWidth() * 1.0 / getScreenHeight()), uiScale.value);
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
	}

	private int calcViewportX() {
		return (screenWidth - getViewportWidth()) / 2;
	}

	private int calcViewportY() {
		return (screenHeight - getViewportHeight()) / 2;
	}

	private int calcViewportWidth() {
		return (int) (screenWidth * 3 / 4 * uiScale.value);
	}

	private int calcViewportHeight() {
		return (int) (screenHeight * uiScale.value);
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
