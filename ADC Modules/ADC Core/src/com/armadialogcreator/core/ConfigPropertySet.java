package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyList;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 @author K
 @since 01/03/2019 */
public class ConfigPropertySet implements Iterable<ConfigProperty> {
	private final List<ConfigProperty> properties = new LinkedList<>();
	private final ReadOnlyList<ConfigProperty> propertiesRO = new ReadOnlyList<>(properties);
	private boolean immutable = false;
	private final UpdateListenerGroup<ConfigProperty> updateGroup = new UpdateListenerGroup<>();

	public final void makeImmutable() {
		this.immutable = true;
	}

	public final boolean isImmutable() {
		return immutable;
	}

	public final void addProperty(@NotNull ConfigProperty property) {
		if (immutable) {
			noMutateException();
			return;
		}
		properties.add(property);
	}

	public final boolean removeProperty(@NotNull ConfigProperty property) {
		if (immutable) {
			noMutateException();
		}
		return properties.remove(property);
	}

	public final boolean removeProperty(@NotNull ConfigPropertyKey key) {
		if (immutable) {
			noMutateException();
		}
		Iterator<ConfigProperty> iter = properties.iterator();
		while (iter.hasNext()) {
			ConfigProperty property = iter.next();
			if (property.getKey().keyEquals(key)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 Get the {@link ConfigProperty} instance for the given {@link ConfigPropertyKey}.

	 @return the ControlProperty instance
	 @throws MissingConfigPropertyKeyException when the key couldn't be found
	 @see #findPropertyNullable(ConfigPropertyKey)
	 */
	@NotNull
	public final ConfigProperty findProperty(@NotNull ConfigPropertyKey key) throws MissingConfigPropertyKeyException {
		ConfigProperty c = findPropertyNullable(key);
		if (c != null) {
			return c;
		}
		throw new MissingConfigPropertyKeyException(key);
	}

	/**
	 Get the {@link ConfigProperty} instance for the given {@link ConfigPropertyKey}.

	 @return the ControlProperty instance, or null if couldn't be found
	 @see #findProperty(ConfigPropertyKey)
	 */
	@Nullable
	public final ConfigProperty findPropertyNullable(@NotNull ConfigPropertyKey key) {
		for (ConfigProperty p : properties) {
			if (p.getKey().keyEquals(key)) {
				return p;
			}
		}
		return null;
	}

	@NotNull
	@Override
	public final Iterator<ConfigProperty> iterator() {
		return immutable ? propertiesRO.iterator() : properties.iterator();
	}


	private void noMutateException() {
		throw new IllegalStateException("can't mutate immutable set");
	}
}
