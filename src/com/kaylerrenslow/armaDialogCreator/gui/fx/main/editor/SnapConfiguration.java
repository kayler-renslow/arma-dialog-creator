package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

/**
 Created by Kayler on 05/13/2016.
 */
public interface SnapConfiguration {
	/** The smallest possible snap percentage possible. Must be > 0 */
	double alternateSnapPercentage();

	/** How much snap there is (in percent). Should be > 0 */
	double snapPercentage();

	/** Get the snap percentage as a decimal (returning 1.0 == 100%, returning .5 = 50%) */
	default double snapPercentageDecimal() {
		return snapPercentage() / 100.0;
	}
}
