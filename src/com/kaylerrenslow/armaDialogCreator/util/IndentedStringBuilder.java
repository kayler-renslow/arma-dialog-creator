package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;

/**
 A {@link StringBuilder} like class that supports automatic indentation when append(\n) ({@link #append(char)})
 was invoked

 @author Kayler
 @since 05/26/2017 */
public class IndentedStringBuilder {
	/** StringBuilder that is being used */
	private final StringBuilder b = new StringBuilder();
	/** Number of spaces equal to a tab, or any value if {@link #useTabCharacter} is true */
	private final int tabSizeInSpaces;
	/** Number of current tabs */
	private int currentTabCount = 0;
	/** If true, write the tab character instead of a number of spaces */
	private boolean useTabCharacter;

	/**
	 Will create a builder that writes in a specified number of spaces that will represent a tab,
	 instead of writing the tab character

	 @param tabSizeInSpaces number of spaces equal to 1 tab
	 @throws IllegalArgumentException if tabSizeInSpaces <= 0
	 @see #IndentedStringBuilder(int, boolean)
	 */
	public IndentedStringBuilder(int tabSizeInSpaces) {
		this(tabSizeInSpaces, false);
	}

	/**
	 See class level doc

	 @param tabSizeInSpaces number of spaces equal to 1 tab
	 @param useTabCharacter if true, <code>tabSizeInSpaces</code> will be ignored and will just write
	 the tab character (\t) instead of a bunch of spaces
	 @throws IllegalArgumentException if tabSizeInSpaces <= 0 and useTabCharacter==false
	 */
	public IndentedStringBuilder(int tabSizeInSpaces, boolean useTabCharacter) {
		if (!useTabCharacter) {
			if (tabSizeInSpaces <= 0) {
				throw new IllegalArgumentException("tabSizeInSpaces must be >0");
			}
		}
		this.useTabCharacter = useTabCharacter;
		this.tabSizeInSpaces = tabSizeInSpaces;
	}

	/** @return number of spaces equal to 1 tab */
	public int getTabSizeInSpaces() {
		return tabSizeInSpaces;
	}

	/** Increment the current tab size by {@link #getTabSizeInSpaces()}. Initial tab length is 0. */
	public void incrementTabCount() {
		currentTabCount++;
	}

	/**
	 Decrement the current tab size by {@link #getTabSizeInSpaces()}. Initial tab length is 0.

	 @throws IllegalStateException if {@link #getCurrentTab()}'s length is 0
	 */
	public void decrementTabCount() {
		if (currentTabCount == 0) {
			throw new IllegalArgumentException("currentTabCount ==0");
		}
		currentTabCount--;
	}

	/**
	 Get the current tab, which is comprised of a number of spaces equal to {@link #getTabSizeInSpaces()}.
	 Initial tab size is 0.

	 @return current tab
	 */
	@NotNull
	public String getCurrentTab() {
		StringBuilder sb = new StringBuilder(currentTabCount * tabSizeInSpaces);
		writeCurrentTab(sb);
		return sb.toString();
	}

	/** Write the current tab into the given StringBuilder */
	private void writeCurrentTab(@NotNull StringBuilder b) {
		if (useTabCharacter) {
			for (int tabCount = 0; tabCount < currentTabCount; tabCount++) {
				b.append('\t');
			}
		} else {
			for (int tabCount = 0; tabCount < currentTabCount; tabCount++) {
				for (int space = 0; space < tabSizeInSpaces; space++) {
					b.append(' ');
				}
			}
		}
	}

	/** Get the builder that is being used */
	@NotNull
	public StringBuilder getBuilder() {
		return b;
	}

	/**
	 If c=='\n', will also then append {@link #getCurrentTab()}.

	 @see StringBuilder#append(char)
	 */
	public void append(char c) {
		b.append(c);
		if (c == '\n') {
			if (currentTabCount > 0) {
				writeCurrentTab(b);
			}
		}
	}

	/**
	 Will use {@link #append(char)} for the whole String (0 to length)
	 */
	public void append(@NotNull String s) {
		for (int i = 0; i < s.length(); i++) {
			append(s.charAt(i));
		}
	}

	/**
	 Will use {@link #append(char)} for each char in the String from start (inclusive) to end (exclusive)

	 @throws IndexOutOfBoundsException if
	 {@code start} is negative, or
	 {@code start} is greater than {@code end} or
	 {@code end} is greater than {@code s.length()}
	 */
	public void append(@NotNull String s, int start, int end) {
		if ((start < 0) || (start > end) || (end > s.length())) {
			throw new IndexOutOfBoundsException(
					"start " + start + ", end " + end + ", s.length() "
							+ s.length());
		}
		for (int i = start; i < end; i++) {
			append(s.charAt(i));
		}
	}

	/**
	 Will use {@link #append(char)} for the whole StringBuilder's value (0 to length)
	 */
	public void append(@NotNull StringBuilder b) {
		for (int i = 0; i < b.length(); i++) {
			append(b.charAt(i));
		}
	}

	/** @return {@link StringBuilder#toString()} on {@link #getBuilder()} */
	@Override
	public String toString() {
		return b.toString();
	}


}
