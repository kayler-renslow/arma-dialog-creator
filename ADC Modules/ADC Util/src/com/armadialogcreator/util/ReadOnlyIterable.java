package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 @author K
 @since 01/08/2019 */
public class ReadOnlyIterable<T> implements Iterable<T> {
	private final Iterable<T> iterable;

	public ReadOnlyIterable(@NotNull Iterable<T> iterable) {
		this.iterable = iterable;
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Iterator<T> iter = iterable.iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public T next() {
				return iter.next();
			}

			@Override
			public void remove() {
				throw new IllegalStateException();
			}
		};
	}
}
