package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Component;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.DefaultComponentContextMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.IComponentContextMenuCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor;
import com.kaylerrenslow.armaDialogCreator.io.ApplicationDataManager;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Used to hold the canvas editor itself and canvas controls (tree view, step, etc). This class is also used to update the editor when need be.
 Created on 05/15/2016. */
class CanvasView extends HBox implements ICanvasView {
	private UICanvasEditor uiCanvasEditor;
	private final CanvasControls canvasControls = new CanvasControls(this);
	private Resolution resolution;

	CanvasView(Resolution resolution) {
		this.resolution = resolution;
		initializeUICanvasEditor(resolution);

		this.getChildren().addAll(uiCanvasEditor, canvasControls);
		HBox.setHgrow(canvasControls, Priority.ALWAYS);

		setOnMouseMoved(new CanvasViewMouseEvent(this));
		focusToCanvas(true);
	}

	private void initializeUICanvasEditor(Resolution r) {
		this.uiCanvasEditor = new UICanvasEditor(r, canvasControls);
		setToDisplay(ApplicationDataManager.applicationData.getEditingDisplay());
		uiCanvasEditor.setComponentMenuCreator(new IComponentContextMenuCreator() {
			@Override
			public @NotNull ContextMenu initialize(Component component) {
				return new DefaultComponentContextMenu(((ArmaControlRenderer) component).getMyControl());
			}
		});
	}

	private void setToDisplay(@NotNull ArmaDisplay display) {
		for (ArmaControl control : display.getBackgroundControls()) {
			uiCanvasEditor.addComponentNoPaint(control.getRenderer());
		}
		for (ArmaControl control : display.getControls()) {
			uiCanvasEditor.addComponentNoPaint(control.getRenderer());
		}
		for (ArmaControl control : display.getObjects()) {
			uiCanvasEditor.addComponentNoPaint(control.getRenderer());
		}
		uiCanvasEditor.paint();
	}

	private void focusToCanvas(boolean focusToCanvas) {
		canvasControls.setFocusTraversable(!focusToCanvas);
		uiCanvasEditor.setFocusTraversable(focusToCanvas);
		if (focusToCanvas) {
			uiCanvasEditor.requestFocus();
		}
	}

	@Override
	public void setCanvasSize(int width, int height) {
		this.uiCanvasEditor.setCanvasSize(width, height);
	}

	@Override
	public void showGrid(boolean showGrid) {
		uiCanvasEditor.showGrid(showGrid);
	}

	@Override
	public void setCanvasBackgroundToImage(@Nullable String imgPath) {
		if (imgPath == null) {
			uiCanvasEditor.setCanvasBackgroundImage(null);
			return;
		}
		uiCanvasEditor.setCanvasBackgroundImage(new ImagePattern(new Image(imgPath)));
	}


	@Override
	public void updateCanvas() {
		uiCanvasEditor.updateColors();
	}

	@Override
	public void updateAbsRegion(int alwaysFront, int showing) {
		uiCanvasEditor.updateAbsRegion(alwaysFront, showing);
	}

	@Override
	public Resolution getCurrentResolution() {
		return resolution;
	}

	@Override
	public UICanvasEditor getUiCanvasEditor() {
		return uiCanvasEditor;
	}

	void keyEvent(String text, boolean keyDown, boolean shiftDown, boolean controlDown, boolean altDown) {
		uiCanvasEditor.keyEvent(text, keyDown, shiftDown, controlDown, altDown);
	}

	void repaintCanvas() {
		uiCanvasEditor.paint();
	}


	private static class CanvasViewMouseEvent implements EventHandler<MouseEvent> {

		private final CanvasView canvasView;

		CanvasViewMouseEvent(CanvasView canvasView) {
			this.canvasView = canvasView;
		}

		@Override
		public void handle(MouseEvent event) {
			canvasView.focusToCanvas(event.getTarget() == canvasView.uiCanvasEditor.getCanvas());
		}
	}
}
