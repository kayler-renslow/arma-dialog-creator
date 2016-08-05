package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Region;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.SimpleCanvasComponent;
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
