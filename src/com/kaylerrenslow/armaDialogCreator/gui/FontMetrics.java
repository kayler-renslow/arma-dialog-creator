package com.kaylerrenslow.armaDialogCreator.gui;

import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 12/09/2017 */
public class FontMetrics {
	private final Font font;

	public FontMetrics(@NotNull Font font) {
		this.font = font;
	}

	/**
	 Computes the string width for the given text.

	 @param text the text. If this is null, the returned result will be equal to if text was an empty string
	 @return the string width
	 */
	public int computeStringWidth(@Nullable String text) {
		return -1; //todo
	}

	/** @return the font's line height */
	public int getLineHeight() {
		return -1; //todo
	}
}
