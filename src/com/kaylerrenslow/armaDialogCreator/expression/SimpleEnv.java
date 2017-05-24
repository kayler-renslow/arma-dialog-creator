package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 A simple implementation of {@link Env} that allows for adding identifiers to the env and removing identifiers from the env.

 @author Kayler
 @since 07/15/2016. */
public class SimpleEnv implements Env {
	private HashMap<String, Value> map = new HashMap<>();

	@Override
	public Value put(@NotNull String identifier, Value v) {
		return map.put(identifier, v);
	}

	@Override
	public Value remove(@NotNull String identifier) {
		return map.remove(identifier);
	}

	@Override
	@Nullable
	public Value getValue(@NotNull String identifier) {
		return map.get(identifier);
	}
}
