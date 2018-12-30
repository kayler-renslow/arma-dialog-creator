package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/22/2017 */
public class TextRange {
	private int start;
	private int end;

	private TextRange(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int length() {
		return end - start;
	}

	@NotNull
	public static TextRange fromRange(int start, int end) {
		testBounds(start, end);
		return new TextRange(start, end);
	}

	@NotNull
	public static TextRange fromLength(int start, int length) {
		int end = start + length;
		testBounds(start, end);
		return new TextRange(start, end);
	}

	@NotNull
	public static TextRange forString(@NotNull String s) {
		int end = s.length();
		return new TextRange(0, end);
	}

	private static void testBounds(int start, int end) {
		if (start < 0) {
			throw new IllegalArgumentException("start < 0");
		}
		if (end < 0) {
			throw new IllegalArgumentException("end < 0");
		}
		if (end < start) {
			throw new IllegalArgumentException("end < start");
		}
	}

}
