package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 Used when {@link ControlClass#getPropertyUpdateGroup()} is updated via {@link com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup#update(Object)}

 @author Kayler
 @since 11/16/16 */
public class ControlClassPropertyUpdate implements ControlClassUpdate {
	private final ControlClass controlClass;
	private final ControlPropertyUpdate propertyUpdate;

	/**
	 @param controlClass the {@link ControlClass} that was updated
	 @param propertyUpdate the update
	 */
	public ControlClassPropertyUpdate(@NotNull ControlClass controlClass, @NotNull ControlPropertyUpdate propertyUpdate) {
		this.controlClass = controlClass;
		this.propertyUpdate = propertyUpdate;
	}

	@Override
	public @NotNull ControlClass getOwnerControlClass() {
		return controlClass;
	}

	@NotNull
	public ControlPropertyUpdate getPropertyUpdate() {
		return propertyUpdate;
	}
}
