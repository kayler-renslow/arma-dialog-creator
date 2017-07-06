package com.kaylerrenslow.armaDialogCreator.arma.util;

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
				"#\\(([a-zA-Z]+),(\\d+),(\\d+),(\\d+)\\)([a-zA-Z]+)\\(.*?\\)"
		);
		Matcher m = p.matcher(textureString);

		if (!m.find()) {
			error();
		}

		try {
			String format = m.group(1);
			int width = Integer.parseInt(m.group(2));
			int height = Integer.parseInt(m.group(3));
			int numMips = Integer.parseInt(m.group(4));

			String textureType = m.group(5);
			String[] args = m.group(6).split(",");
			switch (textureType) {
				case "perlinNoise": {
					if (args.length != 4) {
						error();
					}
					//fallthrough
				}
				case "irradiance": {
					if (args.length != 1) {
						error();
					}
					//fallthrough
				}
				case "Fresnel": {
					if (args.length != 2) {
						error();
					}
					//fallthrough
				}
				case "fresnelGlass": {
					if (args.length != 0) {
						error();
					}
					//fallthrough
				}
				case "rendertotexture": {
					if (args.length != 3) {
						error();
					}
					//fallthrough
				}
				case "treeCrown": {
					if (args.length != 1) {
						error();
					}
					//fallthrough
				}
				case "waterIrradiance": {
					if (args.length != 1) {
						error();
					}
					//returns a pink color
					return new Texture.Color(format, width, height, numMips, 1.0, 0.7529412, 0.79607844, 1);
				}
				case "color": {
					double r = Double.parseDouble(args[0]);
					double g = Double.parseDouble(args[1]);
					double b = Double.parseDouble(args[2]);
					double a = Double.parseDouble(args[3]);
					return new Texture.Color(format, width, height, numMips, r, g, b, a);
				}
				case "colori": {
					int r = Integer.parseInt(args[0]);
					int g = Integer.parseInt(args[1]);
					int b = Integer.parseInt(args[2]);
					int a = Integer.parseInt(args[3]);
					return new Texture.ColorI(format, width, height, numMips, r, g, b, a);
				}
				default: {
					error();
				}
			}
		} catch (Exception e) {
			error();
		}

		error();
		return null;
	}

	private void error() {
		throw new IllegalArgumentException("Couldn't parse \"" + textureString + "\"");
	}

}
