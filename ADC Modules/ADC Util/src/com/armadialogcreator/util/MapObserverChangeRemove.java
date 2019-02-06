package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see MapObserver#remove(Object, V)
 @since 1/7/2019 */
public class MapObserverChangeRemove<K, V> {
	private final K key;
	private final V value;

	public MapObserverChangeRemove(@NotNull K key, @NotNull V value) {
		this.key = key;
		this.value = value;
	}

	@NotNull
	public K getKey() {
		return key;
	}

	@NotNull
	public V getValue() {
		return value;
	}
}
