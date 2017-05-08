package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 A simple implementation of {@link Env} that allows for adding identifers to the env and removing identifiers from the env.

 @author Kayler
 @since 07/15/2016. */
public class SimpleEnv implements Env {
	private HashMap<String, Value> map = new HashMap<>();

	/**
	 Associates the specified value with the specified identifier in this env. If the env previously contained a mapping for the identifier, the old value is replaced.

	 @return the previous value associated with identifier, or null if there was no mapping for identifier. (A null return can also indicate that the map previously associated null with identifier.)
	 */
	public Value put(String identifier, Value v) {
		return map.put(identifier, v);
	}

	/**
	 Removes the mapping for the specified identifier from this env if present.

	 @return the previous value associated with identifier, or null if there was no mapping for identifier. (A null return can also indicate that the map previously associated null with identifier.)
	 */
	public Value remove(String identifier) {
		return map.remove(identifier);
	}

	@Override
	@Nullable
	public Value getValue(String identifier) {
		return map.get(identifier);
	}
}
