package com.armadialogcreator.control;

import com.armadialogcreator.canvas.UIScale;
import org.jetbrains.annotations.NotNull;

/**
 Constant values for Arma 3's UI scale
 @author Kayler
 @since 05/18/2016. */
public enum ArmaUIScale implements UIScale {
	VERY_LARGE("Very Large", 1), LARGE("Large", 0.85), MEDIUM("Medium", 0.7), SMALL("Small", 0.55), VERY_SMALL("Very Small", 0.47);
	
	public final String label;
	public final double value;
	
	ArmaUIScale(String label, double value) {
		this.label = label;
		this.value = value;
	}


	@NotNull
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
	
	/** Constant field for default UIScale */
	public static final ArmaUIScale DEFAULT = SMALL;
}
