package com.kaylerrenslow.armaDialogCreator.arma.util;

import java.text.DecimalFormat;

/**
 @author Kayler
 Converts screen positions and width/height to viewport positions and width/height, and vice versa.
 Created on 05/18/2016. */
public class PositionCalculator {

	/**
	 Identifier used for expression Strings returned from :
	 <ul>
	 <li>{@link #getSafeZoneExpressionX(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionY(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionW(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionH(ArmaResolution, double)}</li>
	 </ul>
	 */
	public static final String SAFE_ZONE_X = "safeZoneX";
	/**
	 Identifier used for expression Strings returned from :
	 <ul>
	 <li>{@link #getSafeZoneExpressionX(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionY(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionW(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionH(ArmaResolution, double)}</li>
	 </ul>
	 */
	public static final String SAFE_ZONE_Y = "safeZoneY";
	/**
	 Identifier used for expression Strings returned from :
	 <ul>
	 <li>{@link #getSafeZoneExpressionX(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionY(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionW(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionH(ArmaResolution, double)}</li>
	 </ul>
	 */
	public static final String SAFE_ZONE_W = "safeZoneW";
	/**
	 Identifier used for expression Strings returned from :
	 <ul>
	 <li>{@link #getSafeZoneExpressionX(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionY(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionW(ArmaResolution, double)}</li>
	 <li>{@link #getSafeZoneExpressionH(ArmaResolution, double)}</li>
	 </ul>
	 */
	public static final String SAFE_ZONE_H = "safeZoneH";

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.########");

	private static String format(double d) {
		return DECIMAL_FORMAT.format(d);
	}


	/**
	 Convert the given screen X position into a <a href='https://community.bistudio.com/wiki/SafeZone'>"Safe Zone"</a> expression String such as "safeZoneX + safeZoneW * .5".<br>
	 Sample:
	 <pre>
	 resolution.screenWidth = 1920
	 screenX = 960
	 output = "safeZoneX + safeZoneW * 0.5"
	 </pre>

	 @param resolution resolution instance
	 @param screenX x screen position
	 @return "Safe Zone" expression String
	 */
	public static String getSafeZoneExpressionX(ArmaResolution resolution, double screenX) {
		final double percentXCanvas = screenX / resolution.getScreenWidth();
		return SAFE_ZONE_X + " + " + SAFE_ZONE_W + " * " + format(percentXCanvas);
	}

	/**
	 Convert the given screen Y position into a <a href='https://community.bistudio.com/wiki/SafeZone'>"Safe Zone"</a> expression String such as "safeZoneU + safeZoneH * .5".<br>
	 Sample:
	 <pre>
	 resolution.screenHeight = 1080
	 screenY = 920
	 output = "safeZoneY + safeZoneH * 0.5"
	 </pre>

	 @param resolution resolution instance
	 @param screenY y screen position
	 @return "Safe Zone" expression String
	 */
	public static String getSafeZoneExpressionY(ArmaResolution resolution, double screenY) {
		final double percentYCanvas = screenY / resolution.getScreenHeight();
		return SAFE_ZONE_Y + " + " + SAFE_ZONE_H + " * " + format(percentYCanvas);
	}

	/**
	 Convert the given screen width into a <a href='https://community.bistudio.com/wiki/SafeZone'>"Safe Zone"</a> expression String such as "safeZoneW * .5".<br>
	 Sample:
	 <pre>
	 resolution.screenWidth = 1920
	 screenY = 960
	 output = "safeZoneW * 0.5" //half the screen width
	 </pre>

	 @param resolution resolution instance
	 @param screenW on-screen width
	 @return "Safe Zone" expression String
	 */
	public static String getSafeZoneExpressionW(ArmaResolution resolution, double screenW) {
		final double percentWCanvas = screenW / resolution.getScreenWidth();
		return SAFE_ZONE_W + " * " + format(percentWCanvas);
	}

	/**
	 Convert the given screen height into a <a href='https://community.bistudio.com/wiki/SafeZone'>"Safe Zone"</a> expression String such as "safeZoneH * .5".<br>
	 Sample:
	 <pre>
	 resolution.screenWidth = 1080
	 screenY = 920
	 output = "safeZoneH * 0.5" //half the screen height
	 </pre>

	 @param resolution resolution instance
	 @param screenH on-screen height
	 @return "Safe Zone" expression String
	 */
	public static String getSafeZoneExpressionH(ArmaResolution resolution, double screenH) {
		final double percentHCanvas = screenH / resolution.getScreenHeight();
		return SAFE_ZONE_H + " * " + format(percentHCanvas);
	}

	/**
	 Get the screen x position for the given percentage of the viewport

	 @param resolution current resolution with viewport width and viewport x position
	 @param percentX percent x value of viewport (as decimal)
	 @return screen x position
	 */
	public static int getScreenX(ArmaResolution resolution, double percentX) {
		return (int) (resolution.getViewportX() + percentX * resolution.getViewportWidth());
	}

	/**
	 Get the screen y position for the given percentage of the viewport

	 @param resolution current resolution with viewport height and viewport y position
	 @param percentY percent Y value of viewport (as decimal)
	 @return screen y position
	 */
	public static int getScreenY(ArmaResolution resolution, double percentY) {
		return (int) (resolution.getViewportY() + percentY * resolution.getViewportHeight());
	}


	/**
	 Get the screen width for the given percentage of the viewport width

	 @param resolution current resolution that has the viewport width
	 @param percentW percentage (as decimal) of viewport width
	 @return on screen width
	 */
	public static int getScreenWidth(ArmaResolution resolution, double percentW) {
		return (int) (resolution.getViewportWidth() * percentW);
	}

	/**
	 Get the screen height for the given percentage of the viewport height

	 @param resolution current resolution that has the viewport height
	 @param percentH percentage (as decimal) of viewport height
	 @return on screen height
	 */
	public static int getScreenHeight(ArmaResolution resolution, double percentH) {
		return (int) (resolution.getViewportHeight() * percentH);
	}

	/**
	 Get the percentage (as decimal) of the viewport that the given screen x position corresponds to

	 @param resolution resolution that holds viewport width and screen x position
	 @param screenX the screen x position
	 @return percentage of viewport(as decimal)
	 */
	public static double getPercentX(ArmaResolution resolution, int screenX) {
		return (screenX - resolution.getViewportXF()) / resolution.getViewportWidthF();
	}

	/**
	 Get the percentage (as decimal) of the viewport that the given screen y position corresponds to

	 @param resolution resolution that holds viewport height and screen y position
	 @param screenY the screen y position
	 @return percentage of viewport (as decimal)
	 */
	public static double getPercentY(ArmaResolution resolution, int screenY) {
		return (screenY - resolution.getViewportYF()) / resolution.getViewportHeightF();
	}

	/**
	 Get the percentage (as decimal) of the viewport width that the given screen width corresponds to

	 @param resolution resolution that holds viewport width and screen x position
	 @param screenW the width of the screen
	 @return percentage of viewport width (as decimal)
	 */
	public static double getPercentWidth(ArmaResolution resolution, int screenW) {
		return screenW / resolution.getViewportWidthF();
	}

	/**
	 Get the percentage (as decimal) of the viewport height that the given screen height corresponds to

	 @param resolution resolution that holds viewport width and screen x position
	 @param screenH the height of the screen
	 @return percentage of viewport height (as decimal)
	 */
	public static double getPercentHeight(ArmaResolution resolution, int screenH) {
		return screenH / resolution.getViewportHeightF();
	}
}
