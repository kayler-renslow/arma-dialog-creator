package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/04/2019 */
public interface Observer<L extends ObserverListener> {
	/** Adds a listener to the observer */
	void addListener(@NotNull L listener);

	/** Removes a listener the observer */
	void removeListener(@NotNull L listener);

	/** Gets all listeners in the observer */
	@NotNull ReadOnlyList<L> getListeners();

	/** Clears all listeners from the observer and also invokes {@link ObserverListener#listenerDetached()} on each once */
	void clearListeners();

	/** Invokes {@link ObserverListener#observerInvalidated()} on each listener and then subsequently {@link #clearListeners()} */
	default void invalidate() {
		for (L l : getListeners()) {
			l.observerInvalidated();
		}
		clearListeners();
	}
}
