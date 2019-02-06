package com.armadialogcreator.core.old;

import org.jetbrains.annotations.NotNull;

/**
 Used with {@link ControlClassOld#getControlClassUpdateGroup()}
 @author Kayler
 @since 11/16/16
 */
public interface ControlClassUpdate {

	/** Get the {@link ControlClassOld} that was updated */
	@NotNull
	ControlClassOld getOwnerControlClass();
}
