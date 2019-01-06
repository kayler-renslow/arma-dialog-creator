package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ListObserver#remove(int)
 @since 08/12/2016 */
public class ListObserverChangeRemove<E> {
	private final E removed;
	private final int index;

	public ListObserverChangeRemove(@NotNull E removed, int index) {
		this.removed = removed;
		this.index = index;
	}

	@NotNull
	public E getRemoved() {
		return removed;
	}

	public int getIndex() {
		return index;
	}
}
