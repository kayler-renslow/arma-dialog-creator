package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.AColor;
import com.armadialogcreator.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

/**
 Defines a color as an array with a single integer value in format ARGB.

 @author Kayler
 @since 02/16/2019. */
public class SVColorInt extends SerializableValue implements SVColor {


	public static final StringArrayConverter<SVColorInt> CONVERTER = new StringArrayConverter<SVColorInt>() {
		@Override
		public SVColorInt convert(@NotNull Env env, @NotNull String[] values) throws Exception {
			return new SVColorInt(values);
		}
	};

	/** ARGB color */
	private int argb;

	/**
	 Creates a color
	 */
	public SVColorInt(int argb) {
		setColorARGB(argb);
	}

	/**
	 Creates a color

	 @param r red (range 0-1.0)
	 @param g green (range 0-1.0)
	 @param b blue (range 0-1.0)
	 @param a alpha (range 0-1.0)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1
	 */
	public SVColorInt(double r, double g, double b, double a) {
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
	public SVColorInt(int r, int g, int b, int a) {
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
	public SVColorInt(double[] rgba) {
		this(0, 0, 0, 0);
		setColorF(rgba);
	}

	/**
	 Create a new color from String array which contains one number (ARGB)

	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 4)
	 */
	public SVColorInt(@NotNull String[] newValue) throws NumberFormatException, IndexOutOfBoundsException {
		this(Integer.parseInt(newValue[0]));
	}

	@Override
	public int getRedI() {
		return ColorUtil.ri(argb);
	}

	@Override
	public void setRedI(int r) {
		ColorUtil.boundCheckI(r);
		this.argb = ColorUtil.toARGB(r, ColorUtil.gi(argb), ColorUtil.bi(argb), ColorUtil.ai(argb));
	}

	@Override
	public int getGreenI() {
		return ColorUtil.gi(argb);
	}

	@Override
	public void setGreenI(int g) {
		ColorUtil.boundCheckI(g);
		this.argb = ColorUtil.toARGB(ColorUtil.ri(argb), g, ColorUtil.bi(argb), ColorUtil.ai(argb));
	}

	@Override
	public int getBlueI() {
		return ColorUtil.bi(argb);
	}

	@Override
	public void setBlueI(int b) {
		ColorUtil.boundCheckI(b);
		this.argb = ColorUtil.toARGB(ColorUtil.ri(argb), ColorUtil.gi(argb), b, ColorUtil.ai(argb));
	}

	@Override
	public int getAlphaI() {
		return ColorUtil.ai(argb);
	}

	@Override
	public void setAlphaI(int a) {
		ColorUtil.boundCheckI(a);
		this.argb = ColorUtil.toARGB(ColorUtil.ri(argb), ColorUtil.gi(argb), ColorUtil.bi(argb), a);
	}

	@Override
	public double getRedF() {
		return ColorUtil.rf(argb);
	}

	@Override
	public void setRedF(double r) {
		setRedI(ColorUtil.toInt(r));
	}

	@Override
	public double getGreenF() {
		return ColorUtil.gf(argb);
	}

	@Override
	public void setGreenF(double g) {
		setGreenI(ColorUtil.toInt(g));
	}

	@Override
	public double getBlueF() {
		return ColorUtil.bf(argb);
	}

	@Override
	public void setBlueF(double b) {
		setBlueI(ColorUtil.toInt(b));
	}

	@Override
	public double getAlphaF() {
		return ColorUtil.af(argb);
	}

	@Override
	public void setAlphaF(double a) {
		setAlphaI(ColorUtil.toInt(a));
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
		return new SVColorInt(this.argb);
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
			return AColor.isEqualTo(this, (SVColor) o);
		}
		return false;
	}

}
