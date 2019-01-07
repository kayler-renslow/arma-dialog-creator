package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 08/13/2017 */
public class ControlClassNestedClassUpdate implements ControlClassUpdate {

	private final ControlClassOld ownerControlClass;
	private final ControlClassOld nested;
	private final ControlClassUpdate nestedClassUpdate;

	public ControlClassNestedClassUpdate(@NotNull ControlClassOld ownerControlClass, @NotNull ControlClassOld nested, @NotNull ControlClassUpdate nestedClassUpdate) {
		this.ownerControlClass = ownerControlClass;
		this.nested = nested;
		this.nestedClassUpdate = nestedClassUpdate;
	}

	@Override
	@NotNull
	public ControlClassOld getOwnerControlClass() {
		return ownerControlClass;
	}

	/** @return the {@link ControlClassOld} that had an update (this is a nested {@link ControlClassOld} of {@link #getOwnerControlClass()}) */
	@NotNull
	public ControlClassOld getNested() {
		return nested;
	}

	@NotNull
	public ControlClassUpdate getNestedClassUpdate() {
		return nestedClassUpdate;
	}
}
