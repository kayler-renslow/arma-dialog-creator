package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.util.List;

/**
 Created by Kayler on 06/14/2016.
 */
public class PreviewPopupWindow extends StagePopup<VBox> {
	private static final PreviewPopupWindow INSTANCE = new PreviewPopupWindow();
	private final UpdateListener<ArmaDisplay.DisplayUpdate> displayUpdateListener = new UpdateListener<ArmaDisplay.DisplayUpdate>() {
		@Override
		public void update(ArmaDisplay.DisplayUpdate data) {
			previewCanvas.removeAllComponents();
			addAllControls(armaDisplay.getControls(), previewCanvas);
			repaintCanvas();
		}
	};

	public static PreviewPopupWindow getInstance() {
		return INSTANCE;
	}


	private ArmaDisplay armaDisplay;
	private UICanvasPreview previewCanvas = new UICanvasPreview(ScreenDimension.D1600.width, ScreenDimension.D1600.height);

	/** Create */
	private PreviewPopupWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.PreviewWindow.POPUP_TITLE);
		myRootElement.getChildren().add(previewCanvas);
	}

	/** Shows the popup with the given arma display */
	public void showDisplay(ArmaDisplay display) {
		if (this.armaDisplay != null) {
			this.armaDisplay.getUpdateListenerGroup().removeUpdateListener(displayUpdateListener);
		}
		this.armaDisplay = display;
		show();
	}

	@Override
	public void show() {
		if (armaDisplay == null) {
			throw new IllegalStateException("no display set");
		}
		armaDisplay.getUpdateListenerGroup().addListener(displayUpdateListener);
		previewCanvas.removeAllComponents();
		addAllControls(armaDisplay.getControls(), previewCanvas);
		repaintCanvas();
		super.show();
	}

	private static void addAllControls(List<ArmaControl> controls, UICanvas canvas) {
		for (ArmaControl control : controls) {
			canvas.addComponentNoPaint(control.getRenderer());
			if (control instanceof ArmaControlGroup) {
				addAllControls(((ArmaControlGroup) control).getControls(), canvas);
			}
		}
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
