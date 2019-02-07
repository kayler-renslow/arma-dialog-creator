package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlGroup;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.canvas.CanvasContext;
import com.armadialogcreator.expression.Env;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

/**
 Renderer for a ControlGroup control. Use for controls whose classes that extends ArmaControlGroup
 @author Kayler
 @since 07/04/2016. */
public class ControlGroupRenderer extends ArmaControlRenderer {
	
	public ControlGroupRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(control, resolution, env);
	}
	
	@Override
	public void paint(@NotNull GraphicsContext gc, CanvasContext canvasContext) {
		super.paint(gc, canvasContext);
		if (getArea() < 2) {
			return;
		}
		gc.save();
		gc.beginPath();
		gc.rect(getLeftX(), getTopY(), getWidth(), getHeight());
		gc.closePath();
		gc.clip();
		ArmaControlGroup controlGroup = (ArmaControlGroup) getMyControl();
		for (ArmaControl control : controlGroup.getControls()) {
			if (control.getRenderer().isGhost()) {
				continue;
			}
			control.getRenderer().paint(gc, canvasContext);
		}
		gc.restore();
	}
}
