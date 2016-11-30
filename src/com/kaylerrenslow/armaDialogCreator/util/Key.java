package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used in conjunction with {@link DataContext}

 @author Kayler
 @since 07/19/2016. */
public class Key<V> {
	private final String name;
	private final V defaultValue;

	public Key(@NotNull String name, @Nullable V defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public Key(@NotNull String name) {
		this(name, null);
	}

	@NotNull
	public String getName() {
		return name;
	}

	public V getDefaultValue() {
		return defaultValue;
	}

	/** Will execute {@link DataContext#put(Key, Object)} with this key as the parameter. */
	@Nullable
	public V put(@NotNull DataContext context, @Nullable V value) {
		return (V) context.put(this, value);
	}

	/** Will execute {@link DataContext#getValue(Key)}} with this key as the parameter. */
	@Nullable
	public V get(@NotNull DataContext context) {
		return (V) context.getValue(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Key<?> key = (Key<?>) o;
		return name.equals(key.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
