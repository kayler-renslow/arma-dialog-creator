package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author kayler
 @since 4/26/17 */
public class CharSequenceReader {
	private final CharSequence cs;
	private int pos = 0;
	private int lineCount = 0;
	private int posInLine = 0;

	public CharSequenceReader(@NotNull CharSequence cs) {
		this.cs = cs;
	}

	public boolean hasAvailable() {
		return pos >= cs.length();
	}

	public int getPos() {
		return pos;
	}

	public boolean canPeekAhead(int amount) {
		return pos + amount < cs.length();
	}

	public char peekAhead(int amount) {
		return cs.charAt(pos + amount);
	}

	public char lookBehind(int amount) {
		return cs.charAt(pos - amount);
	}

	public boolean canLookBehind(int amount) {
		return pos - amount >= 0;
	}

	public int getLineCount() {
		return lineCount;
	}

	public int getPosInLine() {
		return posInLine;
	}

	public char read() {
		if (!hasAvailable()) {
			throw new IllegalStateException("can't read when nothing left to read.");
		}
		char c = cs.charAt(pos++);
		if (c == '\n') {
			lineCount++;
			posInLine = 0;
		} else {
			posInLine++;
		}
		return c;
	}
}
