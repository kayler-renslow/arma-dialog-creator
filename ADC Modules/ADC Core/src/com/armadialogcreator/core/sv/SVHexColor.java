package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.old.PropertyType;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Class used to depict a hex color string.

 @author Kayler
 @since 05/23/2016. */
public class SVHexColor extends SerializableValue implements SVColor {

	public static final ValueConverter<SVHexColor> CONVERTER = new ValueConverter<SVHexColor>() {
		@Override
		public SVHexColor convert(DataContext context, @NotNull String... values) {
			return new SVHexColor(values[0]);
		}
	};

	/** Colors where each value is ranged from 0.0 - 1.0 inclusively. Format=[r,g,b,a] */
	private double r, g, b, a;
	private String hex;

	/**
	 Creates a HexColor object based off a hex string (#fffff for example)

	 @throws IllegalArgumentException for when the hex color is formatted wrong
	 */
	public SVHexColor(@NotNull String hex) {
		double[] colorArray = new double[4];
		getColorArray(colorArray, hex);
		setColor(colorArray);
	}

	public SVHexColor(@NotNull Color color) {
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
		updateHex();
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{hex};
	}

	@Override
	@NotNull
	public SerializableValue deepCopy() {
		return new SVHexColor(hex);
	}

	/** return the hex color String */
	@NotNull
	public String getHexColor() {
		return hex;
	}

	/**
	 Gets color array. Ignores alpha

	 @param arr stores values in given array (array must be length 3) (r,g,b).
	 This array is modified directly and is returned. Each value at each index will be randed 0-1
	 @param color color as a hex string
	 @return the array
	 @throws IllegalArgumentException when the color couldn't be converted to an int
	 */
	public static double[] getColorArray(double[] arr, @NotNull String color) {
		return getColorArray(arr, Integer.decode(color));
	}

	/**
	 Gets color array. Ignores alpha

	 @param arr stores values in given array (array must be length 3) (r,g,b).
	 This array is modified directly and is returned. Each value at each index will be randed 0-1
	 @param color the color as an integer
	 @return the array
	 @throws IllegalArgumentException when the array length < 3
	 */
	public static double[] getColorArray(double[] arr, int color) {
		if (arr.length < 3) {
			throw new IllegalArgumentException("arr.length != 4");
		}
		int r = (color) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 16) & 0xFF;
		final double f = 255.0;
		arr[0] = r / f;
		arr[1] = g / f;
		arr[2] = b / f;
		return arr;
	}

	@Override
	public String toString() {
		return getHexColor();
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.HexColorString;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVHexColor) {
			SVHexColor other = (SVHexColor) o;
			return getHexColor().equals(other.getHexColor());
		}
		return false;
	}

	private void boundCheck(double c) {
		if (c < 0.0 || c > 1.0) {
			throw new IllegalArgumentException("Color value is out of range (must be >=0 and <=1): " + c);
		}
	}

	@Override
	public double getRed() {
		return r;
	}

	@Override
	public void setRed(double r) {
		boundCheck(r);
		this.r = r;
		updateHex();
	}

	@Override
	public double getGreen() {
		return g;
	}

	@Override
	public void setGreen(double g) {
		boundCheck(g);
		this.g = g;
		updateHex();
	}

	@Override
	public double getBlue() {
		return b;
	}

	@Override
	public void setBlue(double b) {
		boundCheck(b);
		this.b = b;
		updateHex();
	}

	/** @return always 1 */
	@Override
	public double getAlpha() {
		return 1;
	}

	/** does nothing */
	@Override
	public void setAlpha(double a) {
	}

	@Override
	public void setColor(double[] c) {
		if (c.length != 4) {
			throw new IllegalArgumentException("array length must be 4");
		}
		this.r = c[0];
		this.g = c[1];
		this.b = c[2];
		updateHex();
	}

	private void updateHex() {
		final double f = 255.0;
		int r = (int) (getRed() * f);
		int g = (int) (getGreen() * f);
		int b = (int) (getBlue() * f);
		int argb = (r << 16) | (g << 8) | b;
		String h = Integer.toHexString(argb);
		if (h.length() < 6) {
			h = "000000".substring(0, 6 - h.length()) + h;
		}
		hex = "#" + h;
	}

	@Override
	@NotNull
	public Color toJavaFXColor() {
		return Color.color(getRed(), getGreen(), getBlue());
	}
}
