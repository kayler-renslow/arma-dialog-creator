package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

/**
 Created by Kayler on 05/13/2016.
 */
public interface SnapConfiguration {
	/** The smallest possible snap percentage decimal possible. Must be > 0 */
	double alternateSnapPercentage();

	/** How much snap there is (as a percentage decimal.). Should be > 0 */
	double snapPercentage();

	/** If true, snapping percentage will be based upon viewport size rather than canvas size. If false, snapping percentage will be based on canvas size. This value also dictates how the grid is drawn. */
	boolean snapRelativeToViewport();
}
