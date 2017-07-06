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
	public static void paint(@NotNull GraphicsContext gc, @NotNull Texture texture, int x, int y, int w, int h) {
		if (texture instanceof Texture.Color) {
			Texture.Color tc = (Texture.Color) texture;
			gc.setStroke(Color.color(tc.getR(), tc.getG(), tc.getB(), tc.getA()));
			Region.fillRectangle(gc, x, y, x + w, y + h);
		} else {
			throw new IllegalStateException("unpainted texture class: " + texture.getClass().getName());
		}
	}
}
