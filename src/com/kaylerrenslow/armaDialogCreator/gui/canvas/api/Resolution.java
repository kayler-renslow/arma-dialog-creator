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

import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;

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
}
