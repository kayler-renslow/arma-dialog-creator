package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.arma.util.Texture;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/05/2017 */
public class TexturePainter {
	/**
	 paints the given texture from area x1,y1 to x2,y2

	 @param gc context
	 @param texture texture to paint
	 @param x1 left
	 @param y1 top
	 @param x2 right
	 @param y2 bottom
	 */
	public static void paint(@NotNull GraphicsContext gc, @NotNull Texture texture, int x1, int y1, int x2, int y2) {
		if (texture instanceof Texture.Color) {
			Texture.Color tc = (Texture.Color) texture;
			Color color = Color.color(tc.getR(), tc.getG(), tc.getB(), tc.getA());
			gc.setStroke(color);
			Region.fillRectangle(gc, x1, y1, x2, y2);
		} else {
			gc.setStroke(Color.PINK);
			Region.fillRectangle(gc, x1, y1, x2, y2);
		}
	}
}
