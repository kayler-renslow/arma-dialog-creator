package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/26/2017 */
public class MiscHelpers {
	public static void paintFlippedImage(@NotNull GraphicsContext gc, @NotNull Image img, int x, int y, int w, int h) {
		gc.save();
		gc.scale(-1, 1);
		gc.drawImage(img, -x - w, y, w, h);
		gc.restore();
	}

	public static void paintRotatedImage(@NotNull GraphicsContext gc, @NotNull Image img, int x, int y, int w, int h, double rotateDeg) {
		gc.save();
		gc.translate(x + w / 2, y + h / 2); //move to center
		gc.rotate(rotateDeg);
		gc.drawImage(img, -w / 2, -h / 2, w, h);
		gc.restore();
	}
}
