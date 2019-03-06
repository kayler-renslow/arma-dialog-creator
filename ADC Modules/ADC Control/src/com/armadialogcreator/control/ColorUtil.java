package com.armadialogcreator.control;

import com.armadialogcreator.core.sv.SVColorArray;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 3/5/19 */
public class ColorUtil {

	@NotNull
	public static SVColorArray toColorArray(@NotNull Color c) {
		return new SVColorArray(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity());
	}

}
