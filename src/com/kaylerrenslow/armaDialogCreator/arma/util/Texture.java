package com.kaylerrenslow.armaDialogCreator.arma.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/05/2017 */
public interface Texture {
	@NotNull String getFormat();

	int getWidth();

	int getHeight();

	int getNumberOfMipMaps();

	class Color implements Texture {
		private final String format;
		private final int width;
		private final int height;
		private final int numMips;
		private final double r;
		private final double g;
		private final double b;
		private final double a;

		public Color(@NotNull String format, int width, int height, int numMips,
					 double r, double g, double b, double a) {
			this.format = format;
			this.width = width;
			this.height = height;
			this.numMips = numMips;

			if (r < 0 || r > 1) {
				throw new IllegalArgumentException("r is out of bounds");
			}
			if (g < 0 || g > 1) {
				throw new IllegalArgumentException("g is out of bounds");
			}
			if (b < 0 || b > 1) {
				throw new IllegalArgumentException("b is out of bounds");
			}
			if (a < 0 || a > 1) {
				throw new IllegalArgumentException("a is out of bounds");
			}

			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}

		@Override
		@NotNull
		public String getFormat() {
			return format;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public int getNumberOfMipMaps() {
			return numMips;
		}

		public double getR() {
			return r;
		}

		public double getG() {
			return g;
		}

		public double getB() {
			return b;
		}

		public double getA() {
			return a;
		}
	}

	class ColorI extends Color {

		public ColorI(@NotNull String format, int width, int height, int numMips,
					  int r, int g, int b, int a) {
			super(format, width, height, numMips, r / 255.0, g / 255.0, b / 255.0, a / 255.0);
		}

	}
}
