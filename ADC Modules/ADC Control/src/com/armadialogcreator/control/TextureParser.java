package com.armadialogcreator.control;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Kayler
 @since 07/05/2017 */
public class TextureParser {
	private final String textureString;

	public TextureParser(@NotNull String textureString) {
		this.textureString = textureString;
	}

	@NotNull
	public String getTextureString() {
		return textureString;
	}

	/**
	 Parses the texture.

	 @return the {@link Texture} instance
	 @throws IllegalArgumentException if the texture couldn't be parsed
	 */
	@NotNull
	public Texture parse() {
		Pattern p = Pattern.compile(
				"#\\(([a-zA-Z]+),(\\d+),(\\d+),(\\d+)\\)([a-zA-Z]+)\\((.*?)\\)"
		);
		Matcher m = p.matcher(textureString);

		if (!m.find()) {
			error(null);
		}

		try {
			String format = m.group(1);
			int width = Integer.parseInt(m.group(2));
			int height = Integer.parseInt(m.group(3));
			int numMips = Integer.parseInt(m.group(4));

			String textureName = m.group(5);
			String[] args = m.group(6).split(",");
			switch (textureName) {
				case "perlinNoise": {
					int xScale = Integer.parseInt(args[0]);
					int yScale = Integer.parseInt(args[1]);
					int min = Integer.parseInt(args[2]);
					int max = Integer.parseInt(args[3]);
					return new Texture.PerlinNoise(format, width, height, numMips,
							xScale, yScale, min, max
					);
				}
				case "irradiance": {
					int specPower = Integer.parseInt(args[0]);
					return new Texture.Irradiance(format, width, height, numMips, specPower);
				}
				case "Fresnel": {
					double n = Double.parseDouble(args[0]);
					double k = Double.parseDouble(args[1]);
					return new Texture.Fresnel(format, width, height, numMips, n, k);
				}
				case "fresnelGlass": {
					if (args.length != 0) {
						error(null);
					}
					return new Texture.FresnelGlass(format, width, height, numMips);
				}
				case "r2t": {
					String surfaceName = args[0];
					double aspectRatio = Double.parseDouble(args[1]);
					return new Texture.RenderToTexture(format, width, height, numMips, surfaceName, aspectRatio);
				}
				case "treeCrown": {
					double density = Double.parseDouble(args[0]);
					return new Texture.TreeCrown(format, width, height, numMips, density);
				}
				case "waterIrradiance": {
					int specPower = Integer.parseInt(args[0]);
					return new Texture.WaterIrradiance(format, width, height, numMips, specPower);

				}
				case "color": {
					String textureType = null;
					if (args.length > 4) {
						textureType = args[5];
					}

					if (format.equals("rgb") || format.equals("argb")) {
						double r = Double.parseDouble(args[0]);
						double g = Double.parseDouble(args[1]);
						double b = Double.parseDouble(args[2]);
						double a = Double.parseDouble(args[3]);
						return new Texture.Color(format, width, height, numMips, r, g, b, a, textureType);
					} else {
						error(null);
					}
				}
				case "colori": {
					String textureType = null;
					if (args.length > 4) {
						textureType = args[5];
					}
					if (format.equals("rgb")) {
						int r = Integer.parseInt(args[0]);
						int g = Integer.parseInt(args[1]);
						int b = Integer.parseInt(args[2]);
						int a = Integer.parseInt(args[3]);
						return new Texture.ColorI(format, width, height, numMips, r, g, b, a, textureType);
					} else if (format.equals("argb")) {
						int a = Integer.parseInt(args[0]);
						int r = Integer.parseInt(args[1]);
						int g = Integer.parseInt(args[2]);
						int b = Integer.parseInt(args[3]);
						return new Texture.ColorI(format, width, height, numMips, r, g, b, a, textureType);
					} else {
						error(null);
					}

				}
				default: {
					error(null);
				}
			}
		} catch (Exception e) {
			error(e);
		}

		error(null);
		return null;
	}

	private void error(Exception e) {
		throw new IllegalArgumentException("Couldn't parse \"" + textureString + "\"", e);
	}

}
