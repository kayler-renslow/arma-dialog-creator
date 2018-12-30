package com.armadialogcreator.gui.uicanvas;

import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.UpdateListenerGroup;
import com.armadialogcreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 Created by Kayler on 08/04/2016.
 */
public interface CanvasControl<C extends CanvasControl> {
	/** Get the {@link CanvasComponent} instance used for rendering the control is a {@link UICanvas} */
	@NotNull CanvasComponent getRenderer();

	/** Invoked when the resolution is changed */
	void resolutionUpdate(@NotNull Resolution newResolution);

	Comparator<CanvasControl> RENDER_PRIORITY_COMPARATOR = new Comparator<CanvasControl>() {
		@Override
		public int compare(CanvasControl o1, CanvasControl o2) {
			return CanvasComponent.RENDER_PRIORITY_COMPARATOR.compare(o1.getRenderer(), o2.getRenderer());
		}
	};

	/**
	 Will equal {@link #getDisplay()} if the control is not inside a {@link CanvasControlGroup}.
	 If not equal to {@link #getDisplay()}, then the control is inside a {@link CanvasControlGroup}.

	 @return the parent of the control.
	 */
	default ControlHolder<C> getHolder() {
		return getHolderObserver().getValue();
	}

	/** @return the display that the control is associated with */
	default CanvasDisplay<C> getDisplay() {
		return getDisplayObserver().getValue();
	}

	@NotNull ValueObserver<CanvasDisplay<C>> getDisplayObserver();

	@NotNull ValueObserver<ControlHolder<C>> getHolderObserver();

	/**
	 Used for letting a {@link UICanvas} know that a render needs to happen.
	 This method should be invoked on the JavaFX thread.

	 @return the update group that will update anytime the control needs to be re-rendered
	 @throws NullPointerException when {@link #getDisplay()} is null
	 */
	@NotNull
	default UpdateListenerGroup<C> getRenderUpdateGroup() {
		return getDisplay().getReRenderUpdateGroup();
	}

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

	@NotNull DataContext getUserData();
}
