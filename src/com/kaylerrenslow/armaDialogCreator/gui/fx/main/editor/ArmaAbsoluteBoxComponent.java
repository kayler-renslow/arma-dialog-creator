package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.SimpleCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import javafx.scene.canvas.GraphicsContext;

/**
 Created by Kayler on 05/18/2016.
 */
class ArmaAbsoluteBoxComponent extends SimpleCanvasComponent {
	private boolean alwaysFront = true;

	ArmaAbsoluteBoxComponent(ArmaResolution r) {
		super(r.getViewportX(), r.getViewportY(), r.getViewportWidth(), r.getViewportHeight());
		super.setBackgroundColor(CanvasViewColors.ABS_REGION);
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	void updateToNewResolution(ArmaResolution newResolution) {
		this.setPositionWH(newResolution.getViewportX(), newResolution.getViewportY(), newResolution.getViewportWidth(), newResolution.getViewportHeight());
	}

	void setAlwaysRenderAtFront(boolean alwaysFront) {
		this.alwaysFront = alwaysFront;
	}

	@Override
	public void paint(GraphicsContext gc) {
		gc.save();
		gc.setStroke(backgroundColor);
		drawRectangle(gc);
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
