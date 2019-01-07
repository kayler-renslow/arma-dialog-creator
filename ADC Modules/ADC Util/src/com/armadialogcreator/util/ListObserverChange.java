package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ListObserver
 @see ListObserver#addListener(ListObserverListener)
 @since 08/12/2016 */
public class ListObserverChange<E> {
	public enum ChangeType {
		Add, Set, Remove, Move
	}

	private final ListObserver<E> modifiedList;
	private ListObserverChangeAdd<E> added;
	private ListObserverChangeRemove<E> removed;
	private ListObserverChangeMove<E> moved;
	private ListObserverChangeSet<E> set;
	private ChangeType changeType = null;

	protected ListObserverChange(@NotNull ListObserver<E> modifiedList) {
		this.modifiedList = modifiedList;
	}

	@NotNull
	public ListObserverChange.ChangeType getChangeType() {
		return changeType;
	}

	void setSet(ListObserverChangeSet<E> set) {
		this.set = set;
		checkType();
		changeType = ChangeType.Set;
	}

	void setAdded(ListObserverChangeAdd<E> added) {
		this.added = added;
		checkType();
		changeType = ChangeType.Add;
	}

	void setRemoved(ListObserverChangeRemove<E> removed) {
		this.removed = removed;
		checkType();
		changeType = ChangeType.Remove;
	}

	void setMoved(ListObserverChangeMove<E> move) {
		this.moved = move;
		checkType();
		changeType = ChangeType.Move;
	}

	private void checkType() {
		if (changeType != null) {
			throw new IllegalStateException("only one changeType is allowed at once");
		}
	}

	/** Get the list that had the change (where the change was triggered) */
	@NotNull
	public ListObserver<E> getModifiedListObserver() {
		return modifiedList;
	}

	public boolean wasSet() {
		return changeType == ChangeType.Set;
	}

	/** @return {@link #getChangeType()}=={@link ChangeType#Add} */
	public boolean wasAdded() {
		return changeType == ChangeType.Add;
	}

	/** @return {@link #getChangeType()}=={@link ChangeType#Remove} */
	public boolean wasRemoved() {
		return changeType == ChangeType.Remove;
	}

	/** @return {@link #getChangeType()}=={@link ChangeType#Move} */
	public boolean wasMoved() {
		return changeType == ChangeType.Move;
	}

	/**
	 @return the {@link ListObserverChangeSet} update
	 @throws IllegalStateException when {@link #wasSet()}==false
	 */
	@NotNull
	public ListObserverChangeSet<E> getSet() {
		if (!wasSet()) {
			throw new IllegalStateException("not a set change");
		}
		return set;
	}

	/**
	 @return the {@link ListObserverChangeAdd} update
	 @throws IllegalStateException when {@link #wasAdded()}==false
	 */
	@NotNull
	public ListObserverChangeAdd<E> getAdded() {
		if (!wasAdded()) {
			throw new IllegalStateException("not an add change");
		}
		return added;
	}

	/**
	 @return the {@link ListObserverChangeRemove} update
	 @throws IllegalStateException when {@link #wasRemoved()}==false
	 */
	@NotNull
	public ListObserverChangeRemove<E> getRemoved() {
		if (!wasRemoved()) {
			throw new IllegalStateException("not a remove change");
		}
		return removed;
	}

	/**
	 @return the {@link ListObserverChangeMove} update
	 @throws IllegalStateException when {@link #wasMoved()}==false
	 */
	@NotNull
	public ListObserverChangeMove<E> getMoved() {
		if (!wasMoved()) {
			throw new IllegalStateException("not a move change");
		}
		return moved;
	}


}
