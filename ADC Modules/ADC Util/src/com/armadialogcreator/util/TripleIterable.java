package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 @author K
 @since 02/06/2019 */
public class TripleIterable<E> implements Iterable<E> {
	private final Iterable<E> first;
	private final Iterable<E> second;
	private final Iterable<E> third;

	public TripleIterable(@NotNull Iterable<E> first, @NotNull Iterable<E> second, @NotNull Iterable<E> third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@NotNull
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			Iterator<E> firstIter = first.iterator();
			Iterator<E> secondIter = second.iterator();
			Iterator<E> thirdIter = third.iterator();

			@Override
			public boolean hasNext() {
				return firstIter.hasNext() || secondIter.hasNext() || thirdIter.hasNext();
			}

			@Override
			public E next() {
				return firstIter.hasNext() ? firstIter.next() : secondIter.hasNext() ? secondIter.next() : thirdIter.next();
			}
		};
	}
}
