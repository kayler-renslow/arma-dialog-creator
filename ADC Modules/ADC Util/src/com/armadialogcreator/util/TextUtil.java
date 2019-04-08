package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/7/19 */
public class TextUtil {

	@NotNull
	public static String getMultilineText(@NotNull String text, int cap) {
		StringBuilder sb = new StringBuilder(text.length());
		int len = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			sb.append(c);
			len++;
			if (len >= cap && Character.isWhitespace(c)) {
				len = 0;
				sb.append('\n');
			}
		}
		return sb.toString();
	}
}
