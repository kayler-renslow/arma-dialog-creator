/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
