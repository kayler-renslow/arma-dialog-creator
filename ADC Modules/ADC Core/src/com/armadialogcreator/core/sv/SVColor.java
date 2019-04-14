package com.armadialogcreator.core.sv;

import com.armadialogcreator.util.AColor;
import org.jetbrains.annotations.NotNull;

/**
 Defines a color.

 @author Kayler
 @since 05/22/2016. */
public interface SVColor extends AColor {

	@NotNull
	default SVColorArray toColorArray() {
		return new SVColorArray(getRedF(), getGreenF(), getBlueF(), getAlphaF());
	}
}
