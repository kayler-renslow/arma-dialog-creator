package com.armadialogcreator.core.old;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 06/30/2017 */
public interface AllowedStyleProvider {
	@NotNull
	default ControlStyle[] getAllowedStyles() {
		return new ControlStyle[0];
	}
}
