package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ColorUtil;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Defines a color as an array.

 @author Kayler
 @since 05/22/2016. */
public class SVColorArray extends SerializableValue implements SVColor {

	private static final double EPSILON = 0.0001;

	/** @return the rounded result that has decimal places count equal to what {@link SVColor#format} would output */
	public static double round(double d) {
		return Math.round(d * 10000) / 10000.0;
	}

	/** @return if the doubles are equal to one another */
	public static boolean equalTo(double d1, double d2) {
		return d1 == d2 || Math.abs(d1 - d2) < EPSILON;
	}


	public static final StringArrayConverter<SVColorArray> CONVERTER = new StringArrayConverter<SVColorArray>() {
		@Override
		public SVColorArray convert(@NotNull Env env, @NotNull String[] values) throws Exception {
			return new SVColorArray(values);
		}
	};

	/** Colors where each value is ranged from 0.0 - 1.0 inclusively. */
	private double r, g, b, a;


	/**
	 Creates a color

	 @param r red (range 0-1.0)
	 @param g green (range 0-1.0)
	 @param b blue (range 0-1.0)
	 @param a alpha (range 0-1.0)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1
	 */
	public SVColorArray(double r, double g, double b, double a) {
		setRedF(r);
		setGreenF(g);
		setBlueF(b);
		setAlphaF(a);
	}

	/**
	 Creates a color

	 @param r red (range 0-255)
	 @param g green (range 0-255)
	 @param b blue (range 0-255)
	 @param a alpha (range 0-255)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 255
	 */
	public SVColorArray(int r, int g, int b, int a) {
		setRedI(r);
		setGreenI(g);
		setBlueI(b);
		setAlphaI(a);
	}

	/**
	 Creates a color from a double array of length 4

	 @param rgba the color array that must have length=4
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1. Also throws it when rgba.length != 4
	 */
	public SVColorArray(double[] rgba) {
		this(0, 0, 0, 0);
		setColorF(rgba);
	}

	/** Set the color from a JavaFX Color instance */
	public SVColorArray(@NotNull Color newValue) {
		this(newValue.getRed(), newValue.getGreen(), newValue.getBlue(), newValue.getOpacity());
	}

	/**
	 Create a new color from String array that is formatted like so:
	 {r,g,b,a} where r,g,b,a are between 0.0 and 1.0 inclusively

	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 4)
	 */
	public SVColorArray(@NotNull String[] newValue) throws NumberFormatException, IndexOutOfBoundsException {
		this(Double.parseDouble(newValue[0]), Double.parseDouble(newValue[1]), Double.parseDouble(newValue[2]), Double.parseDouble(newValue[3]));
	}

	@Override
	public int getRedI() {
		return ColorUtil.toInt(r);
	}

	@Override
	public void setRedI(int r) {
		this.r = ColorUtil.toDouble(r);
	}

	@Override
	public int getGreenI() {
		return ColorUtil.toInt(g);
	}

	@Override
	public void setGreenI(int g) {
		this.g = ColorUtil.toDouble(g);
	}

	@Override
	public int getBlueI() {
		return ColorUtil.toInt(b);
	}

	@Override
	public void setBlueI(int b) {
		this.b = ColorUtil.toDouble(b);
	}

	@Override
	public int getAlphaI() {
		return ColorUtil.toInt(a);
	}

	@Override
	public void setAlphaI(int a) {
		this.a = ColorUtil.toDouble(a);
	}

	@Override
	public double getRedF() {
		return r;
	}

	@Override
	public void setRedF(double r) {
		ColorUtil.boundCheckF(r);
		this.r = r;
	}

	@Override
	public double getGreenF() {
		return g;
	}

	@Override
	public void setGreenF(double g) {
		ColorUtil.boundCheckF(g);
		this.g = g;
	}

	@Override
	public double getBlueF() {
		return b;
	}

	@Override
	public void setBlueF(double b) {
		ColorUtil.boundCheckF(b);
		this.b = b;
	}

	@Override
	public double getAlphaF() {
		return a;
	}

	@Override
	public void setAlphaF(double a) {
		ColorUtil.boundCheckF(a);
		this.a = a;
	}

	/** @return the colors as a string array formatted like so: {red, green, blue, alpha} */
	@Override
	@NotNull
	public String[] getAsStringArray() {
		String[] valuesAsArray = new String[4];
		valuesAsArray[0] = format.format(r);
		valuesAsArray[1] = format.format(g);
		valuesAsArray[2] = format.format(b);
		valuesAsArray[3] = format.format(a);
		return valuesAsArray;
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVColorArray(this.r, this.g, this.b, this.a);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Color;
	}

	/**
	 @return the color array into a String.
	 @see SVColor#toStringF(double, double, double, double)
	 */
	@NotNull
	public String toString() {
		return SVColor.toStringF(r, g, b, a);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SVColor) {
			return SVColor.isEqualTo(this, (SVColor) o);
		}
		return false;
	}

}
