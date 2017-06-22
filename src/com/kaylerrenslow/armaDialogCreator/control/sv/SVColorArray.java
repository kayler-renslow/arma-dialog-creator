package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 Defines a color as an array.

 @author Kayler
 @since 05/22/2016. */
public class SVColorArray extends SerializableValue implements SVColor {
	private static final DecimalFormat format = new DecimalFormat("#.####");

	public static final ValueConverter<SVColorArray> CONVERTER = new ValueConverter<SVColorArray>() {
		@Override
		public SVColorArray convert(DataContext context, @NotNull String... values) {
			return new SVColorArray(values);
		}
	};

	/** cache the javaFX color */
	private Color javafxColor;
	private boolean updateJavafxColor = false;

	/** Color array where each value is ranged from 0.0 - 1.0 inclusively. Format=[r,g,b,a] */
	protected double[] color = new double[4];


	/**
	 Creates a color

	 @param r red (range 0-1.0)
	 @param g green (range 0-1.0)
	 @param b blue (range 0-1.0)
	 @param a alpha (range 0-1.0)
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1
	 */
	public SVColorArray(double r, double g, double b, double a) {
		super(new String[]{format.format(r), format.format(g), format.format(b), format.format(a)});
		setRed(r);
		setGreen(g);
		setBlue(b);
		setAlpha(a);
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
		this(r / 255.0, g / 255.0, b / 255.0, a / 255.0);
	}

	/**
	 Creates a color from a double array of length 4

	 @param c the color array that must have length=4
	 @throws IllegalArgumentException when r,g,b, or a are less than 0 or greater than 1. Also throws it when c.length != 4
	 */
	public SVColorArray(double[] c) {
		this(0, 0, 0, 0);
		setColor(c);
	}

	/** Set the color from a JavaFX Color instance */
	public SVColorArray(@NotNull Color newValue) {
		this(newValue.getRed(), newValue.getGreen(), newValue.getBlue(), newValue.getOpacity());
		this.javafxColor = newValue;
		updateJavafxColor = false;
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

	private void boundCheck(double c) {
		if (c < 0.0 || c > 1.0) {
			throw new IllegalArgumentException("Color value is out of range (must be >=0 and <=1): " + c);
		}
	}

	@Override
	public double getRed() {
		return color[0];
	}

	@Override
	public void setRed(double r) {
		boundCheck(r);
		color[0] = r;
		updateJavafxColor = true;
	}

	@Override
	public double getGreen() {
		return color[1];
	}

	@Override
	public void setGreen(double g) {
		boundCheck(g);
		color[1] = g;
		updateJavafxColor = g != color[1];
	}

	@Override
	public double getBlue() {
		return color[2];
	}

	@Override
	public void setBlue(double b) {
		boundCheck(b);
		updateJavafxColor = b != color[2];
		color[2] = b;
	}

	@Override
	public double getAlpha() {
		return color[3];
	}

	@Override
	public void setAlpha(double a) {
		boundCheck(a);
		color[3] = a;
		updateJavafxColor = a != color[3];
	}

	@Override
	public void setColor(double[] c) {
		if (c.length != 4) {
			throw new IllegalArgumentException("array length must be 4");
		}
		setRed(c[0]);
		setGreen(c[1]);
		setBlue(c[2]);
		setAlpha(c[3]);
	}

	/** @return the colors as a string array formatted like so: {red, green, blue, alpha} */
	@Override
	@NotNull
	public String[] getAsStringArray() {
		valuesAsArray[0] = format.format(color[0]);
		valuesAsArray[1] = format.format(color[1]);
		valuesAsArray[2] = format.format(color[2]);
		valuesAsArray[3] = format.format(color[3]);
		return valuesAsArray;
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		double[] copy = new double[color.length];
		System.arraycopy(color, 0, copy, 0, copy.length);
		return new SVColorArray(copy);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Color;
	}

	/**
	 @return the color array into a String.
	 Example: 0 red, 0.1 green, 0.2 blue, 0.3 alpha becomes "{0.0,0.1,0.2,0.3}"
	 */
	@NotNull
	public String toString() {
		return "{" + format.format(color[0]) + "," + format.format(color[1]) + "," + format.format(color[2]) + "," + format.format(color[3]) + "}";
	}

	/** Convert this color into a JavaFX color */
	@NotNull
	public Color toJavaFXColor() {
		if (updateJavafxColor || javafxColor == null) {
			updateJavafxColor = false;
			javafxColor = Color.color(getRed(), getGreen(), getBlue(), getAlpha());
		}
		return javafxColor;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVColorArray) {
			SVColorArray other = (SVColorArray) o;
			return Arrays.equals(this.color, other.color);
		}
		return false;
	}

}
