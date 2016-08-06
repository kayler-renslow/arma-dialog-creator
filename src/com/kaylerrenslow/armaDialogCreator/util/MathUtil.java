/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.util;

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

	public static double truncate(double d, int digits){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(digits);
		nf.setGroupingUsed(false);
		return Double.parseDouble(nf.format(d));
	}
}
