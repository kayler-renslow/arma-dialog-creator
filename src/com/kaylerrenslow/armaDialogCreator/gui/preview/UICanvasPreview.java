package com.kaylerrenslow.armaDialogCreator.gui.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.UICanvas;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/14/2016.
 */
public class UICanvasPreview extends UICanvas {
	
	public UICanvasPreview(Resolution resolution, ArmaDisplay display) {
		super(resolution, display);
		dataContext.put(ArmaControlRenderer.KEY_PAINT_PREVIEW, true);
	}

	@Override
	protected void paint() {
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
