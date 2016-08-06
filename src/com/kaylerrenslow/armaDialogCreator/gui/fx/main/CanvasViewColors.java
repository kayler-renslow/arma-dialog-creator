/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main;


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
