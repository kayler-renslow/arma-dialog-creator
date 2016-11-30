package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used when {@link ControlClass#extendControlClass(ControlClass)} is invoked

 @author Kayler
 @since 11/16/16 */
public class ControlClassExtendUpdate implements ControlClassUpdate {
	private final ControlClass controlClass;
	private final ControlClass oldValue;
	private final ControlClass newValue;

	/**
	 @param controlClass {@link ControlClass} that was updated
	 @param oldValue the {@link ControlClass} the old value from {@link ControlClass#getExtendClass()}
	 @param newValue the {@link ControlClass} the newly extended {@link ControlClass}
	 */
	public ControlClassExtendUpdate(@NotNull ControlClass controlClass, @Nullable ControlClass oldValue, @Nullable ControlClass newValue) {
		this.controlClass = controlClass;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public @NotNull ControlClass getControlClass() {
		return controlClass;
	}

	/** The {@link ControlClass} the old value from {@link ControlClass#getExtendClass()} */
	@Nullable
	public ControlClass getOldValue() {
		return oldValue;
	}

	/** The {@link ControlClass} the old value from {@link ControlClass#getExtendClass()} */
	@Nullable
	public ControlClass getNewValue() {
		return newValue;
	}
}
