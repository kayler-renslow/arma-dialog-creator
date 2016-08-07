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

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;

import java.util.Iterator;
import java.util.LinkedList;

/**
 Created by Kayler on 08/04/2016.
 */
public interface Display extends ControlHolder {
	
	/** Any time the display or its controls get updated, notify this group. */
	UpdateListenerGroup<Object> getUpdateListenerGroup();
	
	/** Get controls that are rendered first and have no user interaction */
	ReadOnlyList<? extends Control> getBackgroundControls();
	
	/**
	 Get an iterator that will cycle through the background controls ({@link #getBackgroundControls()}) and then main controls ({@link #getControls()})
	 
	 @param backwards if true, the iterator will iterate through the other controls from size-1 to 0 and then the background controls in reverse (starting from size-1 to 0).<br>
	 if false, will iterate through the background controls from 0 to size-1 and then other controls from 0 to size-1
	 */
	default Iterator<? extends Control> iteratorForAllControls(boolean backwards) {
		return new IteratorForAll(this, backwards);
	}
	
	/**
	 Return true if the contorl is a background control (inside {@link #getBackgroundControls()}), false otherwise.<br>
	 Default implementation is <pre>{@link #getBackgroundControls()}.contains(control)</pre>
	 */
	default boolean isBackgroundControl(Control control) {
		return getBackgroundControls().contains(control);
	}
	
	void addBackgroundControl(Control control);
	
	void addBackgroundControl(int index, Control toAdd);
	
	int indexOfBackgroundControl(Control control);
	
	boolean removeBackgroundControl(Control control);
	
	void resolutionUpdate(Resolution newResolution);
	
}

class IteratorForAll implements Iterator<Control> {
	private final boolean backwards;
	private int ind;
	private final LinkedList<ReadOnlyList<? extends Control>> stack = new LinkedList<>();
	private ReadOnlyList<? extends Control> current;
	
	public IteratorForAll(Display display, boolean backwards) {
		this.backwards = backwards;
		if (backwards) {
			stack.push(display.getBackgroundControls());
			stack.push(display.getControls());
			
			ind = stack.peek().size() - 1;
		} else {
			stack.push(display.getControls());
			stack.push(display.getBackgroundControls());
			ind = 0;
		}
		current = stack.pop();
	}
	
	@Override
	public boolean hasNext() {
		if (backwards) {
			if (ind >= 0) {
				return true;
			}
			boolean hasAnotherInd = false;
			for (ReadOnlyList<? extends Control> list : stack) {
				if (list.size() != 0) {
					hasAnotherInd = true;
					break;
				}
			}
			return hasAnotherInd;
		}
		if (stack.size() == 0) {
			return false;
		}
		if (ind < current.size()) {
			return true;
		}
		boolean hasAnotherInd = false;
		for (ReadOnlyList<? extends Control> list : stack) {
			if (list.size() != 0) {
				hasAnotherInd = true;
				break;
			}
		}
		return hasAnotherInd;
	}
	
	
	@Override
	public Control next() {
		if (!hasNext()) {
			throw new IllegalStateException("nothing left to fetch");
		}
		if (backwards) {
			while (ind < 0 && stack.size() > 0) {
				current = stack.pop();
				ind = current.size() - 1;
			}
			return current.get(ind--);
		}
		while(ind >= current.size()){
			current = stack.pop();
			ind = 0;
		}
		return current.get(ind++);
	}
}
