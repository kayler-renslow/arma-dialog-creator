package com.kaylerrenslow.armaDialogCreator.arma.util;

/**
 @author Kayler
 Class used to depict a hex color string.
 Created on 05/23/2016. */
public class AHexColor extends AColor {
	private String hex;

	/**
	 Creates a HexColor object based off a hex string (#fffff for example)

	 @throws IllegalArgumentException for when the hex color is formatted wrong
	 */
	public AHexColor(String hex) {
		super(0, 0, 0, 0);
		setColor(convertToColorArray(hex));
		this.hex = hex;
	}

	/** Get the hex color String */
	public String getHexColor() {
		return this.hex;
	}

	/**
	 sets the hex color string to the given string

	 @throws IllegalArgumentException for when the hex color is formatted wrong
	 */
	public void setHexColor(String hex) {
		setColor(convertToColorArray(hex));
		this.hex = hex;
	}

	/**
	 Returns a color array like: {r,g,b,a} where r,g,b,a are from 0.0 to 1.0 inclusively

	 @throws IllegalArgumentException for when the hex color is formatted wrong
	 */
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
