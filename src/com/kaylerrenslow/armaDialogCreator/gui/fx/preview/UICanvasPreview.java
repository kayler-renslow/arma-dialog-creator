package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/14/2016.
 */
public class UICanvasPreview extends UICanvas {
	
	public UICanvasPreview(int width, int height) {
		super(width, height, ArmaDialogCreator.getApplicationData().getCurrentProject().getEditingDisplay());
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
