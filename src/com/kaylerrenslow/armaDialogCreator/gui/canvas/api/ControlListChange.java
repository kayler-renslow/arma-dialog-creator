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

/**
 Created by Kayler on 08/12/2016.
 */
public class ControlListChange<C extends Control> {
	private ControlAdd<C> added;
	private ControlRemove<C> removed;
	private ControlMove<C> moved;
	private ControlSet<C> set;
	
	void setSet(ControlSet<C> set) {
		this.set = set;
	}
	
	void setAdded(ControlAdd<C> added) {
		if (wasSet() || wasMoved() || wasRemoved()) {
			throw new IllegalStateException("only one state is allowed at once");
		}
		this.added = added;
	}
	
	void setRemoved(ControlRemove<C> removed) {
		if (wasSet() || wasMoved() || wasAdded()) {
			throw new IllegalStateException("only one state is allowed at once");
		}
		this.removed = removed;
	}
	
	void setMoved(ControlMove<C> move) {
		if (wasSet() || wasAdded() || wasRemoved()) {
			throw new IllegalStateException("only one state is allowed at once");
		}
		this.moved = move;
	}
	
	public boolean wasSet() {
		return getSet() != null;
	}
	
	public boolean wasAdded() {
		return getAdded() != null;
	}
	
	public boolean wasRemoved() {
		return getRemoved() != null;
	}
	
	public boolean wasMoved() {
		return getMoved() != null;
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
