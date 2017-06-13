package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 An update for when a {@link ControlProperty} is inherited/overridden in {@link ControlClass}, but it wasn't in the {@link ControlClass} originally.

 @author Kayler
 @since 06/12/2017 */
public class ControlClassTemporaryPropertyUpdate implements ControlClassUpdate {
	private final ControlClass controlClass;
	private final ControlProperty property;
	private final boolean added;

	public ControlClassTemporaryPropertyUpdate(@NotNull ControlClass controlClass, @NotNull ControlProperty property, boolean added) {
		this.controlClass = controlClass;
		this.property = property;
		this.added = added;
	}

	@Override
	@NotNull
	public ControlClass getControlClass() {
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
