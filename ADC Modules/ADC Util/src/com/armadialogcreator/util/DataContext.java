package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 Created by Kayler on 07/19/2016.
 */
public class DataContext {
	private final HashMap<String, Object> map = new HashMap<>();
	
	/**
	 Get the value associated with the given key. If the value is null and {@link Key#getDefaultValue()} != null, will place {@link Key#getDefaultValue()} in the context and return it.
	 If the value is not null or {@link Key#getDefaultValue()} is null, will return whatever is stored inside the context.
	 */
	public Object getValue(@NotNull Key<?> key) {
		Object o = map.get(key.getName());
		if (o == null && key.getDefaultValue() != null) {
			map.put(key.getName(), key.getDefaultValue());
			return key.getDefaultValue();
		}
		return o;
	}
	
	/**
	 Get the value with the given {@link Key} instance name ({@link Key#getName()}).
	 
	 @param keyName key name
	 @return value, or null if not set.
	 */
	public Object getValue(@NotNull String keyName) {
		return map.get(keyName);
	}
	
	/** Place the new value inside the context. Will return whatever was inside previously */
	@Nullable
	public Object put(@NotNull Key<?> key, @Nullable Object value) {
		return map.put(key.getName(), value);
	}
	
	/**
	 Check if all the given {@link Key}'s are set (not null).
	 
	 @param keys {@link Key}'s to check
	 @return true if all keys are not null, or false if at least 1 is null
	 */
	public boolean keysSet(@NotNull Key<?>... keys) {
		for (Key key : keys) {
			if (map.get(key.getName()) == null) {
				return false;
			}
		}
		return true;
	}
}
