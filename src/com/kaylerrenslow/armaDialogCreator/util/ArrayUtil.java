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

import java.lang.reflect.Array;

/**
 Created by Kayler on 07/07/2016.
 */
public class ArrayUtil {
	/** Merges the two arrays together such that the right array's contents will be appended to the left array */
	public static <E> E[] mergeArrays(Class<E> clazz, E[] left, E[] right) {
		final int newSize = left.length + right.length;
		E[] ret = (E[]) Array.newInstance(clazz, newSize);
		int i = 0;
		for (E e : left) {
			ret[i++] = e;
		}
		for (E e : right) {
			ret[i++] = e;
		}
		return ret;
	}
}
