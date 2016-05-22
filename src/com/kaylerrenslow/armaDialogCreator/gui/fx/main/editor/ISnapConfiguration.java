package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

/**
 Created by Kayler on 05/13/2016.
 */
public interface ISnapConfiguration {
	/** The smallest possible snap percentage possible. Must be > 0 */
	double alternateSnapPercentage();

	/** How much snap there is (in percent). Should be > 0 */
	double snapPercentage();

}
