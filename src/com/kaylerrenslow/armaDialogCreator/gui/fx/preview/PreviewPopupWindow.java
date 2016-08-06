package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Display;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
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
