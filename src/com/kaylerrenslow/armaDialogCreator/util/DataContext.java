package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 Created by Kayler on 07/19/2016.
 */
public class DataContext {
	private final HashMap<Key, Object> map = new HashMap<>();
	
	/**
	 Get the value associated with the given key. If the value is null and {@link Key#getDefaultValue()} != null, will place {@link Key#getDefaultValue()} in the context and return it.
	 If the value is not null or {@link Key#getDefaultValue()} is null, will return whatever is stored inside the context.
	 */
	public Object getValue(@NotNull Key<?> key) {
		Object o = map.get(key);
		if (o == null && key.getDefaultValue() != null) {
			map.put(key, key.getDefaultValue());
			return key.getDefaultValue();
		}
		return o;
	}
	
	@Nullable
	/**Place the new value inside the context. Will return whatever was inside previously*/
	public Object put(@NotNull Key<?> key, @Nullable Object value) {
		return map.put(key, value);
	}
}
