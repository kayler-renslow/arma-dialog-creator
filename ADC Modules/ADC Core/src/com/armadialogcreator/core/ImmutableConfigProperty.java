package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/12/2019 */
public class ImmutableConfigProperty {
	private final SerializableValue value;
	private final Macro macro;
	private final String name;

	public ImmutableConfigProperty(@NotNull ConfigProperty property) {
		this.value = property.getValue();
		this.macro = property.getBoundMacro();
		this.name = property.getName();
	}

	public ImmutableConfigProperty(@NotNull String name, @NotNull SerializableValue value, @Nullable Macro macro) {
		this.value = value;
		this.macro = macro;
		this.name = name;
	}

	@NotNull
	public SerializableValue getValue() {
		return value;
	}

	@Nullable
	public Macro getMacro() {
		return macro;
	}

	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public ConfigProperty newProperty() {
		ConfigProperty property = new ConfigProperty(name, value);
		property.bindToMacro(macro);
		return property;
	}
}
