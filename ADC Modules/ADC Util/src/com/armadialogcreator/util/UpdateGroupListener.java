package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/05/2016.
 */
public interface UpdateGroupListener<T> {
	/**
	 Invoked when {@link UpdateListenerGroup#update(Object)} was called.

	 @param group group where the update occurred
	 @param data data that may have been passed
	 */
	void update(@NotNull UpdateListenerGroup<T> group, @NotNull T data);

	/**
	 Have the {@link UpdateListenerGroup} automatically check if this listener has expired.
	 If the listener has been expired, the {@link UpdateListenerGroup} will remove it automatically.
	 The time that it is removed is entirely up to the {@link UpdateListenerGroup}.<p>
	 <p>
	 Default return value is false.

	 @return true if expired, false if not expired
	 */
	default boolean hasExpired() {
		return false;
	}

}
