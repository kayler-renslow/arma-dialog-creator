package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import javafx.scene.canvas.GraphicsContext;

/**
 @author Kayler
 Renderer for a ControlGroup control. Use for controls whose classes that extends ArmaControlGroup
 Created on 07/04/2016. */
public class ControlGroupRenderer extends ArmaControlRenderer {

	@Override
	public void paint(GraphicsContext gc) {
		super.paint(gc);
		if(getArea() < 2){
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
			control.getRenderer().forcePaint(gc);
		}
		gc.restore();
	}
}
