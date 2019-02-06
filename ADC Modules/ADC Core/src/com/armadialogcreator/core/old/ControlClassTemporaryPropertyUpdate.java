package com.armadialogcreator.core.old;

import org.jetbrains.annotations.NotNull;

/**
 An update for when a {@link ControlProperty} is inherited/overridden in {@link ControlClassOld}, but it wasn't in the {@link ControlClassOld} originally.

 @author Kayler
 @since 06/12/2017 */
public class ControlClassTemporaryPropertyUpdate implements ControlClassUpdate {
	private final ControlClassOld controlClass;
	private final ControlProperty property;
	private final boolean added;

	public ControlClassTemporaryPropertyUpdate(@NotNull ControlClassOld controlClass, @NotNull ControlProperty property, boolean added) {
		this.controlClass = controlClass;
		this.property = property;
		this.added = added;
	}

	@Override
	@NotNull
	public ControlClassOld getOwnerControlClass() {
		return controlClass;
	}

	@NotNull
	public ControlProperty getProperty() {
		return property;
	}

	/** @return true if the property was added, false if it was removed */
	public boolean isAdded() {
		return added;
	}
}
