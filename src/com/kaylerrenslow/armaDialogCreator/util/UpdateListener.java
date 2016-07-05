package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/05/2016.
 */
public interface UpdateListener<T> {
	void update(@Nullable T data);
}
