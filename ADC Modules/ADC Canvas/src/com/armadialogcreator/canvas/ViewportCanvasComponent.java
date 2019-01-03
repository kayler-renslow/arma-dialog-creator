package com.armadialogcreator.canvas;

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
