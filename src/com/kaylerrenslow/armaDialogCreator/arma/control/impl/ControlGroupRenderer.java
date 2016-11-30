package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
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
	public void paint(@NotNull GraphicsContext gc, @NotNull DataContext dataContext) {
		super.paint(gc, dataContext);
		if (getArea() < 2) {
			return;
		}
		gc.save();
		gc.beginPath();
		gc.moveTo(getX1(), getY1());
		gc.lineTo(getX2(), getY1()); //top left to top right
		gc.lineTo(getX2(), getY2()); //top right to bottom right
		gc.lineTo(getX1(), getY2()); //bottom right to bottom left
		gc.lineTo(getX1(), getY1()); //bottom left to top left
		gc.closePath();
		gc.clip();
		ArmaControlGroup controlGroup = (ArmaControlGroup) getMyControl();
		for (ArmaControl control : controlGroup.getControls()) {
			if (control.getRenderer().isGhost()) {
				continue;
			}
			control.getRenderer().paint(gc, dataContext);
		}
		gc.restore();
	}
}
