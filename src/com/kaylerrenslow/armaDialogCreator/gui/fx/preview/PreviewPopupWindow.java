/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Display;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

/**
 Created by Kayler on 06/14/2016.
 */
public class PreviewPopupWindow extends StagePopup<VBox> {
	private static final PreviewPopupWindow INSTANCE = new PreviewPopupWindow();
	
	public static PreviewPopupWindow getInstance() {
		return INSTANCE;
	}
	
	
	private Display armaDisplay;
	private UICanvasPreview previewCanvas;
	
	/** Create */
	private PreviewPopupWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.PreviewWindow.POPUP_TITLE);
		myRootElement.getChildren().add(previewCanvas);
	}
	
	/** Shows the popup with the given arma display */
	public void showDisplay(Display display) {
		this.armaDisplay = display;
		show();
	}
	
	@Override
	public void show() {
		if (armaDisplay == null) {
			throw new IllegalStateException("no display set");
		}
		if (previewCanvas == null) {
			previewCanvas = new UICanvasPreview(DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()), armaDisplay);
		}
		previewCanvas.setDisplay(armaDisplay);
		repaintCanvas();
		super.show();
	}
	
	private void repaintCanvas() {
		previewCanvas.paint();
	}
	
	@Override
	public void close() {
		hide();
	}
	
	@Override
	protected void onCloseRequest(WindowEvent event) {
		event.consume();
		hide();
	}
}
