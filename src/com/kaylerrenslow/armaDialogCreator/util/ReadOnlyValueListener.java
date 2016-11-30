package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 09/16/2016.
 */
public interface ReadOnlyValueListener<V> {
	/**
	The value was updated. However, the value is guaranteed to be different.

	@param observer observer
	@param oldValue old value
	@param newValue new value
	 */
	void valueUpdated(@NotNull ReadOnlyValueObserver<V> observer, @Nullable V oldValue, @Nullable V newValue);
}
