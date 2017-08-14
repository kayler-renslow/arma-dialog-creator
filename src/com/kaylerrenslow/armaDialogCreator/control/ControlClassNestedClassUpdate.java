package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 08/13/2017 */
public class ControlClassNestedClassUpdate implements ControlClassUpdate {

	private final ControlClass ownerControlClass;
	private final ControlClass nested;
	private final ControlClassUpdate nestedClassUpdate;

	public ControlClassNestedClassUpdate(@NotNull ControlClass ownerControlClass, @NotNull ControlClass nested, @NotNull ControlClassUpdate nestedClassUpdate) {
		this.ownerControlClass = ownerControlClass;
		this.nested = nested;
		this.nestedClassUpdate = nestedClassUpdate;
	}

	@Override
	@NotNull
	public ControlClass getOwnerControlClass() {
		return ownerControlClass;
	}

	/** @return the {@link ControlClass} that had an update (this is a nested {@link ControlClass} of {@link #getOwnerControlClass()}) */
	@NotNull
	public ControlClass getNested() {
		return nested;
	}

	@NotNull
	public ControlClassUpdate getNestedClassUpdate() {
		return nestedClassUpdate;
	}
}
