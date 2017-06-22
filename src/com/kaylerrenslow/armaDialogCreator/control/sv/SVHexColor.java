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
public class SVHexColor extends SerializableValue implements SVColor {

	public static final ValueConverter<SVHexColor> CONVERTER = new ValueConverter<SVHexColor>() {
		@Override
		public SVHexColor convert(DataContext context, @NotNull String... values) {
			return new SVHexColor(values[0]);
		}
	};

	private double[] colorArray = new double[3];

	/**
	 Creates a HexColor object based off a hex string (#fffff for example)

	 @throws IllegalArgumentException for when the hex color is formatted wrong
	 */
	public SVHexColor(@NotNull String hex) {
		super(hex);
		this.valuesAsArray[0] = hex;
		colorArray = getColorArray(colorArray, Integer.decode(hex));
	}

	public SVHexColor(@NotNull Color color) {
		super("");
		colorArray[0] = color.getRed();
		colorArray[1] = color.getGreen();
		colorArray[2] = color.getBlue();
		updateHex();
	}

	@Override
	@NotNull
	public SerializableValue deepCopy() {
		return new SVHexColor(this.valuesAsArray[0]);
	}

	/** return the hex color String */
	@NotNull
	public String getHexColor() {
		return this.valuesAsArray[0];
	}

	/**
	 Gets color array. Ignores alpha

	 @param arr stores values in given array (array must be length 3) (r,g,b)
	 */
	public static double[] getColorArray(double[] arr, int color) {
		if (arr.length != 4) {
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

	@Override
	public double getRed() {
		return colorArray[0];
	}

	@Override
	public void setRed(double r) {
		colorArray[0] = r;
		updateHex();
	}

	@Override
	public double getGreen() {
		return colorArray[1];
	}

	@Override
	public void setGreen(double g) {
		colorArray[1] = g;
	}

	@Override
	public double getBlue() {
		return colorArray[2];
	}

	@Override
	public void setBlue(double b) {
		colorArray[2] = b;
	}

	/** @return always 1 */
	@Override
	public double getAlpha() {
		return 1;
	}

	/** Does nothing */
	@Override
	public void setAlpha(double a) {

	}

	@Override
	public void setColor(double[] c) {
		colorArray[0] = c[0];
		colorArray[1] = c[1];
		colorArray[2] = c[2];
		updateHex();
	}

	private void updateHex() {
		final double f = 255.0;
		int r = (int) (getRed() * f);
		int g = (int) (getGreen() * f);
		int b = (int) (getBlue() * f);
		int argb = (r << 16) | (g << 8) | b;
		valuesAsArray[0] = "#" + Integer.toHexString(argb);
	}

	@Override
	@NotNull
	public Color toJavaFXColor() {
		return Color.color(getRed(), getGreen(), getBlue());
	}
}
