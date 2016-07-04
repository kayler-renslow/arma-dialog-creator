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
		ArmaControlGroup controlGroup = (ArmaControlGroup) getMyControl();
		for (ArmaControl control : controlGroup.getControls()) {
			control.getRenderer().paint(gc);
		}
	}
}
