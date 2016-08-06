package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Display;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/14/2016.
 */
public class UICanvasPreview extends UICanvas {
	
	public UICanvasPreview(Resolution resolution, Display display) {
		super(resolution, display);
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
