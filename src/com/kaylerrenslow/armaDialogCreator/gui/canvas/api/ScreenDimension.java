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
 @author Kayler
 All 16:9 screen dimensions.
 Created on 05/18/2016. */
public class ScreenDimension {

	public static final ScreenDimension D640 = new ScreenDimension(640, 360);
	public static final ScreenDimension D720 = new ScreenDimension(720, 405);
	public static final ScreenDimension D848 = new ScreenDimension(848, 480);
	public static final ScreenDimension D960 = new ScreenDimension(960, 540);
	public static final ScreenDimension D1024 = new ScreenDimension(1024, 576);
	public static final ScreenDimension D1280 = new ScreenDimension(1280, 720);
	public static final ScreenDimension D1366 = new ScreenDimension(1366, 768);
	public static final ScreenDimension D1600 = new ScreenDimension(1600, 900);
	public static final ScreenDimension D1920 = new ScreenDimension(1920, 1080);
	
	public static final ScreenDimension SMALLEST = D640;
	public static final ScreenDimension LARGEST = D1920;
	
	/** All screen dimensions */
	private static ScreenDimension[] allValues = {D640, D720, D848, D960, D1024, D1280, D1366, D1600, D1920};

	public final int width;
	public final int height;

	ScreenDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public static ScreenDimension[] values() {
		return allValues;
	}
}
