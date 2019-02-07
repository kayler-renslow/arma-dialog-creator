package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 Created by Kayler on 07/05/2016.
 */
public class UpdateListenerGroup<T> {
	private final LinkedList<UpdateGroupListener<T>> updateListeners = new LinkedList<>();
	private final LinkedList<UpdateListenerGroup<T>> chain = new LinkedList<>();
	private final LinkedList<UpdateGroupListener<T>> newListeners = new LinkedList<>();
	private boolean iterating = false; //prevent CoModificationException

	/** Use this for when the T doesn't matter */
	public static final class NoData {
		private NoData() {
		}
	}

	public static final NoData NoDataInstance = new NoData();


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

	public void update(@NotNull T data) {
		iterating = true;
		Iterator<UpdateGroupListener<T>> iter = updateListeners.iterator();
		while (iter.hasNext()) {
			UpdateGroupListener<T> updateListener = iter.next();
			if (updateListener.hasExpired()) {
				iter.remove();
				continue;
			}
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
