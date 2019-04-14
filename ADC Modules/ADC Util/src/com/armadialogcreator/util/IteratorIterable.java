package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 @author K
 @since 01/08/2019 */
public class IteratorIterable<T> implements Iterable<T> {
	private final Iterator<T> iter;

	public IteratorIterable(@NotNull Iterator<T> iter) {
		this.iter = iter;
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return iter;
	}
}
