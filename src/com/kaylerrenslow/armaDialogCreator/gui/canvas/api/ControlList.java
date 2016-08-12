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

import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 @author Kayler
 Used for storing controls. This is an alternative to {@link javafx.collections.ObservableList}. Advantages of this over the other is this supports moving. In the other one, in order to "move"
 something, you would have to remove it and then add it back. This would fire 2 events: remove and add. Although this implementation functionally does the same thing, only one event is fired for
 moving and thus makes it easier to detect and manage.
 Created on 08/12/2016. */
public class ControlList<C extends Control> implements List<C> {
	private final ArrayList<C> controls = new ArrayList<>();
	private final LinkedList<ControlListChangeListener<C>> listeners = new LinkedList<>();
	private final ControlHolder<C> holder;
	private final UpdateListenerGroup<Object> onClear = new UpdateListenerGroup<>();
	
	
	public ControlList(ControlHolder<C> holder) {
		this.holder = holder;
	}
	
	/** Add a control to end of list. Returns true. */
	public boolean add(C control) {
		add(controls.size(), control);
		return true;
	}
	
	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return controls.containsAll(c);
	}
	
	@Override
	public boolean addAll(@NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends C> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAll(int index, @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends C> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		controls.clear();
		onClear.update(null);
	}
	
	/** Listeners for when the list is cleared. */
	public UpdateListenerGroup<Object> getOnClear() {
		return onClear;
	}
	
	@Override
	public C get(int index) {
		return controls.get(index);
	}
	
	@Override
	public C set(int index, @Flow(targetIsContainer = true) C element) {
		boundTest(index);
		C old = controls.set(index, element);
		ControlListChange<C> change = new ControlListChange<>();
		change.setSet(new ControlSet<>(old, element, index));
		notifyListeners(change);
		return old;
	}
	
	/** Add a control at index */
	public void add(int index, C control) {
		boundTest(index);
		controls.add(index, control);
		ControlListChange<C> change = new ControlListChange<>();
		change.setAdded(new ControlAdd<>(control, index));
		notifyListeners(change);
	}
	
	/** Remove a control. Returns true if control could be located and removed, false otherwise. */
	@Override
	public boolean remove(Object control) {
		int index = controls.indexOf(control);
		if (index < 0) {
			return false;
		}
		remove(index);
		return true;
	}
	
	/** Removes a control at index */
	public C remove(int index) {
		boundTest(index);
		C removedControl = controls.remove(index);
		ControlListChange<C> change = new ControlListChange<>();
		change.setRemoved(new ControlRemove<>(removedControl, index));
		notifyListeners(change);
		return removedControl;
	}
	
	@Override
	public int indexOf(Object o) {
		return controls.indexOf(o);
	}
	
	@Override
	public int lastIndexOf(Object o) {
		return controls.lastIndexOf(o);
	}
	
	@NotNull
	@Override
	public ListIterator<C> listIterator() {
		return controls.listIterator();
	}
	
	@NotNull
	@Override
	public ListIterator<C> listIterator(int index) {
		return controls.listIterator(index);
	}
	
	@NotNull
	@Override
	public List<C> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 Performs a move operation on this list. This calls {@link #move(int, ControlHolder, int)} with the index of toMove and this list's control holder as the new parent
	 
	 @param toMove control to move in this list
	 @param newIndex new index to place the control in this list
	 @return true if the operation succeeded, false if it didn't (occurs when control couldn't be found)
	 */
	public boolean move(C toMove, int newIndex) {
		int index = controls.indexOf(toMove);
		if (index < 0) {
			return false;
		}
		move(index, holder, newIndex);
		return true;
	}
	
	
	/**
	 Performs a move operation on this list. This calls {@link #move(int, ControlHolder, int)} with the index and this list's control holder as the new parent
	 
	 @param index index of control to move in this list
	 @param newIndex new index to place the control in this list
	 */
	public void move(int index, int newIndex) {
		move(index, holder, newIndex);
	}
	
	/**
	 Moves a control from this list into another ControlHolder's ControlList. This method will result in {@link ControlListChange#getMoved()} not being null.
	 
	 @param toMove control to move
	 @param newParent holder to move the control to
	 @param newParentIndex index of the new holder's control list to move to
	 @return true if the operation was successful, false if it wasn't (happens when the control to move wasn't located).
	 */
	public boolean move(C toMove, ControlHolder<C> newParent, int newParentIndex) {
		int index = controls.indexOf(toMove);
		if (index < 0) {
			return false;
		}
		move(index, newParent, newParentIndex);
		return true;
	}
	
	/**
	 Moves a control at index indexOfControlToMove from this list into another ControlHolder's ControlList. This method will result in {@link ControlListChange#getMoved()} not being null.
	 
	 @param indexOfControlToMove index of control to move inside this ControlList
	 @param newParent holder to move the control to
	 @param newParentIndex index of the new holder's control list to move to
	 */
	public void move(int indexOfControlToMove, ControlHolder<C> newParent, int newParentIndex) {
		boundTest(indexOfControlToMove);
		newParent.getControls().boundTest(newParentIndex);
		C toMove = controls.get(indexOfControlToMove);
		newParent.getControls().controls.add(newParentIndex, toMove);
		controls.remove(indexOfControlToMove); //remove after insertion to prevent index mis-align
		
		ControlListChange<C> change = new ControlListChange<>();
		change.setMoved(new ControlMove<>(holder, indexOfControlToMove, newParent, newParentIndex));
		notifyListeners(change);
		newParent.getControls().notifyListeners(change);
		
	}
	
	/** Adds a listener. If the listener already has been added, will not be added again */
	public void addChangeListener(ControlListChangeListener<C> l) {
		if (listeners.contains(l)) {
			return;
		}
		listeners.add(l);
	}
	
	/** Removes a listener. Returns true if the listener was added and not removed, false otherwise */
	public boolean removeChangeListener(ControlListChangeListener<C> l) {
		return listeners.remove(l);
	}
	
	private void notifyListeners(ControlListChange<C> change) {
		for (ControlListChangeListener<C> l : listeners) {
			l.onChanged(this, change);
		}
	}
	
	private void boundTest(int index) {
		if (index < 0 || index >= controls.size()) {
			throw new IndexOutOfBoundsException("index is out of range. index:" + index + " size of list:" + controls.size());
		}
	}
	
	@Override
	public int size() {
		return controls.size();
	}
	
	@Override
	public boolean isEmpty() {
		return controls.isEmpty();
	}
	
	@Override
	public boolean contains(Object o) {
		return controls.contains(o);
	}
	
	@NotNull
	@Override
	public Iterator<C> iterator() {
		return controls.iterator();
	}
	
	@NotNull
	@Override
	public Object[] toArray() {
		return controls.toArray();
	}
	
	@NotNull
	@Override
	public <T> T[] toArray(@NotNull T[] a) {
		return controls.toArray(a);
	}
}
