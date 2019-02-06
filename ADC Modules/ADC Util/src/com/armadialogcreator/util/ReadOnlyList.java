package com.armadialogcreator.util;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 Creates a wrapper around a list such that it is read only. All methods that could mutate the underlying list will throw a {@link IllegalStateException}
 Also, any subsequent iterators will also not be able to mutate the list.

 @author Kayler
 @since 06/07/2016. */
public class ReadOnlyList<E> implements List<E> {
	private final List<E> dataList;

	public ReadOnlyList(List<E> dataList) {
		this.dataList = dataList;
	}

	/** Constructs a list with an ArrayList as the underlying list. The given data will populate the list. */
	public ReadOnlyList(E[] data) {
		dataList = new ArrayList<>(data.length);
		Collections.addAll(dataList, data);
	}

	/** Return true if the underlying list supports random access (extends {@link RandomAccess}) */
	public boolean randomAccess() {
		return dataList instanceof RandomAccess;
	}

	public int size() {
		return dataList.size();
	}

	public boolean isEmpty() {
		return dataList.isEmpty();
	}

	public boolean contains(Object o) {
		return dataList.contains(o);
	}

	public E get(int index) {
		return dataList.get(index);
	}

	@Override
	public E set(int index, @Flow(targetIsContainer = true) E element) {
		noMutateException();
		return null;
	}

	@Override
	public void add(int index, @Flow(targetIsContainer = true) E element) {
		noMutateException();
	}

	@Override
	public E remove(int index) {
		noMutateException();
		return null;
	}

	@Override
	public int indexOf(Object o) {
		return dataList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return dataList.lastIndexOf(o);
	}

	@Override
	public @NotNull ListIterator<E> listIterator() {
		return new MyIterator(0);
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator(int index) {
		return new MyIterator(index);
	}

	@NotNull
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return dataList.subList(fromIndex, toIndex);
	}

	@NotNull
	public Iterator<E> iterator() {
		return new MyIterator();
	}

	private final class MyIterator implements ListIterator<E>, Iterator<E> {
		private ListIterator<E> listIter;
		private Iterator<E> iter;
		private boolean useNormalIterator;

		public MyIterator() {
			iter = dataList.iterator();
			useNormalIterator = true;
		}

		public MyIterator(int index) {
			listIter = dataList.listIterator(index);
			useNormalIterator = false;
		}


		@Override
		public boolean hasNext() {
			return useNormalIterator ? iter.hasNext() : listIter.hasNext();
		}

		@Override
		public E next() {
			return useNormalIterator ? iter.next() : listIter.next();
		}

		@Override
		public boolean hasPrevious() {
			if (useNormalIterator) {
				throw new UnsupportedOperationException();
			}
			return listIter.hasPrevious();
		}

		@Override
		public E previous() {
			if (useNormalIterator) {
				throw new UnsupportedOperationException();
			}
			return listIter.previous();
		}

		@Override
		public int nextIndex() {
			if (useNormalIterator) {
				throw new UnsupportedOperationException();
			}
			return listIter.nextIndex();
		}

		@Override
		public int previousIndex() {
			if (useNormalIterator) {
				throw new UnsupportedOperationException();
			}
			return listIter.previousIndex();
		}

		@Override
		public void remove() {
			noMutateException();
		}

		@Override
		public void set(E e) {
			noMutateException();
		}

		@Override
		public void add(E e) {
			noMutateException();
		}
	}

	private void noMutateException() {
		throw new IllegalStateException("can't mutate read only list");
	}

	@NotNull
	@Override
	public Object[] toArray() {
		return dataList.toArray();
	}

	@NotNull
	@Override
	public <T> T[] toArray(T[] a) {
		return dataList.toArray(a);
	}

	@Override
	public boolean add(@Flow(targetIsContainer = true) E e) {
		noMutateException();
		return false;
	}

	@Override
	public boolean remove(Object o) {
		noMutateException();
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return dataList.containsAll(c);
	}

	@Override
	public boolean addAll(@Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c) {
		noMutateException();
		return false;
	}

	@Override
	public boolean addAll(int index, @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c) {
		noMutateException();
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		noMutateException();
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		noMutateException();
		return false;
	}

	@Override
	public void clear() {
		noMutateException();
	}

	@Override
	public String toString() {
		return dataList.toString();
	}
}
