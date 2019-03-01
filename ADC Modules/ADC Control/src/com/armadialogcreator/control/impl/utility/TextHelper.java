package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.sv.SerializableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/03/2017 */
public class TextHelper {

	/**
	 Get the text that can be displayed by a {@link ArmaControlRenderer}

	 @param newValue the text as a {@link SerializableValue}. If null, will return ""
	 @return the text displayable for a renderer
	 */
	@NotNull
	public static String getText(@Nullable SerializableValue newValue) {
		if (newValue == null) {
			return "";
		} else {
			String tostring = newValue.toString();
			if (tostring.length() < 2) {
				//isn't a string literal like "HELLO ""World"""
				return cancelQuotes(tostring);
			} else {
				char first = tostring.charAt(0);
				char last = tostring.charAt(tostring.length() - 1);
				if (first == last && (first == '"' || first == '\'')) {
					if (tostring.length() > 2) { //isn't an empty string like "" or ''
						tostring = tostring.substring(1, tostring.length());
					} else {
						tostring = ""; //empty string
					}
				}
				return cancelQuotes(tostring);
			}
		}
	}

	/**
	 Paint the text where designated. The text will not be clipped anywhere.
	 <p>
	 This method will invoke {@link GraphicsContext#save()} and {@link GraphicsContext#restore()}
	 at the start and end of this method, respectively.

	 @param gc context to use
	 @param textX x position of text
	 @param textY y position of text
	 @param font font to use
	 @param text text to use
	 @param textColor color of text
	 @param textShadow shadow to use
	 @param shadowColor color of shadow
	 */
	public static void paintText(@NotNull GraphicsContext gc, int textX, int textY, @NotNull Font font,
								 @NotNull String text, @NotNull Color textColor,
								 @NotNull TextShadow textShadow, @NotNull Color shadowColor) {
		gc.save();

		gc.setFont(font);
		gc.setFill(textColor);
		switch (textShadow) {
			case None: {
				gc.fillText(text, textX, textY);
				break;
			}
			case DropShadow: {
				final double offset = 2.0;
				gc.setFill(shadowColor);
				gc.fillText(text, textX + offset, textY + offset);
				gc.setFill(textColor);
				gc.fillText(text, textX, textY);
				break;
			}
			case Stroke: {
				gc.setLineWidth(2);
				gc.setStroke(shadowColor);
				gc.strokeText(text, textX, textY);
				gc.fillText(text, textX, textY);
				break;
			}
			default: {
				throw new IllegalStateException("unknown textShadow=" + textShadow);
			}
		}
		gc.restore();
	}

	/**
	 Get the Font object for the given font size (as a percentage of viewport)

	 @param resolution resolution to use
	 @param fontSizePercent percentage value of the font size
	 @return the font object
	 */
	@NotNull
	public static Font getFont(@NotNull ArmaResolution resolution, double fontSizePercent) {
		return Font.font(fontSize(resolution, fontSizePercent));
	}

	/**
	 Get the font size for a {@link Font} object with the provided font size as a percentage of resolution viewport

	 @param resolution resolution to use
	 @param fontSizePercent font size as a percentage of viewport
	 @return font size for a {@link Font} object
	 */
	private static double fontSize(@NotNull ArmaResolution resolution, double fontSizePercent) {
		double maxPixels = resolution.getViewportHeight();
		return toPoints(maxPixels * fontSizePercent);
	}

	private static double toPoints(double pixels) {
		final double pointsPerInch = 72;
		final double pixelsPerInch = 96;
		return pixels * pointsPerInch / pixelsPerInch;
	}

	private static String cancelQuotes(@NotNull String s) {
		s = s.replaceAll("([\"'])\\1", "$1"); //remove any "" or '' and convert to " and '
		return s;
	}
}
