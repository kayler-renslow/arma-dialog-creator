package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 @author K
 @since 01/07/2019 */
public class ListsArrayIterator<T> implements Iterator<T>, Iterable<T> {
	private final Iterator<?>[] iters;
	private int iterInd = 0;
	private int lastCheckIterInd = -1;

	public ListsArrayIterator(@NotNull List<T>[] ts) {
		iters = new Iterator<?>[ts.length];
		int i = 0;
		for (List<T> t : ts) {
			iters[i++] = t.iterator();
		}
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (lastCheckIterInd != iterInd) {
			while (iterInd < iters.length && !iters[iterInd].hasNext()) {
				iterInd++;
			}
			lastCheckIterInd = iterInd;
		}
		return iterInd < iters.length && iters[iterInd].hasNext();
	}

	@Override
	public T next() {
		return (T) iters[iterInd].next();
	}
}
