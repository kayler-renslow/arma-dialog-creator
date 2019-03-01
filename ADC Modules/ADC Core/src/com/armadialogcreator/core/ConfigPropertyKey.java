package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/03/2019 */
public interface ConfigPropertyKey {
	/** @return the name associated with the property. Is not guaranteed to be unique. */
	@NotNull
	String getPropertyName();
}
