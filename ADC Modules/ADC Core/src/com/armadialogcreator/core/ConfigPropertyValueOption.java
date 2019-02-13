package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/23/2016.
 */
public class ConfigPropertyValueOption {
	@NotNull
	public final String value, description, displayName;

	public ConfigPropertyValueOption(@NotNull String displayName, @NotNull String value, @NotNull String description) {
		this.value = value;
		this.description = description;
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
