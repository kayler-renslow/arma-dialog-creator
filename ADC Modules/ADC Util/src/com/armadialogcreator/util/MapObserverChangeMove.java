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
	private final boolean isEntryUpdate;

	public MapObserverChangeMove(@NotNull K key, @NotNull V value, @NotNull MapObserver<K, V> originalMap,
								 @NotNull MapObserver<K, V> newMap, boolean isEntryUpdate) {
		this.key = key;
		this.value = value;
		this.originalMap = originalMap;
		this.newMap = newMap;
		this.isEntryUpdate = isEntryUpdate;
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

	/**
	 Return true if this update comes from the moved element being moved into it's new
	 list via {@link MapObserver#move(K, MapObserver)},
	 or false if this update captures the moved
	 element leaving the old map.

	 @see ListObserver#move(K, ListObserver)
	 */
	public boolean isEntryUpdate() {
		return isEntryUpdate;
	}
}
