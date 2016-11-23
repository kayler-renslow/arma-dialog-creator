/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */


package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ViewportCanvasComponent;

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
}
