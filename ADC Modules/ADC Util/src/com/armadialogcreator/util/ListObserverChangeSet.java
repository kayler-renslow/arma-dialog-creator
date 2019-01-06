package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ListObserver#set(int, E)
 @since 08/12/2016 */
public class ListObserverChangeSet<E> {
	private final E oldControl;
	private final E newControl;
	private final int index;

	public ListObserverChangeSet(@NotNull E oldControl, @NotNull E newControl, int index) {
		this.oldControl = oldControl;
		this.newControl = newControl;
		this.index = index;
	}

	@NotNull
	public E getOld() {
		return oldControl;
	}

	@NotNull
	public E getNew() {
		return newControl;
	}

	public int getIndex() {
		return index;
	}
}
