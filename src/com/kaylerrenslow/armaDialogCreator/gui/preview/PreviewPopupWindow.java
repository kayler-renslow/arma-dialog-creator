package com.kaylerrenslow.armaDialogCreator.gui.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.layout.VBox;

/**
 Created by Kayler on 06/14/2016.
 */
public class PreviewPopupWindow extends StagePopup<VBox> {


	private final UICanvasPreview previewCanvas;

	public PreviewPopupWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("PreviewWindow.popup_title"));
		ApplicationData data = ApplicationData.getManagerInstance();
		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(data);
		ArmaDisplay armaDisplay = data.getCurrentProject().getEditingDisplay();

		previewCanvas = new UICanvasPreview(resolution, armaDisplay);
		myRootElement.getChildren().add(previewCanvas);

		previewCanvas.setDisplay(armaDisplay);
		previewCanvas.updateResolution(resolution);
	}

	@Override
	protected void closing() {
		previewCanvas.clearListeners();
	}
}
