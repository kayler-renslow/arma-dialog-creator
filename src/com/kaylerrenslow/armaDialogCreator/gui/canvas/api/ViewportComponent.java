package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

/**
 @author Kayler
 A component that has a position based upon a percentage of the viewport rather than a pixel value
 Created on 06/18/2016. */
public interface ViewportComponent extends CanvasComponent {
	/** Sets the percentage x1 value */
	void setPercentX1(double percentX);

	/** Sets the percentage y1 value */
	void setPercentY1(double percentY);

	/** Sets the percentage x2 value */
	void setPercentX2(double percentX2);

	/** Sets the percentage y2 value */
	void setPercentY2(double percentY2);

	/** Gets the percentage x1 value */
	double getPercentX1();

	/** Gets the percentage y1 value */
	double getPercentY1();

	/** Gets the percentage x2 value */
	double getPercentX2();

	/** Gets the percentage y2 value */
	double getPercentY2();

	/** Gets the canvas screen x position for the given percentage x. */
	int getScreenX(double percentX);

	/** Gets the canvas screen y position for the given percentage y.  */
	int getScreenY(double percentY);

	/** Gets the canvas screen width position for the given percentage width.  */
	int getScreenWidth(double percentW);

	/** Gets the canvas screen height position for the given percentage height.  */
	int getScreenHeight(double percentH);
}