package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Simple value listener interface.

 @author Kayler
 @since 05/31/2016. */
public interface ValueListener<V> {
	/**
	 The value was updated. However, the value is guaranteed to be different.

	 @param observer observer
	 @param oldValue old value
	 @param newValue new value
	 */
	void valueUpdated(@NotNull ValueObserver<V> observer, @Nullable V oldValue, @Nullable V newValue);
}
