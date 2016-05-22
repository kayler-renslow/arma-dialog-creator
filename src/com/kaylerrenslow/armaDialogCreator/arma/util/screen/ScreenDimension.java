package com.kaylerrenslow.armaDialogCreator.arma.util.screen;

/**
 Created by Kayler on 05/18/2016.
 */
public enum ScreenDimension {

	D640(640, 360),
	D720(720, 405),
	D848(848, 480),
	D960(960, 540),
	D1024(1024, 576),
	D1280(1280, 720),
	D1366(1366, 768),
	D1600(1600, 900),
	D1920(1920, 1080);

	public final int width;
	public final int height;

	ScreenDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
