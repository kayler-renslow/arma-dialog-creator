package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ControlList
 @see ControlList#addChangeListener(ControlListChangeListener)
 @since 08/12/2016 */
public class ControlListChange<C extends CanvasControl> {
	private final ControlList<C> modifiedList;
	private ControlAdd<C> added;
	private ControlRemove<C> removed;
	private ControlMove<C> moved;
	private ControlSet<C> set;
	private ControlListChangeType changeType = null;

	protected ControlListChange(ControlList<C> modifiedList) {
		this.modifiedList = modifiedList;
	}

	@NotNull
	public ControlListChangeType getChangeType() {
		return changeType;
	}

	void setSet(ControlSet<C> set) {
		this.set = set;
		checkType();
		changeType = ControlListChangeType.SET;
	}

	void setAdded(ControlAdd<C> added) {
		this.added = added;
		checkType();
		changeType = ControlListChangeType.ADD;
	}

	void setRemoved(ControlRemove<C> removed) {
		this.removed = removed;
		checkType();
		changeType = ControlListChangeType.REMOVE;
	}

	void setMoved(ControlMove<C> move) {
		this.moved = move;
		checkType();
		changeType = ControlListChangeType.MOVE;
	}

	private void checkType() {
		if (changeType != null) {
			throw new IllegalStateException("only one changeType is allowed at once");
		}
	}

	/** Get the list that had the change (where the change was triggered) */
	@NotNull
	public ControlList<C> getModifiedList() {
		return modifiedList;
	}

	public boolean wasSet() {
		return changeType == ControlListChangeType.SET;
	}

	/** @return {@link #getChangeType()}=={@link ControlListChangeType#ADD} */
	public boolean wasAdded() {
		return changeType == ControlListChangeType.ADD;
	}

	/** @return {@link #getChangeType()}=={@link ControlListChangeType#REMOVE} */
	public boolean wasRemoved() {
		return changeType == ControlListChangeType.REMOVE;
	}

	/** @return {@link #getChangeType()}=={@link ControlListChangeType#MOVE} */
	public boolean wasMoved() {
		return changeType == ControlListChangeType.MOVE;
	}

	/**
	 @return the {@link ControlSet} update
	 @throws IllegalStateException when {@link #wasSet()}==false
	 */
	@NotNull
	public ControlSet<C> getSet() {
		if (!wasSet()) {
			throw new IllegalStateException("not a set change");
		}
		return set;
	}

	/**
	 @return the {@link ControlAdd} update
	 @throws IllegalStateException when {@link #wasAdded()}==false
	 */
	@NotNull
	public ControlAdd<C> getAdded() {
		if (!wasAdded()) {
			throw new IllegalStateException("not an add change");
		}
		return added;
	}

	/**
	 @return the {@link ControlRemove} update
	 @throws IllegalStateException when {@link #wasRemoved()}==false
	 */
	@NotNull
	public ControlRemove<C> getRemoved() {
		if (!wasRemoved()) {
			throw new IllegalStateException("not a remove change");
		}
		return removed;
	}

	/**
	 @return the {@link ControlMove} update
	 @throws IllegalStateException when {@link #wasMoved()}==false
	 */
	@NotNull
	public ControlMove<C> getMoved() {
		if (!wasMoved()) {
			throw new IllegalStateException("not a move change");
		}
		return moved;
	}
}
