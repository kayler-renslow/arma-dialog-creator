/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
