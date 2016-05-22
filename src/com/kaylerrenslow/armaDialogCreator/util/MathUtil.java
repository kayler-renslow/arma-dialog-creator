package com.kaylerrenslow.armaDialogCreator.util;

/**
 Created by Kayler on 05/12/2016.
 */
public class MathUtil {

	/**
	 Returns min when v < min, returns max when v > max, otherwise returns v
	 */
	public static int putIntoBounds(int v, int min, int max) {
		if (v < min) {
			return min;
		}
		if (v > max) {
			return max;
		}
		return v;
	}

	public static boolean outOfBounds(int v, int min, int max) {
		return v < min || v > max;
	}
}
