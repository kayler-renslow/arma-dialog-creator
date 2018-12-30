package com.armadialogcreator.gui.main;


import javafx.scene.paint.Color;

/**
 Created by Kayler on 05/18/2016.
 */
public class CanvasViewColors {
	public final static Color DEFAULT_SELECTION = color(0, 147, 255);
	public final static Color DARK_THEME_GRID = color(51, 51, 51);
	public final static Color DARK_THEME_EDITOR_BG = color(26, 26, 26);
	public final static Color DEFAULT_ABS_REGION = Color.RED;
	public final static Color DEFAULT_EDITOR_BG = Color.WHITE;
	public final static Color DEFAULT_GRID = Color.GRAY;
	
	public static Color SELECTION = DEFAULT_SELECTION;
	public static Color GRID = DEFAULT_GRID;
	public static Color EDITOR_BG = DEFAULT_EDITOR_BG;
	
	public static Color ABS_REGION = DEFAULT_ABS_REGION;
	
	
	/** Takes rgba values (0-255) and returns Color instance */
	public static Color color(int r, int g, int b, int a) {
		return Color.color(r / 255.0, g / 255.0, b / 255.0, a / 255.0);
	}
	
	/** Takes rgb values (0-255) and returns Color instance */
	public static Color color(int r, int g, int b) {
		return Color.color(r / 255.0, g / 255.0, b / 255.0);
	}
}
