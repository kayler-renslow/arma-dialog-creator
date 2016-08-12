/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 Created by Kayler on 08/04/2016.
 */
public interface Control {
	/** Get the {@link CanvasComponent} instance used for rendering the control is a {@link com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas} */
	CanvasComponent getRenderer();
	
	/** Invoked when the resolution is changed */
	void resolutionUpdate(Resolution newResolution);
	
	Comparator<Control> RENDER_PRIORITY_COMPARATOR = new Comparator<Control>() {
		@Override
		public int compare(Control o1, Control o2) {
			return CanvasComponent.RENDER_PRIORITY_COMPARATOR.compare(o1.getRenderer(), o2.getRenderer());
		}
	};
	
	/** Get the parent of the control. */
	@NotNull
	ControlHolder<? extends Control> getHolder();
	
	/**Get the display that the control is associated with*/
	@NotNull
	Display<? extends Control> getDisplay();
	
	/**
	 Return true if the control is a background control (inside {@link Display#getBackgroundControls()}), false otherwise.<br>
	 Will return true if the control is within a {@link ControlGroup} which is then inside the background controls.
	 */
	default boolean isBackgroundControl() {
		if (getHolder() instanceof Display) {
			return ((Display) getHolder()).getBackgroundControls().contains(this);
		}
		if (getHolder() instanceof ControlGroup) {
			return ((ControlGroup) getHolder()).isBackgroundControl();
		} else {
			throw new IllegalStateException("unknown holder type:" + getHolder().getClass().getName());
		}
	}
}
