package com.kaylerrenslow.armaDialogCreator.gui.fx.main;


import javafx.scene.paint.Color;

/**
 Created by Kayler on 05/18/2016.
 */
public class CanvasViewColors {
	public static Color SELECTION = Default.SELECTION;
	public static Color GRID = Default.GRID;
	public static Color ABS_REGION = Default.ABS_REGION;
	public static Color EDITOR_BG = Default.EDITOR_BG;

	public static class Default {
		public final static Color SELECTION = color(0, 147, 255);
		public final static Color GRID = Color.GRAY;
		public final static Color ABS_REGION = Color.RED;
		public final static Color EDITOR_BG = Color.WHITE;

		public final static Color DARK_THEME_GRID = color(51, 51, 51);
		public final static Color DARK_THEME_EDITOR_BG = color(26, 26, 26);
	}

	/** Takes rgba values (0-255) and returns Color instance */
	public static Color color(int r, int g, int b, int a) {
		return Color.color(r / 255.0, g / 255.0, b / 255.0, a / 255.0);
	}

	/** Takes rgb values (0-255) and returns Color instance */
	public static Color color(int r, int g, int b) {
		return Color.color(r / 255.0, g / 255.0, b / 255.0);
	}
}
