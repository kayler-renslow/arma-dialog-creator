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

	/**
	 @return true if there is chars left to read, false otherwise
	 */
	public boolean hasAvailable() {
		return pos < cs.length();
	}

	/** Get the current position inside the CharSequence */
	public int getPos() {
		return pos;
	}

	/**
	 Check if there are chars ahead of the cursor.

	 @param amount number of chars to look ahead of
	 @return true if there is chars ahead, false otherwise
	 */
	public boolean canPeekAhead(int amount) {
		return pos + amount < cs.length();
	}

	/**
	 Get the char at {@link #getPos()} + <code>amount</code>. Will throw exception if out of bounds. This method will not advance the cursor.

	 @param amount numbers of chars to skip
	 @return the char
	 */
	public char peekAhead(int amount) {
		return cs.charAt(pos + amount);
	}

	/**
	 Get the char at {@link #getPos()} - <code>amount</code>. Will throw exception if out of bounds. This method will not change the cursor's position.

	 @param amount numbers of chars to skip
	 @return the char
	 */
	public char lookBehind(int amount) {
		return cs.charAt(pos - amount);
	}

	/**
	 Check if there are chars behind the cursor.

	 @param amount number of chars to look behind
	 @return true if there is chars behind, false otherwise
	 */
	public boolean canLookBehind(int amount) {
		return pos - amount >= 0;
	}

	/**
	 @return the number of newline characters that have been read (counts \n)
	 */
	public int getLineCount() {
		return lineCount;
	}

	/** @return get the position in the current line number. This will be reset everytime {@link #getLineCount()} increments */
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

	/** @return number of chars left to read (returns 0 if none left to read) */
	public int available() {
		return Math.max(0, cs.length() - pos);
	}
}
