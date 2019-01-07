package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used for locating {@link ControlClassOld} instances.

 @author Kayler
 @since 11/19/2016 */
public interface ControlClassRegistry extends Registry {

	/**
	 Will get the {@link ControlClassOld} by the given name

	 @return the matched {@link ControlClassOld}, or null if nothing could be matched
	 */
	@Nullable
	ControlClassOld findControlClassByName(@NotNull String className);

}
