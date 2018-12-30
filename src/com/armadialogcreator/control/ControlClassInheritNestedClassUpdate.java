package com.armadialogcreator.control;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 08/14/2017 */
public class ControlClassInheritNestedClassUpdate implements ControlClassUpdate {
	private final ControlClass owner;
	private final ControlClass nested;
	private final boolean inherited;

	public ControlClassInheritNestedClassUpdate(@NotNull ControlClass owner, @NotNull ControlClass nested, boolean inherited) {
		this.owner = owner;
		this.nested = nested;
		this.inherited = inherited;
	}

	@Override
	@NotNull
	public ControlClass getOwnerControlClass() {
		return owner;
	}

	/** @return the nested {@link ControlClass} that is owned by {@link #getOwnerControlClass()} and was inherited/overridden */
	@NotNull
	public ControlClass getNested() {
		return nested;
	}

	/** @return true if {@link #getNested()} was inherited, or false if it was overridden */
	public boolean isInherited() {
		return inherited;
	}
}
