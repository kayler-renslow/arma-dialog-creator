package com.kaylerrenslow.armaDialogCreator.control.sv;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Defines a color.

 @author Kayler
 @since 05/22/2016. */
public interface SVColor {


	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getRed();

	/**
	 Set red.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setRed(double r);

	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getGreen();

	/**
	 Set green.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setGreen(double g);

	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getBlue();

	/**
	 Set blue.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setBlue(double b);

	/**
	 @return 0-1.0 inclusively (1.0 being max value (255/255))
	 */
	double getAlpha();

	/**
	 Set alpha.

	 @throws IllegalArgumentException when value is less than 0 or greater than 1
	 */
	void setAlpha(double a);

	/** Takes out the color values 1 by 1 and injects them into this instances' color array. */
	void setColor(double[] c);


	/**
	 Takes a serialized color array String and converts it into a double array. Example (both curly braces and square brackets are allowed): "{0.0,0.1,0.2,0.3}"  becomes  0 red, 0.1 green, 0.2 blue, 0.3 alpha

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
	@NotNull Color toJavaFXColor();


	/**
	 Create a new JavaFX Color from String array that is formatted like so:
	 {r,g,b,a} where r,g,b,a are between 0.0 and 1.0 inclusively

	 @throws NumberFormatException     when the string array is not formatted correctly
	 @throws IndexOutOfBoundsException when string array is not of proper size (must be length 4)
	 */
	@NotNull
	static Color toJavaFXColor(@NotNull String[] newValue) throws NumberFormatException {
		return Color.color(Double.parseDouble(newValue[0]), Double.parseDouble(newValue[1]), Double.parseDouble(newValue[2]), Double.parseDouble(newValue[3]));
	}


	/**
	 Gets color array (formatted: [r,g,b,a])

	 @param arr stores values in given array (array must be length 4)
	 */
	static double[] getColorArray(double[] arr, int color) {
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

	/** Returns what {@link #getColorArray(double[], int)} would, with new array of length 4 */
	static double[] getColorArray(int color) {
		return getColorArray(new double[4], color);
	}

	static int to32BitInteger(int red, int green, int blue, int alpha) {
		int i = red;
		i = i << 8;
		i = i | green;
		i = i << 8;
		i = i | blue;
		i = i << 8;
		i = i | alpha;
		return i;
	}

	static int to32BitInteger(@NotNull Color color) {
		int r = (int) Math.round(color.getRed() * 255.0);
		int g = (int) Math.round(color.getGreen() * 255.0);
		int b = (int) Math.round(color.getBlue() * 255.0);
		int a = (int) Math.round(color.getOpacity() * 255.0);
		return to32BitInteger(r, g, b, a);
	}
}
