package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used for locating {@link ControlClass} instances.

 @author Kayler
 @since 11/19/2016 */
public interface ControlClassRegistry {

	/**
	 Will get the {@link ControlClass} by the given name

	 @return the matched {@link ControlClass}, or null if nothing could be matched
	 */
	@Nullable
	ControlClass findControlClassByName(@NotNull String className);

}
