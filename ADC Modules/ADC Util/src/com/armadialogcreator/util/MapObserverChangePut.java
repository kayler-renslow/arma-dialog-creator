package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @see MapObserver#put(K, V)
 @since 1/7/2019 */
public class MapObserverChangePut<K, V> {
	private final K key;
	private final V value;
	private final V previousValue;

	public MapObserverChangePut(@NotNull K key, @NotNull V value, @Nullable V previousValue) {
		this.key = key;
		this.value = value;
		this.previousValue = previousValue;
	}

	@NotNull
	public K getKey() {
		return key;
	}

	@NotNull
	public V getValue() {
		return value;
	}

	@Nullable
	public V getPreviousValue() {
		return previousValue;
	}
}
