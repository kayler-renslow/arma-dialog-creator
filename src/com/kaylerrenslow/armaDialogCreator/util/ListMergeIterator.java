/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 An iterator used to iterate over all elements across several lists. In times where merging is expensive, this is a cheap way of iterating over each of the items

 @author Kayler
 @since 08/07/2016. */
public class ListMergeIterator<E, L extends List<E>> implements Iterator<E>, Iterable<E> {
	private final boolean backwards;
	private final List<L> lists;
	private int ind;
	private final LinkedList<L> stack = new LinkedList<>();
	private List<E> current;
	private boolean used = false;


	/**
	 @param backwards if true, will iterate starting from list at lists.size-1 to 0. For each list being iterated, the list iteration itself will start at index list.size-1 and end at 0.<br>
	 If backwards is false, will iterate over the lists from index 0 to size - 1. For each list being iterated, will be iterated from index 0 to list.size - 1
	 @param lists lists to iterate over. <b>NOTE: best if the list is a random access list</b>
	 */
	public ListMergeIterator(boolean backwards, @NotNull List<L> lists) {
		this.backwards = backwards;
		this.lists = lists;
		if (backwards) {
			for (int i = lists.size() - 1; i >= 0; i--) {
				stack.push(lists.get(i));
			}

			ind = stack.peek().size() - 1;
		} else {
			for (L list : lists) {
				stack.push(list);
			}
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
			for (List<E> list : stack) {
				if (list.size() != 0) {
					hasAnotherInd = true;
					break;
				}
			}
			return hasAnotherInd;
		}
		if (ind < current.size()) {
			return true;
		}
		boolean hasAnotherInd = false;
		for (List<E> list : stack) {
			if (list.size() != 0) {
				hasAnotherInd = true;
				break;
			}
		}
		return hasAnotherInd;
	}


	@Override
	public E next() {
		used = true;
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
		while (ind >= current.size()) {
			current = stack.pop();
			ind = 0;
		}
		return current.get(ind++);
	}

	@Override
	public Iterator<E> iterator() {
		if (!used) {
			return this;
		}
		return new ListMergeIterator<>(backwards, lists);
	}
}
