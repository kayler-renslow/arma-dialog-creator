package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 @author K
 @since 02/06/2019 */
public class DoubleIterable<E> implements Iterable<E> {
	private final Iterable<E> first;
	private final Iterable<E> second;

	public DoubleIterable(@NotNull Iterable<E> first, @NotNull Iterable<E> second) {
		this.first = first;
		this.second = second;
	}

	@NotNull
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			Iterator<E> firstIter = first.iterator();
			Iterator<E> secondIter = second.iterator();

			@Override
			public boolean hasNext() {
				return firstIter.hasNext() || secondIter.hasNext();
			}

			@Override
			public E next() {
				return firstIter.hasNext() ? firstIter.next() : secondIter.next();
			}
		};
	}
}
