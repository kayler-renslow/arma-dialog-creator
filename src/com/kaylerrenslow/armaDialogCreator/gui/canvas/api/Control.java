package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import java.util.Comparator;

/**
 Created by Kayler on 08/04/2016.
 */
public interface Control {
	CanvasComponent getRenderer();
	
	Comparator<Control> RENDER_PRIORITY_COMPARATOR = new Comparator<Control>() {
		@Override
		public int compare(Control o1, Control o2) {
			return CanvasComponent.RENDER_PRIORITY_COMPARATOR.compare(o1.getRenderer(), o2.getRenderer());
		}
	};
}
