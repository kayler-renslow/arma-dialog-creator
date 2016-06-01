package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Simple value listener interface.
 Created on 05/31/2016. */
public interface ValueListener<V> {
	/** The value was updated. However, the value isn't guaranteed to be different. */
	void valueUpdated(@Nullable V oldValue, @Nullable V newValue);
}
