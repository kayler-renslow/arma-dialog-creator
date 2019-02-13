package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 @author K
 @since 02/12/2019 */
public class ReadOnlyArray<T> implements Iterable<T> {
	public static final ReadOnlyArray EMPTY = new ReadOnlyArray<>(new Object[0]);

	private final T[] arr;
	public final int length;

	public ReadOnlyArray(@NotNull T[] arr) {
		this.arr = arr;
		this.length = arr.length;
	}

	@NotNull
	public T item(int i) {
		return arr[i];
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < arr.length;
			}

			@Override
			public T next() {
				return arr[i++];
			}
		};
	}
}
