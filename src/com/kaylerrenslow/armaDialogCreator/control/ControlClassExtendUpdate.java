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
	private final Iterable<ControlProperty> oldInherits;

	/**
	 @param controlClass {@link ControlClass} that was updated
	 @param oldValue the {@link ControlClass} the old value from {@link ControlClass#getExtendClass()}
	 @param newValue the {@link ControlClass} the newly extended {@link ControlClass}
	 @param oldInherits the properties that were inherited before the extend
	 */
	public ControlClassExtendUpdate(@NotNull ControlClass controlClass, @Nullable ControlClass oldValue,
									@Nullable ControlClass newValue, @NotNull Iterable<ControlProperty> oldInherits) {
		this.controlClass = controlClass;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.oldInherits = oldInherits;
	}

	@Override
	@NotNull
	public ControlClass getOwnerControlClass() {
		return controlClass;
	}

	/** @return {@link ControlClass} the old value from {@link ControlClass#getExtendClass()} */
	@Nullable
	public ControlClass getOldExtendClass() {
		return oldValue;
	}

	/** @return {@link ControlClass} the old value from {@link ControlClass#getExtendClass()} */
	@Nullable
	public ControlClass getNewExtendClass() {
		return newValue;
	}

	/** @return an iterable of the old inherited properties returned by {@link ControlClass#getInheritedProperties()} */
	@NotNull
	public Iterable<ControlProperty> getOldInherits() {
		return oldInherits;
	}
}
