package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ListObserver#add(E)
 @since 08/12/2016 */
public class ListObserverChangeAdd<E> {
	private final E added;
	private final int index;

	public ListObserverChangeAdd(@NotNull E added, int index) {
		this.added = added;
		this.index = index;
	}

	@NotNull
	public E getAdded() {
		return added;
	}

	public int getIndex() {
		return index;
	}
}
