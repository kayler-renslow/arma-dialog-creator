package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 Simple value listener interface.

 @author Kayler
 @since 1/7/2019. */
public interface NotNullValueListener<V> extends ObserverListener {
	/**
	 The value was updated. However, the value is guaranteed to be different.

	 @param observer observer
	 @param oldValue old value
	 @param newValue new value
	 */
	void valueUpdated(@NotNull NotNullValueObserver<V> observer, @NotNull V oldValue, @NotNull V newValue);

}
