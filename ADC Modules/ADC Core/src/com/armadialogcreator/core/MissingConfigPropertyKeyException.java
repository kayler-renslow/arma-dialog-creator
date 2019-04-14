package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/03/2019 */
public class MissingConfigPropertyKeyException extends RuntimeException {
	private final String name;

	public MissingConfigPropertyKeyException(@NotNull String name) {
		this.name = name;
	}

	@NotNull
	public String getMissingKey() {
		return name;
	}
}
