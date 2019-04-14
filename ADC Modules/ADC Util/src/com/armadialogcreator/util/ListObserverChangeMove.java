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
	private final boolean isSourceListChange;

	public ListObserverChangeMove(@NotNull E moved, @NotNull ListObserver<E> oldList, int oldIndex, @NotNull
			ListObserver<E> newList, int newParentIndex, boolean isSourceListChange) {
		this.moved = moved;
		this.oldList = oldList;
		this.oldIndex = oldIndex;
		this.newList = newList;
		this.newParentIndex = newParentIndex;
		this.isSourceListChange = isSourceListChange;
	}

	/**
	 Return true if this change was created for the source list. Since 2 move changes are created
	 (one for source list and one for target list), this is helped to identify which change this instance was meant for.
	 The source list will always receive the first update.

	 @see ListObserver#move(int, ListObserver, int)
	 */
	public boolean isSourceListChange() {
		return isSourceListChange;
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

}
