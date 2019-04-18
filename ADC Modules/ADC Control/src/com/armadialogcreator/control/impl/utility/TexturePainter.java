package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.Texture;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/05/2017 */
public class TexturePainter {
	/**
	 paints the given texture from area x1,y1 to x2,y2

	 @param g graphics
	 @param texture texture to paint
	 @param x1 left
	 @param y1 top
	 @param x2 right
	 @param y2 bottom
	 */
	public static void paint(@NotNull Graphics g, @NotNull Texture texture, int x1, int y1, int x2, int y2) {
		paint(g, texture, null, x1, y1, x2, y2);
	}

	/**
	 Paints the given texture from area x1,y1 to x2,y2 with a color to multiply the texture with

	 @param g graphics
	 @param texture texture to paint
	 @param multColor Color used to multiply the texture's output, or null if to ignore
	 @param x1 left
	 @param y1 top
	 @param x2 right
	 @param y2 bottom
	 */
	public static void paint(@NotNull Graphics g, @NotNull Texture texture, @Nullable Color multColor,
							 int x1, int y1, int x2, int y2) {
		if (texture instanceof Texture.Color) {
			Texture.Color tc = (Texture.Color) texture;
			Color color;
			if (multColor != null) {
				color = Color.color(
						tc.getRed() * multColor.getRed(),
						tc.getGreen() * multColor.getGreen(),
						tc.getBlue() * multColor.getBlue(),
						tc.getAlpha() * multColor.getOpacity()
				);
			} else {
				color = Color.color(tc.getRed(), tc.getGreen(), tc.getBlue(), tc.getAlpha());
			}
			g.setStroke(color);
			g.fillRectangle(x1, y1, x2, y2);
		} else {
			g.setStroke(Color.PINK);
			g.fillRectangle(x1, y1, x2, y2);
		}
	}
}
