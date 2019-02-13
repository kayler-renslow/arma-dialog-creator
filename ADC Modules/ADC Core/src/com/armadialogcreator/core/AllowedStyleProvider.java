package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyArray;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 06/30/2017 */
public interface AllowedStyleProvider {
	@NotNull
	default ReadOnlyArray<ControlStyle> getAllowedStyles() {
		return ReadOnlyArray.EMPTY;
	}
}
