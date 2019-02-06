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

	/**
	 Have the {@link NotNullValueObserver} automatically check if this listener has expired.
	 If the listener has been expired, the {@link NotNullValueObserver} will remove it automatically.
	 The time that it is removed is entirely up to the {@link NotNullValueObserver}.<p>
	 <p>
	 Default return value is false.

	 @return true if expired, false if not expired
	 */
	default boolean hasExpired() {
		return false;
	}
}
