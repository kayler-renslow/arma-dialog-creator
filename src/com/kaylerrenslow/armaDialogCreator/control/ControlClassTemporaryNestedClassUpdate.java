package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 An update for when a {@link ControlClass} is inherited in {@link ControlClass}, but it wasn't in the
 {@link ControlClass} originally.

 @author Kayler
 @see ControlClass#extendControlClass(ControlClass)
 @since 06/12/2017 */
public class ControlClassTemporaryNestedClassUpdate implements ControlClassUpdate {
	private final ControlClass ownerControlClass;
	private ControlClass nestedClass;
	private final boolean added;

	public ControlClassTemporaryNestedClassUpdate(@NotNull ControlClass ownerControlClass,
												  @NotNull ControlClass nestedClass, boolean added) {
		this.ownerControlClass = ownerControlClass;
		this.nestedClass = nestedClass;
		this.added = added;
	}

	@Override
	@NotNull
	public ControlClass getOwnerControlClass() {
		return ownerControlClass;
	}

	@NotNull
	public ControlClass getNestedClass() {
		return nestedClass;
	}

	/** @return true if the property was added, false if it was removed */
	public boolean isAdded() {
		return added;
	}
}
