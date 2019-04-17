package com.armadialogcreator.core;

import com.armadialogcreator.util.IteratorIterable;
import com.armadialogcreator.util.MapObserver;
import com.armadialogcreator.util.MapObserverListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 @author K
 @since 01/03/2019 */
public class ConfigPropertySet implements Iterable<Map.Entry<String, ConfigProperty>> {
	private final MapObserver<String, ConfigProperty> map = new MapObserver<>(new HashMap<>());

	public final boolean putPropertyIfAbsent(@NotNull ConfigProperty p) {
		ConfigProperty property = map.putIfAbsent(p.getName().toLowerCase(), p);
		return property == p;
	}

	@Nullable
	public final ConfigProperty putProperty(@NotNull ConfigProperty property) {
		return map.put(property.getName().toLowerCase(), property);
	}

	public final void replaceProperty(@NotNull ConfigProperty newProperty) {
		ConfigProperty old = map.replace(newProperty.getName().toLowerCase(), newProperty);
		if (old != null) {
			old.invalidate();
		}
	}

	public final boolean removeProperty(@NotNull ConfigProperty property) {
		return removeProperty(property.getName()) != null;
	}

	@Nullable
	public final ConfigProperty removeProperty(@NotNull String name) {
		ConfigProperty removed = map.remove(name.toLowerCase());
		if (removed != null) {
			removed.invalidate();
		}
		return removed;
	}

	public final void clearProperties() {
		for (Map.Entry<String, ConfigProperty> entry : map.entrySet()) {
			entry.getValue().invalidate();
		}
		map.clear();
	}


	/**
	 Get the {@link ConfigProperty} instance for the given property name ({@link ConfigProperty#getName()}).

	 @return the ControlProperty instance
	 @throws MissingConfigPropertyKeyException when the key couldn't be found
	 @see #findPropertyNullable(String)
	 */
	@NotNull
	public final ConfigProperty findProperty(@NotNull String name) throws MissingConfigPropertyKeyException {
		ConfigProperty c = findPropertyNullable(name);
		if (c != null) {
			return c;
		}
		throw new MissingConfigPropertyKeyException(name);
	}

	/**
	 Get the {@link ConfigProperty} instance for the given property name ({@link ConfigProperty#getName()}).

	 @return the ControlProperty instance
	 @throws MissingConfigPropertyKeyException when the key couldn't be found
	 @see #findPropertyNullable(String)
	 */
	@NotNull
	public final ConfigProperty findProperty(@NotNull ConfigPropertyKey key) throws MissingConfigPropertyKeyException {
		ConfigProperty c = findPropertyNullable(key.getPropertyName());
		if (c != null) {
			return c;
		}
		throw new MissingConfigPropertyKeyException(key.getPropertyName());
	}

	/**
	 Get the {@link ConfigProperty} instance for the given property name.

	 @return the ControlProperty instance, or null if couldn't be found
	 @see #findProperty(String)
	 */
	@Nullable
	public final ConfigProperty findPropertyNullable(@NotNull ConfigPropertyKey key) {
		return findPropertyNullable(key.getPropertyName());
	}

	/**
	 Get the {@link ConfigProperty} instance for the given property name.

	 @return the ControlProperty instance, or null if couldn't be found
	 @see #findProperty(String)
	 */
	@Nullable
	public final ConfigProperty findPropertyNullable(@NotNull String name) {
		return map.get(name.toLowerCase());
	}

	@NotNull
	@Override
	public final Iterator<Map.Entry<String, ConfigProperty>> iterator() {
		return map.entrySet().iterator();
	}

	@NotNull
	public final Iterable<ConfigProperty> iterable() {
		return new IteratorIterable<>(map.values().iterator());
	}

	public void addAllProperties(@NotNull ConfigPropertySet set) {
		set.map.forEach((s, configProperty) -> {
			map.put(s.toLowerCase(), configProperty);
		});
	}

	public final void addPropertiesSetListener(@NotNull MapObserverListener<String, ConfigProperty> listener) {
		map.addListener(listener);
	}

	public final void removePropertiesSetListener(@NotNull MapObserverListener<String, ConfigProperty> listener) {
		map.removeListener(listener);
	}

	public int size() {
		return map.size();
	}
}
