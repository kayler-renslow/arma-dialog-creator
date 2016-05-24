package com.kaylerrenslow.armaDialogCreator.arma.util;

/**
 Created by Kayler on 05/23/2016.
 */
public class AHexColor extends AColor {
	private String hex;

	public AHexColor(String hex) {
		super(0, 0, 0, 0);
		setColor(convertToColorArray(hex));
		this.hex = hex;
	}

	public String getHexColor(){
		return this.hex;
	}

	public void setHexColor(String hex) {
		setColor(convertToColorArray(hex));
		this.hex = hex;
	}

	public static double[] convertToColorArray(String hex) {
		int color;
		try {
			color = Integer.decode(hex);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Hex string was formatted wrong.");
		}
		int r = (color) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 16) & 0xFF;
		int a = (color >> 24) & 0xFF;
		double f = 255.0;
		double[] arr = {r / f, g / f, b / f, a / f};
		return arr;
	}
}
