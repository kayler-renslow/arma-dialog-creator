package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/04/2016.
 */
public interface CanvasControlGroup<C extends CanvasControl> extends CanvasControl<C>, ControlHolder<C> {

	default void setDisplayForGroup(@NotNull CanvasDisplay<C> display) {
		getDisplayObserver().updateValue(display);
		for (C c : getControls()) {
			if (c instanceof CanvasControlGroup) {
				((CanvasControlGroup) c).setDisplayForGroup(display);
			}
			c.getDisplayObserver().updateValue((CanvasDisplay) display);
		}
	}
}
