package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

/**
 @author Kayler
 A component that has a position based upon a percentage of the viewport rather than a pixel value
 Created on 06/18/2016. */
public interface ViewportComponent extends CanvasComponent {
	/** Sets the percentage x value as well as the component's position based on this value */
	void setPercentX(double percentX);

	/** Sets the percentage y value as well as the component's position based on this value */
	void setPercentY(double percentY);

	/** Sets the percentage w value as well as the component's position based on this value */
	void setPercentW(double percentW);

	/** Sets the percentage h value as well as the component's position based on this value */
	void setPercentH(double percentH);

	/** Gets the percentage x1 value */
	double getPercentX();

	/** Gets the percentage y1 value */
	double getPercentY();

	/** Gets the percentage x2 value */
	double getPercentW();

	/** Gets the percentage y2 value */
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