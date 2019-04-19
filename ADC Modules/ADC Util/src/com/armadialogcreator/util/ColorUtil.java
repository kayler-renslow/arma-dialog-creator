package com.armadialogcreator.util;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/16/2019 */
public class ColorUtil {
	public static int multiplyAlphaARGB(int argb, double value) {
		return toARGB(ri(argb), gi(argb), bi(argb), (int) (ai(argb) * value));
	}

	/** @return the integer rgb value based upon int values ranged 0-255 */
	public static int toRGB(int r, int g, int b) {
		boundCheckI(r);
		boundCheckI(g);
		boundCheckI(b);
		return (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}

	/** @return the integer rgb value based upon floats ranged 0-1.0 */
	public static int toRGB(double r, double g, double b) {
		boundCheckF(r);
		boundCheckF(g);
		boundCheckF(b);
		final double f = 255.0;
		int R = (int) (r * f);
		int G = (int) (g * f);
		int B = (int) (b * f);
		return (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
	}

	/** @return the integer argb value based upon int values ranged 0-255 */
	public static int toARGB(int r, int g, int b, int a) {
		boundCheckI(r);
		boundCheckI(g);
		boundCheckI(b);
		boundCheckI(a);
		return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}

	/** @return the integer argb value based upon floats ranged 0-1.0 */
	public static int toARGB(double r, double g, double b, double a) {
		boundCheckF(r);
		boundCheckF(g);
		boundCheckF(b);
		boundCheckF(a);
		final double f = 255.0;
		int R = (int) (r * f);
		int G = (int) (g * f);
		int B = (int) (b * f);
		int A = (int) (a * f);
		return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
	}

	public static int toInt(double color) {
		boundCheckF(color);
		return (int) (color * 255.0);
	}

	public static double toDouble(int color) {
		boundCheckI(color);
		return color / 255.0;
	}

	public static int toARGB(@NotNull Color color) {
		return toARGB(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
	}

	@NotNull
	public static Color toColor(int argb) {
		return Color.color(rf(argb), gf(argb), bf(argb), af(argb));
	}

	@NotNull
	public static Color toColor(@NotNull AColor color) {
		return Color.color(color.getRedF(), color.getGreenF(), color.getBlueF(), color.getAlphaF());
	}

	public static int ri(int argb) {
		return (argb >> 16) & 0xFF;
	}

	public static int gi(int argb) {
		return (argb >> 8) & 0xFF;
	}

	public static int bi(int argb) {
		return (argb) & 0xFF;
	}

	public static int ai(int argb) {
		return (argb >> 24) & 0xFF;
	}

	public static double rf(int argb) {
		int r = (argb >> 16) & 0xFF;
		final double f = 255.0;

		return r / f;
	}

	public static double gf(int argb) {
		int g = (argb >> 8) & 0xFF;
		final double f = 255.0;
		return g / f;
	}

	public static double bf(int argb) {
		int b = (argb) & 0xFF;
		final double f = 255.0;
		return b / f;
	}

	public static double af(int argb) {
		int a = (argb >> 24) & 0xFF;
		final double f = 255.0;
		return a / f;
	}

	public static void boundCheckF(double color) {
		if (color < 0 || color > 1.0) {
			throw new IllegalArgumentException("Color value is out of range (must be >=0 and <=1.0): " + color);
		}
	}

	public static void boundCheckI(int color) {
		if (color < 0 || color > 255) {
			throw new IllegalArgumentException("Color value is out of range (must be >=0 and <=255): " + color);
		}
	}

	@NotNull
	public static Color toColor(int r, int g, int b) {
		return Color.color(r / 255.0, g / 255.0, b / 255.0);
	}

	@NotNull
	public static String toHex(@NotNull Color color) {
		int rgb = ColorUtil.toRGB(color.getRed(), color.getGreen(), color.getBlue());
		String h = Integer.toHexString(rgb);
		if (h.length() < 6) {
			h = "000000".substring(0, 6 - h.length()) + h;
		}
		return "#" + h;
	}

	public static int interpolate(int startARGB, int endARGB, double ratio) {
		if (ratio <= 0) {
			return startARGB;
		}
		if (ratio >= 1) {
			return endARGB;
		}

		int r1 = ri(startARGB);
		int b1 = bi(startARGB);
		int g1 = gi(startARGB);
		int a1 = ai(startARGB);

		int r2 = ri(endARGB);
		int b2 = bi(endARGB);
		int g2 = gi(endARGB);
		int a2 = ai(endARGB);

		return toARGB(
				r1 + (r2 - r1) * ratio,
				g1 + (g2 - g1) * ratio,
				b1 + (b2 - b1) * ratio,
				a1 + (a2 - a1) * ratio
		);
	}

	public static int opaqueARGB(int argb) {
		return ColorUtil.toARGB(ri(argb), gi(argb), bi(argb), 255);
	}

	public static int darken(int argb) {
		int r = (int) (ri(argb) / 4.0 * 3);
		int g = (int) (gi(argb) / 4.0 * 3);
		int b = (int) (bi(argb) / 4.0 * 3);
		return ColorUtil.toARGB(r, g, b, ai(argb));
	}

	@NotNull
	public static Color opaque(@NotNull Color color) {
		return Color.color(color.getRed(), color.getGreen(), color.getBlue(), 1);
	}
}
