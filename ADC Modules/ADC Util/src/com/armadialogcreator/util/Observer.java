package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/04/2019 */
public interface Observer<L extends ObserverListener> {
	void addListener(@NotNull L listener);

	void removeListener(@NotNull L listener);

	@NotNull ReadOnlyList<L> getListeners();

	void clearListeners();

	void invalidate();
}
