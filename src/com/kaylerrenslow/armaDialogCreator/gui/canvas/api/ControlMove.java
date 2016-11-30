package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/12/2016.
 */
public class ControlMove<C extends CanvasControl> {
	private final C controlMoved;
	private final ControlList<C> oldList;
	private final int oldIndex;
	private final ControlList<C> newList;
	private final int newParentIndex;
	private boolean isOriginalUpdate;

	public ControlMove(C controlMoved, ControlList<C> oldList, int oldIndex, ControlList<C> newList, int newParentIndex) {
		this.controlMoved = controlMoved;
		this.oldList = oldList;
		this.oldIndex = oldIndex;
		this.newList = newList;
		this.newParentIndex = newParentIndex;
	}

	/** The control that was moved from one list to another */
	@NotNull
	public C getMovedControl() {
		return controlMoved;
	}

	/** Equivalent to {@link #getOldList()} as the ControlList in {@link ControlList#getHolder()} */
	@NotNull
	public ControlHolder<C> getOldHolder() {
		return oldList.getHolder();
	}

	public int getOldIndex() {
		return oldIndex;
	}

	/** Equivalent to {@link #getDestinationList()} as the ControlList in {@link ControlList#getHolder()} */
	@NotNull
	public ControlHolder<C> getDestinationHolder() {
		return newList.getHolder();
	}

	public int getDestinationIndex() {
		return newParentIndex;
	}

	/** Get the list that the moved control belonged to before the move */
	@NotNull
	public ControlList<C> getOldList() {
		return oldList;
	}

	/** Get the list that the control moved to */
	@NotNull
	public ControlList<C> getDestinationList() {
		return newList;
	}

	/**
	 Return true if this update comes from the moved control being moved into it's new list via {@link ControlList#move(int, ControlList, int)}, or false if this update captures the moved
	 control leaving the old list.

	 @see ControlList#move(int, ControlList, int)
	 */
	public boolean isOriginalUpdate() {
		return isOriginalUpdate;
	}

	void setOriginalUpdate(boolean firstUpdate) {
		isOriginalUpdate = firstUpdate;
	}
}
