package com.armadialogcreator.core.old;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used when {@link ControlClassOld#extendControlClass(ControlClassOld)} is invoked

 @author Kayler
 @since 11/16/16 */
public class ControlClassExtendUpdate implements ControlClassUpdate {
	private final ControlClassOld controlClass;
	private final ControlClassOld oldValue;
	private final ControlClassOld newValue;
	private final Iterable<ControlProperty> oldInherits;

	/**
	 @param controlClass {@link ControlClassOld} that was updated
	 @param oldValue the {@link ControlClassOld} the old value from {@link ControlClassOld#getExtendClass()}
	 @param newValue the {@link ControlClassOld} the newly extended {@link ControlClassOld}
	 @param oldInherits the properties that were inherited before the extend
	 */
	public ControlClassExtendUpdate(@NotNull ControlClassOld controlClass, @Nullable ControlClassOld oldValue,
									@Nullable ControlClassOld newValue, @NotNull Iterable<ControlProperty> oldInherits) {
		this.controlClass = controlClass;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.oldInherits = oldInherits;
	}

	@Override
	@NotNull
	public ControlClassOld getOwnerControlClass() {
		return controlClass;
	}

	/** @return {@link ControlClassOld} the old value from {@link ControlClassOld#getExtendClass()} */
	@Nullable
	public ControlClassOld getOldExtendClass() {
		return oldValue;
	}

	/** @return {@link ControlClassOld} the old value from {@link ControlClassOld#getExtendClass()} */
	@Nullable
	public ControlClassOld getNewExtendClass() {
		return newValue;
	}

	/** @return an iterable of the old inherited properties returned by {@link ControlClassOld#getInheritedProperties()} */
	@NotNull
	public Iterable<ControlProperty> getOldInherits() {
		return oldInherits;
	}
}
