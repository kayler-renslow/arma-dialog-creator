package com.kaylerrenslow.armaDialogCreator.gui.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Region;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.SimpleCanvasComponent;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

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
	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		gc.save();
		gc.setStroke(backgroundColor);
		Region.strokeRectangle(gc, resolution.getViewportX(), resolution.getViewportY(), resolution.getViewportX() + resolution.getViewportWidth(), resolution.getViewportY() + resolution.getViewportHeight());
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
