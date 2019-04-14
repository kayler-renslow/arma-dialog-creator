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
}
