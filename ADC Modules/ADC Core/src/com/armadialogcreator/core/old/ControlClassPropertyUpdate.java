package com.armadialogcreator.core.old;

import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

/**
 Used when {@link ControlClassOld#getPropertyUpdateGroup()} is updated via {@link UpdateListenerGroup#update(Object)}

 @author Kayler
 @since 11/16/16 */
public class ControlClassPropertyUpdate implements ControlClassUpdate {
	private final ControlClassOld controlClass;
	private final ControlPropertyUpdate propertyUpdate;

	/**
	 @param controlClass the {@link ControlClassOld} that was updated
	 @param propertyUpdate the update
	 */
	public ControlClassPropertyUpdate(@NotNull ControlClassOld controlClass, @NotNull ControlPropertyUpdate propertyUpdate) {
		this.controlClass = controlClass;
		this.propertyUpdate = propertyUpdate;
	}

	@Override
	public @NotNull ControlClassOld getOwnerControlClass() {
		return controlClass;
	}

	@NotNull
	public ControlPropertyUpdate getPropertyUpdate() {
		return propertyUpdate;
	}
}
