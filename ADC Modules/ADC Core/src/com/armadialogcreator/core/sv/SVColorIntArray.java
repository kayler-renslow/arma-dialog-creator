package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.AColor;
import com.armadialogcreator.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

/**
 Defines a color as an array with integer values ranging 0-255.

 @author Kayler
 @since 02/16/2019. */
public class SVColorIntArray extends SerializableValue implements SVColor {


	public static final StringArrayConverter<SVColorIntArray> CONVERTER = new StringArrayConverter<SVColorIntArray>() {
		@Override
		public SVColorIntArray convert(@NotNull Env env, @NotNull String[] values) throws Exception {
			return new SVColorIntArray(values);
		}
	};

	/** Colors where each value is ranged from 0 - 255 inclusively. */
	private int r, g, b, a;


	/**
	 Creates a color

	 @param r red (range 0-1.0)
	 @param g green (range 0-1.0)
	 @param b blue (range 0-1.0)
	 @param a alpha (range 0-1.0)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1
	 */
	public SVColorIntArray(double r, double g, double b, double a) {
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
	public SVColorIntArray(int r, int g, int b, int a) {
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
	public SVColorIntArray(double[] rgba) {
		this(0, 0, 0, 0);
		setColorF(rgba);
	}

	/**
	 Create a new color from String array that is formatted like so:
	 {r,g,b,a} where r,g,b,a are between 0 and 255 inclusively

	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 4)
	 */
	public SVColorIntArray(@NotNull String[] newValue) throws NumberFormatException, IndexOutOfBoundsException {
		this(Integer.parseInt(newValue[0]), Integer.parseInt(newValue[1]), Integer.parseInt(newValue[2]), Integer.parseInt(newValue[3]));
	}

	@Override
	public int getRedI() {
		return r;
	}

	@Override
	public void setRedI(int r) {
		ColorUtil.boundCheckI(r);
		this.r = r;
	}

	@Override
	public int getGreenI() {
		return g;
	}

	@Override
	public void setGreenI(int g) {
		ColorUtil.boundCheckI(g);
		this.g = g;
	}

	@Override
	public int getBlueI() {
		return b;
	}

	@Override
	public void setBlueI(int b) {
		ColorUtil.boundCheckI(b);
		this.b = b;
	}

	@Override
	public int getAlphaI() {
		return a;
	}

	@Override
	public void setAlphaI(int a) {
		ColorUtil.boundCheckI(a);
		this.a = a;
	}

	@Override
	public double getRedF() {
		return r / 255.0;
	}

	@Override
	public void setRedF(double r) {
		this.r = ColorUtil.toInt(r);
	}

	@Override
	public double getGreenF() {
		return g / 255.0;
	}

	@Override
	public void setGreenF(double g) {
		this.g = ColorUtil.toInt(g);
	}

	@Override
	public double getBlueF() {
		return b / 255.0;
	}

	@Override
	public void setBlueF(double b) {
		this.b = ColorUtil.toInt(b);
	}

	@Override
	public double getAlphaF() {
		return a / 255.0;
	}

	@Override
	public void setAlphaF(double a) {
		this.a = ColorUtil.toInt(a);
	}

	/** @return the colors as a string array formatted like so: {red, green, blue, alpha} */
	@Override
	@NotNull
	public String[] getAsStringArray() {
		String[] valuesAsArray = new String[4];
		valuesAsArray[0] = r + "";
		valuesAsArray[1] = g + "";
		valuesAsArray[2] = b + "";
		valuesAsArray[3] = a + "";
		return valuesAsArray;
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVColorIntArray(this.r, this.g, this.b, this.a);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Color;
	}

	/**
	 @return the color array into a String.
	 @see SVColor#toStringI(int, int, int, int)
	 */
	@NotNull
	public String toString() {
		return AColor.toStringI(r, g, b, a);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SVColor) {
			return AColor.isEqualTo(this, (SVColor) o);
		}
		return false;
	}

}
