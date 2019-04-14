package com.armadialogcreator.canvas;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 12/09/2017 */
public class FontMetrics {
	private final Font font;
	private final Text t = new Text("");

	public FontMetrics(@NotNull Font font) {
		this.font = font;
		t.setFont(font);
	}

	/**
	 Computes the string width for the given text.

	 @param text the text. If this is null, the returned result will be equal to if text was an empty string
	 @return the string width
	 */
	public int computeStringWidth(@Nullable String text) {
		this.t.setText(text);
		return (int) t.getLayoutBounds().getWidth();
	}

	/** @return the font's line height */
	public int getLineHeight() {
		return (int) t.getLayoutBounds().getHeight();
	}
}
