package com.kaylerrenslow.armaDialogCreator.arma.util;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @see TextureParser
 @since 07/05/2017 */
public abstract class Texture {

	private final DataContext dataContext = new DataContext();
	protected final String format;
	protected final int width;
	protected final int height;
	protected final int numMips;

	public Texture(@NotNull String format, int width, int height, int numMips) {
		this.format = format;
		this.width = width;
		this.height = height;
		this.numMips = numMips;
	}

	@NotNull
	public String getFormat() {
		return format;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNumberOfMipMaps() {
		return numMips;
	}

	/** This can be used for anything. */
	@NotNull
	public DataContext getDataContext() {
		return dataContext;
	}

	public static class Color extends Texture {
		private final double r;
		private final double g;
		private final double b;
		private final double a;
		private final String textureType;

		public Color(@NotNull String format, int width, int height, int numMips,
					 double r, double g, double b, double a, @Nullable String textureType) {
			super(format, width, height, numMips);
			this.textureType = textureType;

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

		@Nullable
		public String getTextureType() {
			return textureType;
		}

		public String toString() {
			return String.format(
					"#(rgba,%d,%d)color(%f,%f,%f,%f%s)", width, height, r, g, b, a,
					textureType == null ? "" : "," + textureType
			);
		}
	}

	static class ColorI extends Color {

		public ColorI(@NotNull String format, int width, int height, int numMips,
					  int r, int g, int b, int a, @Nullable String textureType) {
			super(format, width, height, numMips, r / 255.0, g / 255.0, b / 255.0, a / 255.0, textureType);
		}

		public String toString() {
			int r = (int) Math.round(this.getR() * 255);
			int g = (int) Math.round(this.getG() * 255);
			int b = (int) Math.round(this.getB() * 255);
			int a = (int) Math.round(this.getA() * 255);
			return String.format(
					"#(rgba,%d,%d)colori(%d,%d,%d,%d%s)", width, height, r, g, b, a,
					getTextureType() == null ? "" : "," + getTextureType()
			);
		}
	}

	public static class PerlinNoise extends Texture {
		private final int xScale;
		private final int yScale;
		private final int min;
		private final int max;

		public PerlinNoise(@NotNull String format, int width, int height, int numMips,
						   int xScale, int yScale, int min, int max
		) {
			super(format, width, height, numMips);
			this.xScale = xScale;
			this.yScale = yScale;
			this.min = min;
			this.max = max;
		}

		@Override
		public String toString() {
			return String.format("#(%s,%d,%d)perlinNoise(%d,%d,%d,%d)",
					format, width, height, xScale, yScale, min, max);
		}
	}

	public static class Irradiance extends Texture {
		private final int specularPower;

		public Irradiance(@NotNull String format, int width, int height, int numMips, int specularPower) {
			super(format, width, height, numMips);
			this.specularPower = specularPower;
		}

		@Override
		public String toString() {
			return String.format("#(%s,%d,%d)irradiance(%d)", format, width, height, specularPower);
		}
	}

	public static class Fresnel extends Texture {
		private final double n;
		private final double k;

		public Fresnel(@NotNull String format, int width, int height, int numMips, double n, double k) {
			super(format, width, height, numMips);
			this.n = n;
			this.k = k;
		}

		@Override
		public String toString() {
			return String.format("#(%s,%d,%d)Fresnel(%f,%f)", format, width, height, n, k);
		}
	}

	public static class FresnelGlass extends Texture {

		public FresnelGlass(@NotNull String format, int width, int height, int numMips) {
			super(format, width, height, numMips);
		}

		@Override
		public String toString() {
			return String.format("#(%s,%d,%d)fresnelGlass()", format, width, height);
		}
	}

	public static class RenderToTexture extends Texture {

		private final String surfaceName;
		private final String type;
		private final double aspectRatio;

		public RenderToTexture(@NotNull String format, int width, int height, int numMips,
							   @NotNull String surfaceName, @NotNull String type, double aspectRatio
		) {
			super(format, width, height, numMips);
			this.surfaceName = surfaceName;
			this.type = type;
			this.aspectRatio = aspectRatio;
		}

		@Override
		public String toString() {
			return String.format(
					"#(%s,%d,%d)rendertotexture(%s,%s,%f)", format, width, height,
					surfaceName, type, aspectRatio
			);
		}
	}

	public static class TreeCrown extends Texture {

		private final double density;

		public TreeCrown(@NotNull String format, int width, int height, int numMips, double density) {
			super(format, width, height, numMips);
			this.density = density;
		}

		@Override
		public String toString() {
			return String.format("#(%s,%d,%d)treeCrown(%f)", format, width, height, density);
		}
	}

	public static class WaterIrradiance extends Texture {

		private final int specularPower;

		public WaterIrradiance(@NotNull String format, int width, int height, int numMips, int specularPower) {
			super(format, width, height, numMips);
			this.specularPower = specularPower;
		}

		@Override
		public String toString() {
			return String.format("#(%s,%d,%d)waterIrradiance(%d)", format, width, height, specularPower);
		}
	}
}
