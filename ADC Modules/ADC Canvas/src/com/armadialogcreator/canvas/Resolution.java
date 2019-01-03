package com.armadialogcreator.canvas;

import com.armadialogcreator.util.UpdateListenerGroup;

/**
 Created by Kayler on 06/18/2016.
 */
public interface Resolution {

	/** Get the screen width */
	int getScreenWidth();

	/** Get the screen height */
	int getScreenHeight();

	/** Set the screen dimension (must be 16:9 ratio) */
	void setScreenDimension(ScreenDimension dimension);

	/** Set the UIScale */
	void setUIScale(UIScale uiScale);

	UIScale getUIScale();

	/** Sets all properties of this resolution equal to r */
	void setTo(Resolution r);

	/** Get the viewport screen x position (this is where the viewport's x is zeroed) */
	int getViewportX();

	/** Get the viewport screen y position (this is where the viewport's y is zeroed) */
	int getViewportY();

	/** Get the viewport screen width (at this width, 1.0 (percent as decimal) corresponds to this width. 2.0 is 2x this width) */
	int getViewportWidth();

	/** Get the viewport screen height (at this height, 1.0 (percent as decimal) corresponds to this height. 2.0 is 2x this height) */
	int getViewportHeight();

	/** Get the viewport screen x position as a floating point number */
	double getViewportXF();

	/** Get the viewport screen y position as a floating point number */
	double getViewportYF();

	/** Get the viewport screen width as a floating point number */
	double getViewportWidthF();

	/** Get the viewport screen height as a floating point number */
	double getViewportHeightF();

	UpdateListenerGroup<Resolution> getUpdateGroup();

	/** @return the screen's aspect ratio */
	default double getAspectRatio() {
		return getScreenWidth() * 1d / getScreenHeight();
	}
}
