package com.armadialogcreator.canvas;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 06/18/2016 */
public interface UIScale {
	/** @return a user friendly name to present to user */
	@NotNull String getLabel();

	/** @return the value constant for the scale */
	double getValue();
}
