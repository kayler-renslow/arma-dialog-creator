package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 This is an alternative to {@link javafx.collections.ObservableMap}.
 Advantages of this over the other is this supports moving. In the other one, in order to "move"
 something, you would have to remove it and then add it back. This would fire 2 events: remove and add.
 Although this implementation functionally does the same thing, only one event is fired for
 moving and thus makes it easier to detect and manage.

 @author Kayler
 @since 1/7/2019. */
public class MapObserver<K, V> implements Map<K, V>, Observer<MapObserverListener<K, V>> {
	private final Map<K, V> map;
	private final LinkedList<MapObserverListener<K, V>> listeners = new LinkedList<>();

	public MapObserver(@NotNull Map<K, V> map) {
		this.map = map;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Nullable
	@Override
	public V put(K key, V value) {
		V v = map.put(key, value);
		MapObserverChange<K, V> change = new MapObserverChange<>(this);
		change.setPut(new MapObserverChangePut<>(key, value, v));
		notifyListeners(change);
		return v;
	}

	@Override
	public V remove(Object key) {
		V v = map.remove(key);
		if (v == null) {
			return null;
		}
		MapObserverChange<K, V> change = new MapObserverChange<>(this);
		change.setRemoved(new MapObserverChangeRemove<>((K) key, v)); //unchecked cast but who cares
		notifyListeners(change);
		return v;
	}

	@Override
	public void putAll(@NotNull Map<? extends K, ? extends V> m) {
		m.forEach((k, v) -> {
			put(k, v);
		});
	}

	/** Remove all from list. */
	@Override
	public void clear() {
		MapObserverChange<K, V> change = new MapObserverChange<>(this);
		change.setCleared();
		notifyListeners(change);
		map.clear(); //clear after notify
	}

	@NotNull
	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@NotNull
	@Override
	public Collection<V> values() {
		return map.values();
	}

	@NotNull
	@Override
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		map.forEach(action);
	}

	@Override
	public boolean remove(Object key, Object value) {
		boolean removed = map.remove(key, value);
		if (!removed) {
			return false;
		}
		MapObserverChange<K, V> change = new MapObserverChange<>(this);
		change.setRemoved(new MapObserverChangeRemove<>((K) key, (V) value)); //unchecked cast but who cares
		notifyListeners(change);
		return map.remove(key, value);
	}

	@Nullable
	@Override
	public V replace(K key, V value) {
		V replaced = map.replace(key, value);
		MapObserverChange<K, V> change = new MapObserverChange<>(this);
		change.setReplaced(new MapObserverChangeReplace<>(key, replaced, value));
		notifyListeners(change);
		return replaced;
	}

	public boolean move(@NotNull K key, @NotNull MapObserver<K, V> newMap) {
		V remove = map.remove(key);
		if (remove == null) {
			return false;
		}
		newMap.put(key, remove);
		MapObserverChange<K, V> change = new MapObserverChange<>(this);
		change.setMoved(new MapObserverChangeMove<>(key, remove, this, newMap, true));
		notifyListeners(change);

		change = new MapObserverChange<>(newMap);
		change.setMoved(new MapObserverChangeMove<>(key, remove, this, newMap, false));
		newMap.notifyListeners(change); //notify destination map second
		return true;
	}

	/** Adds a listener. If the listener already has been added, will not be added again */
	public void addListener(@NotNull MapObserverListener<K, V> l) {
		if (listeners.contains(l)) {
			return;
		}
		listeners.add(l);
	}

	/**
	 Removes a listener.
	 */
	@Override
	public void removeListener(@NotNull MapObserverListener<K, V> l) {
		listeners.remove(l);
	}

	@Override
	@NotNull
	public ReadOnlyList<MapObserverListener<K, V>> getListeners() {
		return new ReadOnlyList<>(listeners);
	}

	@Override
	public void clearListeners() {
		listeners.clear();
	}

	@Override
	public String toString() {
		return map.toString();
	}

	protected void notifyListeners(@NotNull MapObserverChange<K, V> change) {
		for (MapObserverListener<K, V> l : listeners) {
			l.onChanged(this, change);
		}
	}
}
