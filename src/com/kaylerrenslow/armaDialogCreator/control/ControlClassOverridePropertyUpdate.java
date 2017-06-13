package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 Update triggered by {@link ControlClass#overrideProperty(ControlPropertyLookupConstant)}

 @author Kayler
 @since 11/16/2016 */
public class ControlClassOverridePropertyUpdate implements ControlClassUpdate {
	private final ControlClass controlClass;
	private final ControlProperty overidden;
	private final boolean wasAdded;

	/**
	 @param controlClass {@link ControlClass} that was updated
	 @param overridden the {@link ControlProperty} that was overridden
	 @param wasAdded true if {@link #getOveridden()} was added to {@link ControlClass#getOverriddenProperties()}, false if it was removed
	 */
	public ControlClassOverridePropertyUpdate(@NotNull ControlClass controlClass, @NotNull ControlProperty overridden, boolean wasAdded) {
		this.controlClass = controlClass;
		this.overidden = overridden;
		this.wasAdded = wasAdded;
	}

	@NotNull
	public ControlProperty getOveridden() {
		return overidden;
	}

	/** @return true if {@link #getOveridden()} was added to {@link ControlClass#getOverriddenProperties()}, false if it was removed */
	public boolean wasOverridden() {
		return wasAdded;
	}

	@Override
	public @NotNull ControlClass getControlClass() {
		return controlClass;
	}
}
