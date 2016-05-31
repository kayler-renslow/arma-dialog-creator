package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Component;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.DefaultComponentContextMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.IComponentContextMenuCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/15/2016.
 */
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
		uiCanvasEditor.addComponent(new StaticControl("Sample", 0, ControlStyle.SINGLE, 0, 0, 1, 1, resolution).getRenderer());
		uiCanvasEditor.setComponentMenuCreator(new IComponentContextMenuCreator() {
			@Override
			public @NotNull ContextMenu initialize(Component component) {
				return new DefaultComponentContextMenu(((ArmaControlRenderer) component).getMyControl());
			}
		});
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

	void keyEvent(String text, boolean keyDown, boolean shiftDown, boolean controlDown, boolean altDown) {
		uiCanvasEditor.keyEvent(text, keyDown, shiftDown, controlDown, altDown);
	}

	void repaintCanvas() {
		uiCanvasEditor.paint();
	}

	UICanvasEditor getUiCanvasEditor() {
		return uiCanvasEditor;
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
