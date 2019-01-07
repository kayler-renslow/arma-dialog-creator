package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;

/**
 An update for when a {@link ControlClassOld} is inherited in {@link ControlClassOld}, but it wasn't in the
 {@link ControlClassOld} originally.

 @author Kayler
 @see ControlClassOld#extendControlClass(ControlClassOld)
 @since 06/12/2017 */
public class ControlClassTemporaryNestedClassUpdate implements ControlClassUpdate {
	private final ControlClassOld ownerControlClass;
	private ControlClassOld nestedClass;
	private final boolean added;

	public ControlClassTemporaryNestedClassUpdate(@NotNull ControlClassOld ownerControlClass,
												  @NotNull ControlClassOld nestedClass, boolean added) {
		this.ownerControlClass = ownerControlClass;
		this.nestedClass = nestedClass;
		this.added = added;
	}

	@Override
	@NotNull
	public ControlClassOld getOwnerControlClass() {
		return ownerControlClass;
	}

	@NotNull
	public ControlClassOld getNestedClass() {
		return nestedClass;
	}

	/** @return true if the property was added, false if it was removed */
	public boolean isAdded() {
		return added;
	}
}
