package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 A macro that holds a String. This is different from a regular macro such that this macro requires quotes to be arround the end result, where as a regular macro doesn't.
 Created on 07/05/2016. */
public class StringMacro extends Macro<String> {

	private String cachedValue;

	/**
	 A macro that stores a String value (text surrounded with quotes)

	 @param key the key (prefered to be all caps)
	 @param value the value (without quotes)
	 */
	public StringMacro(@NotNull String key, @NotNull String value) {
		super(key, value);
		cachedValue = "\"" + value + "\"";
	}

	/**
	 A macro that stores a String value (text surrounded with quotes)

	 @param key the key (preferred to be all caps)
	 @param value the value (with or without quotes surrounding it)
	 @param quotesIncluded true if the given value already has the required quotes, false otherwise
	 */
	public StringMacro(@NotNull String key, @NotNull String value, boolean quotesIncluded) {
		this(key, quotesIncluded ? value.substring(1, value.length() - 1) : value);
	}

	public String getValueWithoutQuotes() {
		return value;
	}

	@Override
	public void setValue(@NotNull String value) {
		super.setValue(value);
		cachedValue = "\"" + value + "\"";
	}

	@NotNull
	@Override
	public String getValue() {
		return cachedValue; //no need to constantly return a new String object with quotes appended
	}
}
