package com.kaylerrenslow.armaDialogCreator.util;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 Creates a wrapper around a list such that it is read only

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
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, @Flow(targetIsContainer = true) E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException();
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
		return (ListIterator<E>) dataList.iterator();
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator(int index) {
		return dataList.listIterator(index);
	}

	@NotNull
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return dataList.subList(fromIndex, toIndex);
	}

	@NotNull
	public Iterator<E> iterator() {
		return dataList.iterator();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return dataList.containsAll(c);
	}

	@Override
	public boolean addAll(@Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return dataList.toString();
	}
}
