package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
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
	private final DataContext dataContext = new DataContext();

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

	public boolean paintPartial() {
		return true;
	}

	@NotNull
	public DataContext getData() {
		return dataContext;
	}
}
