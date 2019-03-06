package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

/**
 Defines a color constant that is immutable

 @author Kayler
 @since 3/5/2019. */
public class SVColorConstant extends SerializableValue implements SVColor {

	public static final SVColorConstant BLACK = new SVColorConstant(0, 0, 0, 255);
	public static final SVColorConstant RED = new SVColorConstant(255, 0, 0, 255);
	public static final SVColorConstant GREEN = new SVColorConstant(0, 255, 0, 255);
	public static final SVColorConstant BLUE = new SVColorConstant(0, 0, 255, 255);
	public static final SVColorConstant WHITE = new SVColorConstant(255, 255, 255, 255);
	public static final SVColorConstant TRANSPARENT = new SVColorConstant(0, 0, 0, 0);

	/** ARGB color */
	private final int argb;

	/**
	 Creates a color
	 */
	public SVColorConstant(int argb) {
		this.argb = argb;
	}

	/**
	 Creates a color

	 @param r red (range 0-1.0)
	 @param g green (range 0-1.0)
	 @param b blue (range 0-1.0)
	 @param a alpha (range 0-1.0)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1
	 */
	public SVColorConstant(double r, double g, double b, double a) {
		this.argb = ColorUtil.toARGB(r, g, b, a);
	}

	/**
	 Creates a color

	 @param r red (range 0-255)
	 @param g green (range 0-255)
	 @param b blue (range 0-255)
	 @param a alpha (range 0-255)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 255
	 */
	public SVColorConstant(int r, int g, int b, int a) {
		this.argb = ColorUtil.toARGB(r, g, b, a);
	}

	/**
	 Creates a color from a double array of length 4

	 @param rgba the color array that must have length=4
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1. Also throws it when rgba.length != 4
	 */
	public SVColorConstant(double[] rgba) {
		this.argb = ColorUtil.toARGB(rgba[0], rgba[1], rgba[2], rgba[3]);
	}

	/**
	 Create a new color from String array which contains one number (ARGB)

	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 4)
	 */
	public SVColorConstant(@NotNull String[] newValue) throws NumberFormatException, IndexOutOfBoundsException {
		this(Integer.parseInt(newValue[0]));
	}

	@Override
	public int getRedI() {
		return ColorUtil.ri(argb);
	}

	@Override
	public void setRedI(int r) {
	}

	@Override
	public int getGreenI() {
		return ColorUtil.gi(argb);
	}

	@Override
	public void setGreenI(int g) {
	}

	@Override
	public int getBlueI() {
		return ColorUtil.bi(argb);
	}

	@Override
	public void setBlueI(int b) {
	}

	@Override
	public int getAlphaI() {
		return ColorUtil.ai(argb);
	}

	@Override
	public void setAlphaI(int a) {
	}

	@Override
	public double getRedF() {
		return ColorUtil.rf(argb);
	}

	@Override
	public void setRedF(double r) {
	}

	@Override
	public double getGreenF() {
		return ColorUtil.gf(argb);
	}

	@Override
	public void setGreenF(double g) {
	}

	@Override
	public double getBlueF() {
		return ColorUtil.bf(argb);
	}

	@Override
	public void setBlueF(double b) {
	}

	@Override
	public double getAlphaF() {
		return ColorUtil.af(argb);
	}

	@Override
	public void setAlphaF(double a) {
	}

	@Override
	public int toARGB() {
		return argb;
	}

	/** @return the colors as a string array formatted like so: {ARGB} */
	@Override
	@NotNull
	public String[] getAsStringArray() {
		return new String[]{argb + ""};
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVColorConstant(this.argb);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Color;
	}

	/**
	 @return the color ARGB int
	 */
	@NotNull
	public String toString() {
		return argb + "";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SVColor) {
			return SVColor.isEqualTo(this, (SVColor) o);
		}
		return false;
	}

}
