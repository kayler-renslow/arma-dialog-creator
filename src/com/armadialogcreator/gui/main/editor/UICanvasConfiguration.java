
package com.armadialogcreator.gui.main.editor;

import com.armadialogcreator.gui.uicanvas.ViewportCanvasComponent;

/**
 @author Kayler
 @see UICanvasEditor#setConfig(UICanvasConfiguration)
 @since 05/13/2016 */
public interface UICanvasConfiguration {
	/** The smallest possible snap percentage decimal possible. Must be > 0 */
	double alternateSnapPercentage();

	/** How much snap there is (as a percentage decimal.). Should be > 0 */
	double snapPercentage();

	/**
	 Return true if for each {@link ViewportCanvasComponent} that {@link #snapPercentage()} is percentage for the viewport size rather than canvas size and thus will snap according to the
	 viewport dimensions. If false, will snap relative to canvas size.
	 */
	boolean viewportSnapEnabled();

	/** If true, scaling and translating controls will only work when the actions don't put their bounds outside the canvas. If false, all scaling and translating is allowed. */
	boolean isSafeMovement();

	/**
	 Sets whether or not the grid should be shown.

	 @return true if grid is being shown, false otherwise
	 */
	boolean showGrid();


	/**
	 Set or disable viewport snapping

	 @param set true to enable, false to disable
	 @see #viewportSnapEnabled()
	 */
	void setViewportSnapEnabled(boolean set);

	/**
	 set safe movement

	 @param safeMovement true to set, false to disable
	 @see #isSafeMovement()
	 */
	void setSafeMovement(boolean safeMovement);

	/**
	 Sets whether or not the grid should be shown.

	 @param set true if set grid, false otherwise
	 */
	void setShowGrid(boolean set);
}
