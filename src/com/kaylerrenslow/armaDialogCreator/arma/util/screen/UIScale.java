package com.kaylerrenslow.armaDialogCreator.arma.util.screen;

/**
 Created by Kayler on 05/18/2016.
 */
public enum UIScale {
	VERY_LARGE("Very Large", 1), LARGE("Large",0.85), MEDIUM("Medium",0.7), SMALL("Small",0.55),VERY_SMALL("Very Small",0.47);

	public final String label;
	public final double value;

	UIScale(String label, double value) {
		this.label = label;
		this.value = value;
	}
}
