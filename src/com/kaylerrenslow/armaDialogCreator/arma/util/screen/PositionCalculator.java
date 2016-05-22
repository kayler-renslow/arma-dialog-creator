package com.kaylerrenslow.armaDialogCreator.arma.util.screen;

/**
 @author Kayler
 Converts screen positions and width/height to viewport positions and width/height, and vice versa.
 Created on 05/18/2016. */
public class PositionCalculator {

	/**
	 Get the screen x position for the given percentage of the viewport

	 @param resolution current resolution with viewport width and viewport x position
	 @param percentX percent x value of viewport (as decimal)
	 @return screen x position
	 */
	public static int getScreenX(Resolution resolution, double percentX) {
		System.out.println("positionCalc, viewport::" + resolution.getViewportX() + ", " + resolution.getViewportY());
		return (int) (resolution.getViewportX() + percentX * resolution.getViewportWidth());
	}

	/**
	 Get the screen y position for the given percentage of the viewport

	 @param resolution current resolution with viewport height and viewport y position
	 @param percentY percent Y value of viewport (as decimal)
	 @return screen y position
	 */
	public static int getScreenY(Resolution resolution, double percentY) {
		return (int) (resolution.getViewportY() + percentY * resolution.getViewportHeight());
	}


	/**
	 Get the screen width for the given percentage of the viewport width

	 @param resolution current resolution that has the viewport width
	 @param percentW percentage (as decimal) of viewport width
	 @return on screen width
	 */
	public static int getScreenWidth(Resolution resolution, double percentW) {
		return (int) (resolution.getViewportWidth() * percentW);
	}

	/**
	 Get the screen height for the given percentage of the viewport height

	 @param resolution current resolution that has the viewport height
	 @param percentH percentage (as decimal) of viewport height
	 @return on screen height
	 */
	public static int getScreenHeight(Resolution resolution, double percentH) {
		return (int) (resolution.getViewportHeight() * percentH);
	}

	/**
	 Get the percentage (as decimal) of the viewport that the given screen x position corresponds to

	 @param resolution resolution that holds viewport width and screen x position
	 @param screenX the screen x position
	 @return percentage of viewport(as decimal)
	 */
	public static double getPercentX(Resolution resolution, int screenX) {
		return (screenX - resolution.getViewportXF()) / resolution.getViewportWidthF();
	}

	/**
	 Get the percentage (as decimal) of the viewport that the given screen y position corresponds to

	 @param resolution resolution that holds viewport height and screen y position
	 @param screenY the screen y position
	 @return percentage of viewport (as decimal)
	 */
	public static double getPercentY(Resolution resolution, int screenY) {
		return (screenY - resolution.getViewportYF()) / resolution.getViewportHeightF();
	}

	/**
	 Get the percentage (as decimal) of the viewport width that the given screen width corresponds to

	 @param resolution resolution that holds viewport width and screen x position
	 @param screenW the width of the screen
	 @return percentage of viewport width (as decimal)
	 */
	public static double getPercentWidth(Resolution resolution, int screenW) {
		return screenW / resolution.getViewportWidthF();
	}

	/**
	 Get the percentage (as decimal) of the viewport height that the given screen height corresponds to

	 @param resolution resolution that holds viewport width and screen x position
	 @param screenH the height of the screen
	 @return percentage of viewport height (as decimal)
	 */
	public static double getPercentHeight(Resolution resolution, int screenH) {
		return screenH / resolution.getViewportHeightF();
	}
}
