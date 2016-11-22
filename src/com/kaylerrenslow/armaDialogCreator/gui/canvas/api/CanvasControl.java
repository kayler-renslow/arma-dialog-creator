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

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 Created by Kayler on 08/04/2016.
 */
public interface CanvasControl<C extends CanvasControl> {
	/** Get the {@link CanvasComponent} instance used for rendering the control is a {@link com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas} */
	CanvasComponent getRenderer();

	/** Invoked when the resolution is changed */
	void resolutionUpdate(Resolution newResolution);

	Comparator<CanvasControl> RENDER_PRIORITY_COMPARATOR = new Comparator<CanvasControl>() {
		@Override
		public int compare(CanvasControl o1, CanvasControl o2) {
			return CanvasComponent.RENDER_PRIORITY_COMPARATOR.compare(o1.getRenderer(), o2.getRenderer());
		}
	};

	/** Get the parent of the control. */
	default ControlHolder<C> getHolder() {
		return getHolderObserver().getValue();
	}

	/** Get the display that the control is associated with */
	default CanvasDisplay<C> getDisplay() {
		return getDisplayObserver().getValue();
	}


	@NotNull
	ValueObserver<CanvasDisplay<C>> getDisplayObserver();

	@NotNull
	ValueObserver<ControlHolder<C>> getHolderObserver();

	/** Get the update group that will update anytime the control needs to be re-rendered */
	UpdateListenerGroup<Object> getRenderUpdateGroup();

	/**
	 Return true if the control is a background control (inside {@link CanvasDisplay#getBackgroundControls()}), false otherwise.<br>
	 Will return true if the control is within a {@link CanvasControlGroup} which is then inside the background controls.
	 */
	default boolean isBackgroundControl() {
		if (getHolder() instanceof CanvasDisplay) {
			return ((CanvasDisplay) getHolder()).getBackgroundControls().contains(this);
		}
		if (getHolder() instanceof CanvasControlGroup) {
			return ((CanvasControlGroup) getHolder()).isBackgroundControl();
		} else {
			throw new IllegalStateException("unknown holder type:" + getHolder().getClass().getName());
		}
	}

	DataContext getUserData();
}
