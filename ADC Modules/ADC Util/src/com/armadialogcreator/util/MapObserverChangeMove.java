package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see MapObserver#move(K, MapObserver)
 @since 1/7/2019 */
public class MapObserverChangeMove<K, V> {
	private final K key;
	private final V value;
	private final MapObserver<K, V> originalMap;
	private final MapObserver<K, V> newMap;
	private final boolean isSourceMapChange;

	public MapObserverChangeMove(@NotNull K key, @NotNull V value, @NotNull MapObserver<K, V> originalMap,
								 @NotNull MapObserver<K, V> newMap, boolean isSourceMapChange) {
		this.key = key;
		this.value = value;
		this.originalMap = originalMap;
		this.newMap = newMap;
		this.isSourceMapChange = isSourceMapChange;
	}

	/**
	 Return true if this change was created for the source map. Since 2 move changes are created
	 (one for source map and one for target map), this is helped to identify which change this instance was meant for.
	 The source map will always receive the first update.

	 @see MapObserver#move(Object, MapObserver)
	 */
	public boolean isSourceMapChange() {
		return isSourceMapChange;
	}

	@NotNull
	public MapObserver<K, V> getOriginalMap() {
		return originalMap;
	}

	@NotNull
	public MapObserver<K, V> getNewMap() {
		return newMap;
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
