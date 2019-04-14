package com.armadialogcreator.canvas;

import java.text.NumberFormat;

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

	public static double truncate(double d, int digits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(digits);
		nf.setGroupingUsed(false);
		return Double.parseDouble(nf.format(d));
	}
}
