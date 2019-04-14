package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @see MapObserver#replace(K, V)
 @since 1/7/2019 */
public class MapObserverChangeReplace<K, V> {
	private final K key;
	private final V oldValue;
	private final V newValue;

	public MapObserverChangeReplace(@NotNull K key, @Nullable V oldValue, @NotNull V newValue) {
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@NotNull
	public V getNewValue() {
		return newValue;
	}

	@NotNull
	public K getKey() {
		return key;
	}

	@Nullable
	public V getOldValue() {
		return oldValue;
	}
}
