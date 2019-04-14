package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 @author K
 @since 01/06/2019 */
public class ListsIterator<T> implements Iterable<T>, ListIterator<T>, Iterator<T> {
	private final LinkedList<ListIterator<T>> itersStack = new LinkedList<>();
	private final boolean reverse;

	public ListsIterator(@NotNull List<List<T>> lists) {
		this(lists, false);
	}

	public ListsIterator(@NotNull List<List<T>> lists, boolean reverse) {
		this.reverse = reverse;
		if (reverse) {
			for (List<T> l : lists) {
				itersStack.addFirst(l.listIterator(l.size()));
			}
		} else {
			for (List<T> l : lists) {
				itersStack.add(l.listIterator());
			}
		}
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (itersStack.isEmpty()) {
			return false;
		}
		if (reverse) {
			return itersStack.peek().hasPrevious();
		}
		return itersStack.peek().hasNext();
	}

	@Override
	public T next() {
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		if (reverse) {
			while (!itersStack.isEmpty() && !itersStack.peek().hasPrevious()) {
				itersStack.pop();
			}
			if (itersStack.isEmpty()) {
				throw new IllegalStateException();
			}
			if (itersStack.peek().hasPrevious()) {
				return itersStack.peek().previous();
			}
			throw new IllegalStateException();
		}
		while (!itersStack.isEmpty() && !itersStack.peek().hasNext()) {
			itersStack.pop();
		}
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		if (itersStack.peek().hasNext()) {
			return itersStack.peek().next();
		}
		throw new IllegalStateException();
	}

	@Override
	public boolean hasPrevious() {
		if (itersStack.isEmpty()) {
			return false;
		}
		if (reverse) {
			return itersStack.peek().hasNext();
		}
		return itersStack.peek().hasPrevious();
	}

	@Override
	public T previous() {
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		if (reverse) {
			while (!itersStack.isEmpty() && !itersStack.peek().hasNext()) {
				itersStack.pop();
			}
			if (itersStack.isEmpty()) {
				throw new IllegalStateException();
			}
			if (itersStack.peek().hasNext()) {
				return itersStack.peek().next();
			}

		}
		while (!itersStack.isEmpty() && !itersStack.peek().hasPrevious()) {
			itersStack.pop();
		}
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		if (itersStack.peek().hasPrevious()) {
			return itersStack.peek().previous();
		}
		throw new IllegalStateException();
	}

	@Override
	public int nextIndex() {
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		if (reverse) {
			return itersStack.peek().previousIndex();
		}
		return itersStack.peek().nextIndex();
	}

	@Override
	public int previousIndex() {
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		if (reverse) {
			return itersStack.peek().nextIndex();
		}
		return itersStack.peek().previousIndex();
	}

	@Override
	public void remove() {
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		itersStack.peek().remove();
	}

	@Override
	public void set(T t) {
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		itersStack.peek().set(t);
	}

	@Override
	public void add(T t) {
		if (itersStack.isEmpty()) {
			throw new IllegalStateException();
		}
		itersStack.peek().add(t);
	}
}
