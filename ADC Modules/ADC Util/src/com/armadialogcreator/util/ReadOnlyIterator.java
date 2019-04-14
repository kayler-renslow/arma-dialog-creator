package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 @author K
 @since 01/08/2019 */
public class ReadOnlyIterator<T> implements Iterator<T> {
	private final Iterator<T> iter;

	public ReadOnlyIterator(@NotNull Iterator<T> iter) {
		this.iter = iter;
	}

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

	}
}
