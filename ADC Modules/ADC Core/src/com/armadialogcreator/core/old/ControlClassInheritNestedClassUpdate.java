package com.armadialogcreator.core.old;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 08/14/2017 */
public class ControlClassInheritNestedClassUpdate implements ControlClassUpdate {
	private final ControlClassOld owner;
	private final ControlClassOld nested;
	private final boolean inherited;

	public ControlClassInheritNestedClassUpdate(@NotNull ControlClassOld owner, @NotNull ControlClassOld nested, boolean inherited) {
		this.owner = owner;
		this.nested = nested;
		this.inherited = inherited;
	}

	@Override
	@NotNull
	public ControlClassOld getOwnerControlClass() {
		return owner;
	}

	/** @return the nested {@link ControlClassOld} that is owned by {@link #getOwnerControlClass()} and was inherited/overridden */
	@NotNull
	public ControlClassOld getNested() {
		return nested;
	}

	/** @return true if {@link #getNested()} was inherited, or false if it was overridden */
	public boolean isInherited() {
		return inherited;
	}
}
