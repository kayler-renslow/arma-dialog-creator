package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

/**
 All 16:9 screen dimensions.

 @author Kayler
 @since 05/18/2016. */
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
