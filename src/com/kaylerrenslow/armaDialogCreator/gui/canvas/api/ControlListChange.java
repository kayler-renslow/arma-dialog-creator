package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/12/2016.
 */
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

	public boolean wasAdded() {
		return changeType == ControlListChangeType.ADD;
	}

	public boolean wasRemoved() {
		return changeType == ControlListChangeType.REMOVE;
	}

	public boolean wasMoved() {
		return changeType == ControlListChangeType.MOVE;
	}

	public ControlSet<C> getSet() {
		return set;
	}

	public ControlAdd<C> getAdded() {
		return added;
	}

	public ControlRemove<C> getRemoved() {
		return removed;
	}

	public ControlMove<C> getMoved() {
		return moved;
	}
}
