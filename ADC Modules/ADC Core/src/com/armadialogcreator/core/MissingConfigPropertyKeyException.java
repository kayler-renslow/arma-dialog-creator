package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/03/2019 */
public class MissingConfigPropertyKeyException extends Exception {
	private final ConfigPropertyKey key;

	public MissingConfigPropertyKeyException(@NotNull ConfigPropertyKey key) {
		this.key = key;
	}

	@NotNull
	public ConfigPropertyKey getMissingKey() {
		return key;
	}
}
