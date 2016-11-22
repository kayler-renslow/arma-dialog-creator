/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 @author Kayler
 A component that is drawable and interactable in the UICanvas.
 Created on 06/18/2016. */
public interface CanvasComponent extends Region {

	void paint(@NotNull GraphicsContext gc, @NotNull DataContext dataContext);
	
	void setBackgroundColor(@NotNull Color paint);
	
	Color getBackgroundColor();
	
	Border getBorder();
	
	void setBorder(@Nullable Border border);
	
	/** Return true if the component is enabled (user can click on it or move it with mouse), false otherwise. */
	boolean isEnabled();
	
	/**
	 Set whether the component is enabled or not.
	 
	 @see #setGhost(boolean)
	 */
	void setEnabled(boolean enabled);
	
	/**
	 Returns true if the component is invisible and is disabled, false otherwise
	 */
	boolean isGhost();
	
	/**
	 Sets the visibility and enable values. A ghost is not visible and is not enabled.
	 */
	void setGhost(boolean ghost);
	
	/** Sorts such that smallest render priority is rendered first and highest render priority is rendered last */
	Comparator<CanvasComponent> RENDER_PRIORITY_COMPARATOR = new Comparator<CanvasComponent>() {
		@Override
		public int compare(CanvasComponent o1, CanvasComponent o2) {
			if (o1.getRenderPriority() < o2.getRenderPriority()) {
				return -1;
			}
			if (o1.getRenderPriority() > o2.getRenderPriority()) {
				return 1;
			}
			return 0;
		}
	};
}
