package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 Created by Kayler on 08/04/2016.
 */
public interface CanvasDisplay<C extends CanvasControl> extends ControlHolder<C> {

	/** Get controls that are rendered first and have no user interaction */
	@NotNull DisplayControlList<C> getBackgroundControls();

	@NotNull DisplayControlList<C> getControls();

	/**
	 Get an iterator that will cycle through the background controls ({@link #getBackgroundControls()})
	 and then main controls ({@link #getControls()}). This will not iterate through controls
	 within possible control groups in either controls list or background controls list.

	 @param backwards if true, the iterator will iterate through the controls from size-1 to 0 and then the background
	 controls (starting indexes of each list is from size-1 to 0).<br>
	 if false, will iterate through the background controls from 0 to size-1 and then other controls from 0 to size-1
	 */
	@NotNull Iterator<C> iteratorForAllControls(boolean backwards);

	void resolutionUpdate(@NotNull Resolution newResolution);

	/**
	 Get an update group that will notify a change when a render is required on a {@link UICanvas}
	 <p>
	 This method should be invoked on the JavaFX thread.
	 */
	@NotNull UpdateListenerGroup<C> getReRenderUpdateGroup();

}

