package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/07/2019 */
public interface SimpleMap<K, V> {
	@Nullable V getValue(@NotNull K key);
}
