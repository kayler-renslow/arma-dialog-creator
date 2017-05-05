package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Class used to depict a hex color string.
 @author Kayler
 @since 05/23/2016. */
public class AHexColor extends AColor {
	
	public static final ValueConverter<AHexColor> CONVERTER = new ValueConverter<AHexColor>() {
		@Override
		public AHexColor convert(DataContext context, @NotNull String... values) {
			return new AHexColor(values[0]);
		}
	};

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

	/** @see AColor#AColor(Color) */
	public AHexColor(Color value) {
		super(value);
		updateHex();
	}

	private void updateHex() {
		final double f = 255.0;
		int r = (int) (getRed() * f);
		int g = (int) (getGreen() * f);
		int b = (int) (getBlue() * f);
		int a = (int) (getAlpha() * f);
		int argb = (a << 24) | (r << 16) | (g << 8) | b;
		this.hex = Integer.toHexString(argb);
	}

	@Override
	public void setRed(double r) {
		super.setRed(r);
		updateHex();
	}

	@Override
	public void setGreen(double g) {
		super.setGreen(g);
		updateHex();
	}

	@Override
	public void setBlue(double b) {
		super.setBlue(b);
		updateHex();
	}

	@Override
	public void setAlpha(double a) {
		super.setAlpha(a);
		updateHex();
	}

	@Override
	public void setColor(double[] c) {
		super.setColor(c);
		updateHex();
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
		setColor(convertToColorArray(this.color, hex));
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
		return getColorArray(color);
	}

	/**
	 Returns a color array like: {r,g,b,a} where r,g,b,a are from 0.0 to 1.0 inclusively

	 @param arr stores values in given array (array must be length 4)
	 @throws IllegalArgumentException for when the hex color is formatted wrong
	 */
	public static double[] convertToColorArray(double[] arr, String hex) {
		if (arr.length != 4) {
			throw new IllegalArgumentException("arr.length != 4");
		}
		int color;
		try {
			color = Integer.decode(hex);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Hex string was formatted wrong.");
		}
		return getColorArray(arr, color);
	}

	/** Gets color array
	 @param arr stores values in given array (array must be length 4)
	 */
	public static double[] getColorArray(double[] arr, int color) {
		if (arr.length != 4) {
			throw new IllegalArgumentException("arr.length != 4");
		}
		int r = (color) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 16) & 0xFF;
		int a = (color >> 24) & 0xFF;
		final double f = 255.0;
		arr[0] = r / f;
		arr[1] = g / f;
		arr[2] = b / f;
		arr[3] = a / f;
		return arr;
	}

	public static double[] getColorArray(int color) {
		return getColorArray(new double[4], color);
	}
	
	@Override
	public String toString() {
		return hex;
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.HEX_COLOR_STRING;
	}

	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof AHexColor){
			AHexColor other = (AHexColor) o;
			return this.hex.equals(other.hex);
		}
		return false;
	}
}
