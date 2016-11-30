package com.kaylerrenslow.armaDialogCreator.util;

/**
 Created by Kayler on 10/16/2016.
 */
public interface CustomToString<T> {
	/** Returns {@code value} as a String */
	String toString(T value);

	class SimpleToString<T> implements CustomToString<T> {

		@Override
		public String toString(T value) {
			return value.toString();
		}
	}
}
