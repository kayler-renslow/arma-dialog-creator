package com.armadialogcreator.control;

import com.armadialogcreator.data.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Used for locating {@link Macro} instances.

 @author Kayler
 @since 11/19/2016 */
public interface MacroRegistry extends Registry {

	/** Get a {@link Macro} instance from the given name. Will return null if key couldn't be matched */
	@Nullable
	Macro findMacroByKey(@NotNull String macroKey);

}
