package com.kaylerrenslow.armaDialogCreator.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

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

	/** Merges the two arrays together such that the right array's contents will be appended to the left array. Then sorts the merged list with given comparator */
	public static <E> E[] mergeAndSort(Class<E> clazz, E[] left, E[] right, Comparator<E> comparator) {
		E[] ret = mergeArrays(clazz, left, right);
		Arrays.sort(ret, comparator);
		return ret;
	}
}
