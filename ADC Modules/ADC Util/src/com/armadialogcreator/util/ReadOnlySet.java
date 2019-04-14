package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 @author K
 @since 02/13/2019 */
public class ReadOnlySet<T> implements Set<T> {
	private final Set<T> set;

	public ReadOnlySet(@NotNull Set<T> set) {
		this.set = set;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return new ReadOnlyIterator<>(set.iterator());
	}

	@NotNull
	@Override
	public Object[] toArray() {
		return set.toArray();
	}

	@NotNull
	@Override
	public <T1> T1[] toArray(@NotNull T1[] a) {
		return set.toArray(a);
	}

	@Override
	public boolean add(T t) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return set.containsAll(c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends T> c) {
		return false;
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return false;
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {

	}
}
