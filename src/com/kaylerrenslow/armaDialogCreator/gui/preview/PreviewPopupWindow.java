package com.kaylerrenslow.armaDialogCreator.gui.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
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
	
	
	private ArmaDisplay armaDisplay;
	private UICanvasPreview previewCanvas;
	
	/** Create */
	private PreviewPopupWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("PreviewWindow.popup_title"));
	}
	
	/** Shows the popup with the given arma display */
	public void showDisplay(ArmaDisplay display) {
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
			myRootElement.getChildren().add(previewCanvas);
		}
		previewCanvas.setDisplay(armaDisplay);
		super.show();
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
