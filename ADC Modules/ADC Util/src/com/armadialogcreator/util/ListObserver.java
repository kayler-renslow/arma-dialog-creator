package com.armadialogcreator.util;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 This is an alternative to {@link javafx.collections.ObservableList}.
 Advantages of this over the other is this supports moving. In the other one, in order to "move"
 something, you would have to remove it and then add it back. This would fire 2 events: remove and add.
 Although this implementation functionally does the same thing, only one event is fired for
 moving and thus makes it easier to detect and manage.

 @author Kayler
 @since 08/12/2016. */
public class ListObserver<E> implements List<E>, Observer<ListObserverListener<E>> {
	private final List<E> list;
	private final LinkedList<ListObserverListener<E>> listeners = new LinkedList<>();

	public ListObserver(@NotNull List<E> list) {
		this.list = list;
	}

	/** Remove all from list. */
	@Override
	public void clear() {
		ListObserverChange<E> change = new ListObserverChange<>(this);
		change.setCleared();
		notifyListeners(change);
		list.clear(); //clear after notify
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	/**
	 Create a {@link ListObserverChangeSet} update.

	 @param index were to set
	 @param replacement replacement
	 @return the old control at the index
	 */
	@Override
	public E set(int index, @NotNull @Flow(targetIsContainer = true) E replacement) {
		boundTest(index);
		E old = list.set(index, replacement);

		ListObserverChange<E> change = new ListObserverChange<>(this);
		change.setSet(new ListObserverChangeSet<>(old, replacement, index));
		notifyListeners(change);
		return old;
	}

	/**
	 Add a element to end of list. Creates a {@link ListObserverChangeAdd} update.

	 @return true
	 */
	public boolean add(@NotNull E element) {
		list.add(element);

		ListObserverChange<E> change = new ListObserverChange<>(this);
		//size -1 because element was already added and size of list has changed
		change.setAdded(new ListObserverChangeAdd<>(element, list.size() - 1));
		notifyListeners(change);
		return true;
	}


	/**
	 Adds a element at an index. Creates a {@link ListObserverChangeAdd} update.

	 @param index where to insert
	 @param element the element
	 */
	public void add(int index, @NotNull E element) {
		if (index != list.size()) {
			boundTest(index);
		}
		list.add(index, element);

		ListObserverChange<E> change = new ListObserverChange<>(this);
		change.setAdded(new ListObserverChangeAdd<>(element, index));
		notifyListeners(change);
	}


	/**
	 Remove a element. Creates a {@link ListObserverChangeRemove} update.

	 @return true if object could be located and removed, false otherwise.
	 */
	@Override
	public boolean remove(@NotNull Object object) {
		int index = list.indexOf(object);
		if (index < 0) {
			return false;
		}
		remove(index);
		return true;
	}

	/**
	 Remove a element at an index. Creates a {@link ListObserverChangeRemove} update.

	 @param index where to remove
	 @return the element removed
	 */
	public E remove(int index) {
		boundTest(index);
		E removedControl = list.remove(index);

		ListObserverChange<E> change = new ListObserverChange<>(this);
		change.setRemoved(new ListObserverChangeRemove<>(removedControl, index));
		notifyListeners(change);
		return removedControl;
	}

	/**
	 Performs a move operation on this list. This calls {@link #move(int, ListObserver, int)} with the
	 index of toMove.

	 @param toMove element to move in this list
	 @param newIndex new index to place the element in this list
	 @return true if the operation succeeded, false if it didn't (occurs when element couldn't be found)
	 */
	public boolean move(@NotNull E toMove, int newIndex) {
		int index = list.indexOf(toMove);
		if (index < 0) {
			return false;
		}
		move(index, this, newIndex);
		return true;
	}

	/**
	 Performs a move operation on this list. This calls {@link #move(int, ListObserver, int)}
	 with the index.

	 @param index index of element to move in this list
	 @param newIndex new index to place the element in this list
	 */
	public void move(int index, int newIndex) {
		move(index, this, newIndex);
	}

	/**
	 Moves a element from this list into another {@link ListObserver}. This method will result in
	 {@link ListObserverChange#getMoved()} not being null.

	 @param toMove element to move
	 @param newList list to move element to
	 @param newIndex index of the new list to move to
	 @return true if the operation was successful, false if it wasn't (happens when the element to move wasn't located).
	 */
	public boolean move(@NotNull E toMove, @NotNull ListObserver<E> newList, int newIndex) {
		int index = list.indexOf(toMove);
		if (index < 0) {
			return false;
		}
		move(index, newList, newIndex);
		return true;
	}


	/**
	 Moves a element at index indexOfElementToMove from this list into another {@link ListObserver}.
	 This method will result in {@link ListObserverChange#getMoved()} not being null. When the internal
	 operation is completed, this list's change listeners will be notified and then newList's listeners.
	 <p>
	 Creates a {@link ListObserverChangeMove} update.

	 @param indexOfElementToMove index of element to move inside this {@link ListObserver}
	 @param newList list to move the element to
	 @param newListIndex index of the new list to move to
	 */
	public void move(int indexOfElementToMove, @NotNull ListObserver<E> newList, int newListIndex) {
		boundTest(indexOfElementToMove);
		if (indexOfElementToMove == newListIndex && newList == this) { //not actually moving
			return;
		}
		E toMove = list.get(indexOfElementToMove);
		list.remove(indexOfElementToMove);
		if (newListIndex != newList.size()) {
			newList.boundTest(newListIndex);
			newList.list.add(newListIndex, toMove);
		} else {
			newList.list.add(toMove);
		}

		ListObserverChange<E> change = new ListObserverChange<>(this);
		change.setMoved(new ListObserverChangeMove<>(toMove, this, indexOfElementToMove, newList, newListIndex, true));
		notifyListeners(change); //notify this list first
		change = new ListObserverChange<>(newList);
		change.setMoved(new ListObserverChangeMove<>(toMove, this, indexOfElementToMove, newList, newListIndex, false));
		newList.notifyListeners(change); //notify destination list second
	}

	/**
	 Moves an element at index indexOfElementToMove from this list into another {@link ListObserver}.
	 This method will result in {@link ListObserverChange#getMoved()} not being null.

	 @param indexOfElementToMove index of element to move inside this {@link ListObserver}
	 @param newList list to move the element to (will move element to end of list)
	 */
	public void move(int indexOfElementToMove, @NotNull ListObserver<E> newList) {
		move(indexOfElementToMove, newList, newList.size());
	}

	/**
	 Moves an element from this list into another {@link ListObserver}.
	 This method will result in {@link ListObserverChange#getMoved()} not being null.

	 @param element element to move inside this {@link ListObserver}
	 @param newList list to move the element to (will move element to end of list)
	 @return true if the operation succeed, or false if it didn't (happens when element couldn't be located)
	 */
	public boolean move(@NotNull E element, @NotNull ListObserver<E> newList) {
		return move(element, newList, newList.size());
	}

	/** Adds a listener. If the listener already has been added, will not be added again */
	public void addListener(@NotNull ListObserverListener<E> l) {
		if (listeners.contains(l)) {
			return;
		}
		listeners.add(l);
	}

	/**
	 Removes a listener.
	 */
	@Override
	public void removeListener(@NotNull ListObserverListener<E> l) {
		listeners.remove(l);
	}

	@Override
	@NotNull
	public ReadOnlyList<ListObserverListener<E>> getListeners() {
		return new ReadOnlyList<>(listeners);
	}

	@Override
	public void clearListeners() {
		listeners.clear();
	}


	/** Invoked to notify all listeners of a change */
	protected void notifyListeners(@NotNull ListObserverChange<E> change) {
		for (ListObserverListener<E> l : listeners) {
			l.onChanged(this, change);
		}
	}

	private void boundTest(int index) {
		if (index < 0 || index >= list.size()) {
			throw new IndexOutOfBoundsException("index is out of range. index:" + index + " size of list:" + list.size());
		}
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(@NotNull Object o) {
		return list.contains(o);
	}

	@NotNull
	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@NotNull
	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@NotNull
	@Override
	public <T> T[] toArray(@NotNull T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(@NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c) {
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
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	@NotNull
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return list.toString();
	}

	/** Clears the list and then adds all items */
	public void setAll(@NotNull List<E> list) {
		clear();
		addAll(list);
	}
}
