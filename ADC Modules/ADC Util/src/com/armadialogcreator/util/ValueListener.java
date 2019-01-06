package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Simple value listener interface.

 @author Kayler
 @since 05/31/2016. */
public interface ValueListener<V> extends ObserverListener {
	/**
	 The value was updated. However, the value is guaranteed to be different.

	 @param observer observer
	 @param oldValue old value
	 @param newValue new value
	 */
	void valueUpdated(@NotNull ValueObserver<V> observer, @Nullable V oldValue, @Nullable V newValue);

	/**
	 Have the {@link ValueObserver} automatically check if this listener has expired.
	 If the listener has been expired, the {@link ValueObserver} will remove it automatically.
	 The time that it is removed is entirely up to the {@link ValueObserver}.<p>
	 <p>
	 Default return value is false.

	 @return true if expired, false if not expired
	 */
	default boolean hasExpired() {
		return false;
	}
}
