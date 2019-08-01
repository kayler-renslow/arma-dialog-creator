package com.armadialogcreator.canvas;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/18/2016.
 */
class ArmaAbsoluteBoxComponent {
	private Resolution resolution;
	private boolean alwaysFront = true;
	private Color backgroundColor;

	ArmaAbsoluteBoxComponent(@NotNull Color color, @NotNull Resolution r) {
		this.resolution = r;
		setBackgroundColor(color);
	}

	private void setBackgroundColor(@NotNull Color color) {
		this.backgroundColor = color;
	}


	public void setAlwaysRenderAtFront(boolean alwaysFront) {
		this.alwaysFront = alwaysFront;
	}

	public void paint(@NotNull Graphics graphics) {
		graphics.save();
		graphics.setStroke(backgroundColor);
		graphics.strokeRectangle(
				resolution.getViewportX(),
				resolution.getViewportY(),
				resolution.getViewportWidth(),
				resolution.getViewportHeight()
		);
		graphics.restore();
	}

	public boolean alwaysRenderAtFront() {
		return alwaysFront;
	}
}
