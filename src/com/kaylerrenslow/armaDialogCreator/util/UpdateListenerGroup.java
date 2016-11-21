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
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 Created by Kayler on 07/05/2016.
 */
public class UpdateListenerGroup<T> {
	private final LinkedList<UpdateGroupListener<T>> updateListeners = new LinkedList<>();
	private final LinkedList<UpdateListenerGroup<T>> chain = new LinkedList<>();
	private final LinkedList<UpdateGroupListener<T>> newListeners = new LinkedList<>();
	private boolean iterating = false; //prevent CoModificationException

	/** Will add the given listener. If the listener has already been added, will do nothing (no duplicates allowed). */
	public void addListener(@NotNull UpdateGroupListener<T> listener) {
		if (updateListeners.contains(listener)) {
			return;
		}
		if (iterating) {
			newListeners.add(listener);
		} else {
			updateListeners.add(listener);
		}
	}

	public boolean removeListener(@NotNull UpdateGroupListener<T> listener) {
		return updateListeners.remove(listener);
	}

	public void update(@Nullable T data) {
		iterating = true;
		for (UpdateGroupListener<T> updateListener : updateListeners) {
			updateListener.update(this, data);
		}
		for (UpdateListenerGroup<T> group : chain) {
			group.update(data);
		}
		iterating = false;
		while (newListeners.size() > 0) {
			updateListeners.add(newListeners.pop());
		}
	}


	/**
	 Chain this group and the given group together. Whenever this group gets an update via {@link #update(Object)}, the provided group will also receive the update.

	 @param updateGroup group to chain
	 @see #unchain(UpdateListenerGroup)
	 */
	public void chain(@NotNull UpdateListenerGroup<T> updateGroup) {
		chain.add(updateGroup);
	}

	/**
	 Remove the given group from the chain

	 @param updateGroup group to unchain
	 @see #chain(UpdateListenerGroup)
	 */
	public void unchain(@NotNull UpdateListenerGroup<T> updateGroup) {
		chain.remove(updateGroup);
	}
}
