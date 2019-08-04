package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

/**
 A similar backend implementation to {@link ArrayList}
 but instead of doubling the capacity of the backend array when it gets full,
 it only reallocates the array with the length incremented by a constant value (say 2).
 Therefore, this implementation is not a good choice for something that will have a lot of read and writes,
 but is good for something that will not have many read and writes and would prefer to have a small
 memory footprint.
 <p>
 The backend array will only de-allocate when there the capacity of the backend
 array minus the number of elements ({@link #size()}) is >= {@link #getIncrement()}

 @author Kayler
 @since 8/3/19. */
public class StubbornArrayList<E> implements List<E> {
	private Object[] data;
	private int size = 0;
	private static final Object[] EMPTY = new Object[0];
	private int increment = 2;

	public StubbornArrayList(int capacity) {
		data = new Object[capacity];
	}

	public StubbornArrayList() {
		data = EMPTY;
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		if (increment <= 0) {
			throw new IllegalArgumentException("increment <= 0: " + increment);
		}
		this.increment = increment;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size <= 0;
	}

	@Override
	public boolean contains(Object o) {
		for (Object ob : data) {
			if (ob.equals(o)) {
				return true;
			}
		}
		return false;
	}

	@NotNull
	@Override
	public Iterator<E> iterator() {
		return new Iter();
	}

	@NotNull
	@Override
	public Object[] toArray() {
		Object[] clone = new Object[size];
		for (int i = 0; i < clone.length; i++) {
			clone[i] = data[i];
		}
		return clone;
	}

	@NotNull
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(@NotNull T[] ts) {
		if (ts.length == 0) {
			return ts;
		}
		if (ts.length >= size) {
			int i = 0;
			for (i = 0; i < ts.length; i++) {
				ts[i] = (T) data[i];
			}
			for (; i < size; i++) {
				ts[i] = null;
			}
			return ts;
		}
		T[] neww = (T[]) Array.newInstance(ts[0].getClass(), size);
		for (int i = 0; i < neww.length; i++) {
			neww[i] = (T) data[i];
		}
		return neww;
	}

	@Override
	public boolean add(E e) {
		if (size + 1 >= data.length) {
			Object[] clone = new Object[size + this.increment];
			System.arraycopy(this.data, 0, clone, 0, size);
			this.data = clone;
		}
		this.data[this.size++] = e;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		//todo implement remove according to class level doc
		return false;
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> collection) {
		return false;
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends E> collection) {
		return false;
	}

	@Override
	public boolean addAll(int i, @NotNull Collection<? extends E> collection) {
		return false;
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> collection) {
		return false;
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> collection) {
		return false;
	}

	@Override
	public void clear() {

	}

	@Override
	public E get(int i) {
		return null;
	}

	@Override
	public E set(int i, E e) {
		return null;
	}

	@Override
	public void add(int i, E e) {

	}

	@Override
	public E remove(int i) {
		return null;
	}

	@Override
	public int indexOf(Object o) {
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		return 0;
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator() {
		return null;
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator(int i) {
		return null;
	}

	@NotNull
	@Override
	public List<E> subList(int i, int i1) {
		return null;
	}

	private class Iter implements Iterator<E> {
		private int cur = 0;

		@Override
		public boolean hasNext() {
			return cur < data.length;
		}

		@Override
		@SuppressWarnings("unchecked")
		public E next() {
			return (E) data[cur++];
		}
	}
}
