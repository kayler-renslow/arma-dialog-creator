package com.armadialogcreator.core.sv;

import com.armadialogcreator.util.ArmaPrecision;
import com.armadialogcreator.util.ColorUtil;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 Defines a color.

 @author Kayler
 @since 05/22/2016. */
public interface SVColor {
	/**
	 @return 0-255 inclusively
	 */
	int getRedI();

	/**
	 Set red.

	 @throws IllegalArgumentException when value is less than 0 or greater than 255
	 */
	void setRedI(int r);

	/**
	 @return 0-255 inclusively
	 */
	int getGreenI();

	/**
	 Set green.

	 @throws IllegalArgumentException when value is less than 0 or greater than 255
	 */
	void setGreenI(int g);

	/**
	 @return 0-255 inclusively
	 */
	int getBlueI();

	/**
	 Set blue.

	 @throws IllegalArgumentException when value is less than 0 or greater than 255
	 */
	void setBlueI(int b);

	/**
	 @return 0-255 inclusively
	 */
	int getAlphaI();

	/**
	 Set alpha.

	 @throws IllegalArgumentException when value is less than 0 or greater than 255
	 */
	void setAlphaI(int a);

	/** Takes out the color values 1 by 1 and injects them into this instances' color array. */
	default void setColorI(int[] rgba) {
		if (rgba.length < 4) {
			throw new IllegalArgumentException();
		}
		setRedI(rgba[0]);
		setGreenI(rgba[1]);
		setBlueI(rgba[2]);
		setAlphaI(rgba[3]);
	}

	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getRedF();

	/**
	 Set red.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setRedF(double r);

	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getGreenF();

	/**
	 Set green.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setGreenF(double g);

	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getBlueF();

	/**
	 Set blue.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setBlueF(double b);

	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getAlphaF();

	/**
	 Set alpha.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setAlphaF(double a);

	/** Takes out the color values 1 by 1 and injects them into this instances' color array. */
	default void setColorF(double[] rgba) {
		if (rgba.length < 4) {
			throw new IllegalArgumentException();
		}
		setRedF(rgba[0]);
		setGreenF(rgba[1]);
		setBlueF(rgba[2]);
		setAlphaF(rgba[3]);
	}

	default void setColorARGB(int argb) {
		setRedI(ColorUtil.ri(argb));
		setGreenI(ColorUtil.gi(argb));
		setBlueI(ColorUtil.bi(argb));
		setAlphaI(ColorUtil.ai(argb));
	}

	/**
	 Takes a serialized color array String and converts it into a double array.
	 Example (both curly braces and square brackets are allowed):
	 "{0.0,0.1,0.2,0.3}"  becomes  0 red, 0.1 green, 0.2 blue, 0.3 alpha

	 @return null when string is improperly formatted, otherwise will return a double array of dimension 4
	 */
	@Nullable
	static double[] arrayFromText(@NotNull String colorAsArray) {
		try {
			String[] split = colorAsArray.replaceAll("\\[|\\]|\\{|\\}", "").split(",");

			double[] d = new double[4];
			for (int i = 0; i < d.length; i++) {
				d[i] = Double.parseDouble(split[i].trim());
				if (d[i] < 0 || d[i] > 1) {
					return null;
				}
			}
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	/** Convert this color into a JavaFX color */
	@NotNull
	default Color toJavaFXColor() {
		return Color.color(getRedF(), getGreenF(), getBlueF());
	}

	/** @return this color as a ARGB int value */
	default int toARGB() {
		return ColorUtil.toARGB(getRedF(), getGreenF(), getBlueF(), getAlphaF());
	}

	/**
	 Create a new JavaFX Color from String array that is formatted like so:
	 {r,g,b,a} where r,g,b,a are between 0.0 and 1.0 inclusively

	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 4)
	 */
	@NotNull
	static Color toJavaFXColorF(@NotNull String[] newValue) throws NumberFormatException {
		return Color.color(Double.parseDouble(newValue[0]), Double.parseDouble(newValue[1]), Double.parseDouble(newValue[2]), Double.parseDouble(newValue[3]));
	}


	/**
	 Gets color array (formatted: [r,g,b,a])

	 @param arr stores values in given array (array must be length 4)
	 @param argb color in ARGB integer form
	 */
	static double[] getArgbColorArray(double[] arr, int argb) {
		if (arr.length != 4) {
			throw new IllegalArgumentException("arr.length != 4");
		}
		int a = (argb >> 24) & 0xFF;
		int b = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int r = (argb) & 0xFF;
		final double f = 255.0;
		arr[0] = r / f;
		arr[1] = g / f;
		arr[2] = b / f;
		arr[3] = a / f;
		return arr;
	}

	/** Returns what {@link #getArgbColorArray(double[], int)} would, with new array of length 4 */
	static double[] getArgbColorArray(int argb) {
		return getArgbColorArray(new double[4], argb);
	}

	DecimalFormat format = new DecimalFormat("#.####",
			//This is to ensure that numbers use periods instead of commas for decimals and use commas for thousands place.
			//Also, this is being initialized here because DECIMAL_FORMAT is static and may initialize before some testing methods
			DecimalFormatSymbols.getInstance(Locale.US
			)
	);

	/**
	 @return the given colors into a String.
	 Example: 0 red, 0.1 green, 0.2 blue, 0.3 alpha becomes "{0.0, 0.1, 0.2, 0.3}"
	 */
	@NotNull
	static String toStringF(double red, double green, double blue, double alpha) {
		return "{" + format.format(red) + ", "
				+ format.format(green) + ", "
				+ format.format(blue) + ", "
				+ format.format(alpha) + "}";
	}

	/**
	 @return the given colors into a String.
	 Example: 0 red, 5 green, 50 blue, 255 alpha becomes "{0, 5, 50, 255}"
	 */
	@NotNull
	static String toStringI(int red, int green, int blue, int alpha) {
		return "{" + red + ", "
				+ green + ", "
				+ blue + ", "
				+ alpha + "}";
	}

	static boolean isEqualTo(@NotNull SVColor first, @NotNull SVColor second) {
		if (first == second) {
			return true;
		}
		return ArmaPrecision.isEqualTo(first.getRedF(), (second).getRedF())
				&& ArmaPrecision.isEqualTo(first.getGreenF(), second.getGreenF())
				&& ArmaPrecision.isEqualTo(first.getBlueF(), second.getBlueF())
				&& ArmaPrecision.isEqualTo(first.getAlphaF(), second.getAlphaF());
	}

}
