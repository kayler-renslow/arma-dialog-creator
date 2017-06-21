package com.kaylerrenslow.armaDialogCreator.gui.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 A {@link UICanvas} instance that will paint {@link ArmaControl} instances in "preview mode"

 @author Kayler
 @since 06/14/2016 */
public class UICanvasPreview extends UICanvas {

	private final CanvasView canvasView = ArmaDialogCreator.getCanvasView();

	public UICanvasPreview(@NotNull Resolution resolution, @NotNull ArmaDisplay display) {
		super(resolution, display);
		dataContext.put(ArmaControlRenderer.KEY_PAINT_PREVIEW, true);
	}

	@Override
	protected void paint() {
		this.backgroundColor = canvasView.getCanvasBackgroundColor();
		this.backgroundImage = canvasView.getCanvasBackgroundImage();

		super.paint();
		requestPaint();
	}

	@Override
	protected void mousePressed(int mousex, int mousey, @NotNull MouseButton mb) {

	}

	@Override
	protected void mouseReleased(int mousex, int mousey, @NotNull MouseButton mb) {

	}

	@Override
	protected void mouseMoved(int mousex, int mousey) {

	}
}
