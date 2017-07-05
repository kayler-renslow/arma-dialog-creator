package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/04/2017 */
public class ColorHelper {
	@NotNull
	public static Color transition(double ratio, @NotNull Color color1, @NotNull Color color2) {
		double red = Math.abs((ratio * color1.getRed()) + ((1 - ratio) * color2.getRed()));
		double green = Math.abs((ratio * color1.getGreen()) + ((1 - ratio) * color2.getGreen()));
		double blue = Math.abs((ratio * color1.getBlue()) + ((1 - ratio) * color2.getBlue()));
		return Color.color(red, green, blue);
	}
}
