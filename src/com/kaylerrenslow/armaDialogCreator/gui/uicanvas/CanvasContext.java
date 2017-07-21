package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 @author Kayler
 @since 07/04/2017 */
public class CanvasContext {
	private final List<Function<GraphicsContext, Void>> paintLast = new ArrayList<>();

	/**
	 Use this function to paint something after the initial {@link UICanvas#paint()} is invoked.

	 @param paint the function to use to paint. The returned value of the function is ignored
	 */
	public void paintLast(@NotNull Function<GraphicsContext, Void> paint) {
		paintLast.add(paint);
	}

	@NotNull
	protected List<Function<GraphicsContext, Void>> getPaintLast() {
		return paintLast;
	}

	/**
	 @return true if {@link CanvasControl} shouldn't paint all details.
	 Return false if should paint all details.
	 */
	public boolean paintPartial() {
		return true;
	}
}
