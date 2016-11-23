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

/**
 A type of {@link CanvasComponent} that has a position based upon a percentage of the viewport rather than a pixel value

 @author Kayler
 @since 11/22/2016 */
public interface ViewportCanvasComponent extends CanvasComponent {
	/** Sets the percentage x value relative to viewport dimensions  as well as the component's position based on this value */
	void setPercentX(double percentX);

	/** Sets the percentage y value relative to viewport dimensions  as well as the component's position based on this value */
	void setPercentY(double percentY);

	/** Sets the percentage w value relative to viewport dimensions  as well as the component's position based on this value */
	void setPercentW(double percentW);

	/** Sets the percentage h value relative to viewport dimensions as well as the component's position based on this value */
	void setPercentH(double percentH);

	/** Sets the positions as percentages relative to viewport */
	void setPositionPercent(double percentX, double percentY, double percentW, double percentH);

	/** Gets the percentage x value */
	double getPercentX();

	/** Gets the percentage y value */
	double getPercentY();

	/** Gets the percentage width value */
	double getPercentW();

	/** Gets the percentage height value */
	double getPercentH();

	/** Calculates and returns the canvas screen x position for the given percentage x. */
	int calcScreenX(double percentX);

	/** Calculates and returns the canvas screen y position for the given percentage y. */
	int calcScreenY(double percentY);

	/** Calculates and returns the canvas screen width position for the given percentage width. */
	int calcScreenWidth(double percentW);

	/** Calculates and returns the canvas screen height position for the given percentage height. */
	int calcScreenHeight(double percentH);
}
