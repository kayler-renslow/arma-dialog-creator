package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Border;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 @author Kayler
 A component that is drawable and interactable in the UICanvas.
 Created on 06/18/2016. */
public interface CanvasComponent extends Region{

	void paint(GraphicsContext gc);

	void setBackgroundColor(@NotNull Color paint);

	Color getBackgroundColor();

	Border getBorder();

	void setBorder(@Nullable Border border);

	/** Return true if the component is enabled (user can click on it or move it with mouse), false otherwise. */
	boolean isEnabled();

	/** Set whether the component is enabled or not.
	 @see #setGhost(boolean) */
	void setEnabled(boolean enabled);

	/**
	 Returns true if the component is invisible and is disabled, false otherwise
	 */
	boolean isGhost();

	/**
	 Sets the visibility and enable values. A ghost is not visible and is not enabled.
	 */
	void setGhost(boolean ghost);
	
	Comparator<CanvasComponent> RENDER_PRIORITY_COMPARATOR = new Comparator<CanvasComponent>() {
		@Override
		public int compare(CanvasComponent o1, CanvasComponent o2) {
			if (o1.getRenderPriority() < o2.getRenderPriority()) {
				return -1;
			}
			if (o1.getRenderPriority() > o2.getRenderPriority()) {
				return 1;
			}
			return 0;
		}
	};
}
