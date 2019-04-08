package com.armadialogcreator.gui.main;

import com.armadialogcreator.core.ControlType;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 @author K
 @since 4/7/19 */
public class ArmaControlIconPool {
	private static Map<ControlType, Image> pool = new HashMap<>();

	@NotNull
	public static Image getIcon(@NotNull ControlType type) {
		return pool.computeIfAbsent(type, type1 -> {
			return new Image(type1.getIconPath());
		});
	}
}
