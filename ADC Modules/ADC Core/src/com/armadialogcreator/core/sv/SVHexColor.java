package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.AColor;
import com.armadialogcreator.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

/**
 Class used to depict a hex color string.

 @author Kayler
 @since 05/23/2016. */
public class SVHexColor extends SerializableValue implements SVColor {

	public static final StringArrayConverter<SVHexColor> CONVERTER = new StringArrayConverter<SVHexColor>() {
		@Override
		public SVHexColor convert(@NotNull Env env, @NotNull String[] values) throws Exception {
			return new SVHexColor(values[0]);
		}
	};

	/** Colors where each value is ranged from 0 - 255 inclusively. Format=[r,g,b] */
	private int r, g, b;
	private String hex;

	/**
	 Creates a HexColor object based off a hex string (#fffff for example)

	 @throws IllegalArgumentException for when the hex color is formatted wrong
	 */
	public SVHexColor(@NotNull String hex) {
		double[] colorArray = new double[4];
		getColorArray(colorArray, hex);
		setColorF(colorArray);
	}

	public SVHexColor(int argb) {
		setColorARGB(argb);
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
	 Gets rgbColor array. Ignores alpha

	 @param arr stores values in given array (array must be length 3) (r,g,b).
	 This array is modified directly and is returned. Each value at each index will be randed 0-1
	 @param rgbColor the rgb color as an integer
	 @return the array
	 @throws IllegalArgumentException when the array length < 3
	 */
	public static double[] getColorArray(double[] arr, int rgbColor) {
		if (arr.length < 3) {
			throw new IllegalArgumentException("arr.length < 3");
		}
		arr[0] = ColorUtil.af(rgbColor);
		arr[1] = ColorUtil.gf(rgbColor);
		arr[2] = ColorUtil.bf(rgbColor);
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
		if (o instanceof SVColor) {
			return AColor.isEqualTo(this, (SVColor) o);
		}
		return false;
	}

	@Override
	public int getRedI() {
		return r;
	}

	@Override
	public void setRedI(int r) {
		ColorUtil.boundCheckI(r);
		this.r = r;
		updateHex();
	}

	@Override
	public int getGreenI() {
		return g;
	}

	@Override
	public void setGreenI(int g) {
		ColorUtil.boundCheckI(g);
		this.g = g;
		updateHex();
	}

	@Override
	public int getBlueI() {
		return b;
	}

	@Override
	public void setBlueI(int b) {
		ColorUtil.boundCheckI(b);
		this.b = b;
		updateHex();
	}

	@Override
	public int getAlphaI() {
		return 255;
	}

	@Override
	public void setAlphaI(int a) {
		//do nothing
	}

	@Override
	public double getRedF() {
		return r;
	}

	@Override
	public void setRedF(double r) {
		this.r = ColorUtil.toInt(r);
		updateHex();
	}

	@Override
	public double getGreenF() {
		return g;
	}

	@Override
	public void setGreenF(double g) {
		this.g = ColorUtil.toInt(g);
		updateHex();
	}

	@Override
	public double getBlueF() {
		return b;
	}

	@Override
	public void setBlueF(double b) {
		this.b = ColorUtil.toInt(b);
		updateHex();
	}

	@Override
	public double getAlphaF() {
		return 1.0;
	}

	@Override
	public void setAlphaF(double a) {
		//do nothing
	}

	private void updateHex() {
		int argb = ColorUtil.toRGB(r, g, b);
		String h = Integer.toHexString(argb);
		if (h.length() < 6) {
			h = "000000".substring(0, 6 - h.length()) + h;
		}
		hex = "#" + h;
	}
}
