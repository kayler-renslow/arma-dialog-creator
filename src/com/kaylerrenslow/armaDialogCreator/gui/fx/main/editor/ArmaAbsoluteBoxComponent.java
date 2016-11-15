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

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Region;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.SimpleCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import javafx.scene.canvas.GraphicsContext;

/**
 Created by Kayler on 05/18/2016.
 */
class ArmaAbsoluteBoxComponent extends SimpleCanvasComponent {
	private Resolution resolution;
	private boolean alwaysFront = true;
	
	ArmaAbsoluteBoxComponent(Resolution r) {
		super(r.getViewportX(), r.getViewportY(), r.getViewportWidth(), r.getViewportHeight());
		this.resolution = r;
		super.setBackgroundColor(CanvasViewColors.ABS_REGION);
	}
	
	@Override
	public boolean isEnabled() {
		return false;
	}
		
	void setAlwaysRenderAtFront(boolean alwaysFront) {
		this.alwaysFront = alwaysFront;
	}
	
	@Override
	public void paint(GraphicsContext gc) {
		gc.save();
		gc.setStroke(backgroundColor);
		Region.drawRectangle(gc, resolution.getViewportX(), resolution.getViewportY(), resolution.getViewportX() + resolution.getViewportWidth(), resolution.getViewportY() + resolution.getViewportHeight());
		gc.restore();
	}
	
	@Override
	public int getRenderPriority() {
		if (alwaysFront) {
			return Integer.MAX_VALUE;
		}
		return Integer.MIN_VALUE;
	}
	
	public boolean alwaysRenderAtFront() {
		return alwaysFront;
	}
}
