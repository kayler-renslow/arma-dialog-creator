package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 Defines a color as an array.

 @author Kayler
 @since 05/22/2016. */
public class SVColorArray extends SerializableValue implements SVColor {

	private static final double EPSILON = 0.0001;

	/** @return the rounded result that has decimal places count equal to what {@link #format} would output */
	public static double round(double d) {
		return Math.round(d * 10000) / 10000.0;
	}

	/** @return if the doubles are equal to one another */
	public static boolean equalTo(double d1, double d2) {
		return d1 == d2 || Math.abs(d1 - d2) < EPSILON;
	}

	public static final DecimalFormat format = new DecimalFormat("#.####");

	static {
		//This is to ensure that numbers use periods instead of commas for decimals and use commas for thousands place.
		//Also, this is being initialized here because DECIMAL_FORMAT is static and may initialize before some testing methods
		format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
	}

	public static final ValueConverter<SVColorArray> CONVERTER = new ValueConverter<SVColorArray>() {
		@Override
		public SVColorArray convert(DataContext context, @NotNull String... values) {
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
		return r;
	}

	@Override
	public void setRed(double r) {
		boundCheck(r);
		this.r = r;
	}

	@Override
	public double getGreen() {
		return g;
	}

	@Override
	public void setGreen(double g) {
		boundCheck(g);
		this.g = g;
	}

	@Override
	public double getBlue() {
		return b;
	}

	@Override
	public void setBlue(double b) {
		boundCheck(b);
		this.b = b;
	}

	@Override
	public double getAlpha() {
		return a;
	}

	@Override
	public void setAlpha(double a) {
		boundCheck(a);
		this.a = a;
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
	 Example: 0 red, 0.1 green, 0.2 blue, 0.3 alpha becomes "{0.0,0.1,0.2,0.3}"
	 */
	@NotNull
	public String toString() {
		return toString(r, g, b, a);
	}

	/**
	 @return the given colors into a String.
	 Example: 0 red, 0.1 green, 0.2 blue, 0.3 alpha becomes "{0.0, 0.1, 0.2, 0.3}"
	 */
	@NotNull
	public static String toString(double red, double green, double blue, double alpha) {
		return "{" + format.format(red) + ", "
				+ format.format(green) + ", "
				+ format.format(blue) + ", "
				+ format.format(alpha) + "}";
	}

	/** Convert this color into a JavaFX color */
	@NotNull
	public Color toJavaFXColor() {
		return Color.color(getRed(), getGreen(), getBlue(), getAlpha());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVColorArray) {
			SVColorArray other = (SVColorArray) o;
			return ArmaPrecision.isEqualTo(this.r, other.r)
					&& ArmaPrecision.isEqualTo(this.g, other.g)
					&& ArmaPrecision.isEqualTo(this.b, other.b)
					&& ArmaPrecision.isEqualTo(this.a, other.a);
		}
		return false;
	}

}
