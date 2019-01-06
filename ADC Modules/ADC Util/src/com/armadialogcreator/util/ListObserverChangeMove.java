package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ListObserver#move(int, ListObserver, int)
 @since 08/12/2016 */
public class ListObserverChangeMove<E> {
	private final E moved;
	private final ListObserver<E> oldList;
	private final int oldIndex;
	private final ListObserver<E> newList;
	private final int newParentIndex;
	private final boolean isEntryUpdate;

	public ListObserverChangeMove(@NotNull E moved, @NotNull ListObserver<E> oldList, int oldIndex, @NotNull
			ListObserver<E> newList, int newParentIndex, boolean isEntryUpdate) {
		this.moved = moved;
		this.oldList = oldList;
		this.oldIndex = oldIndex;
		this.newList = newList;
		this.newParentIndex = newParentIndex;
		this.isEntryUpdate = isEntryUpdate;
	}

	/** The element that was moved from one list to another */
	@NotNull
	public E getMoved() {
		return moved;
	}

	public int getOldIndex() {
		return oldIndex;
	}

	public int getDestinationIndex() {
		return newParentIndex;
	}

	/** Get the list that the moved control belonged to before the move */
	@NotNull
	public ListObserver<E> getOldList() {
		return oldList;
	}

	/** Get the list that the control moved to */
	@NotNull
	public ListObserver<E> getDestinationList() {
		return newList;
	}

	/**
	 Return true if this update comes from the moved element being moved into it's new
	 list via {@link ListObserver#move(int, ListObserver, int)},
	 or false if this update captures the moved
	 control leaving the old list.

	 @see ListObserver#move(int, ListObserver, int)
	 */
	public boolean isEntryUpdate() {
		return isEntryUpdate;
	}

}
