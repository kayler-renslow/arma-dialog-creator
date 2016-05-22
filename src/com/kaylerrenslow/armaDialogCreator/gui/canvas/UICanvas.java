package com.kaylerrenslow.armaDialogCreator.gui.canvas;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Component;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.PaintedRegion;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.util.Point;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author Kayler
 Created on 05/11/2016. */
public abstract class UICanvas extends AnchorPane {

	/** javafx Canvas */
	protected final Canvas canvas;
	/** GraphicsContext for the canvas */
	protected final GraphicsContext gc;

	/** Width of canvas */
	private int canvasWidth,
	/** Height of canvas */
	canvasHeight;

	/** Background image of the canvas */
	protected ImagePattern backgroundImage = null;

	/** Background color of the canvas */
	protected Color backgroundColor = CanvasViewColors.EDITOR_BG;

	/** All components added */
	protected ArrayList<Component> components = new ArrayList<>();

	/** Mouse button that is currently down */
	protected final Point lastMousePosition = new Point(-1, -1);//last x and y positions of the mouse relative to the canvas

	protected Keys keys = new Keys();

	public UICanvas(int width, int height) {
		this.canvas = new Canvas(width, height);
		this.canvasWidth = width;
		this.canvasHeight = height;
		this.gc = this.canvas.getGraphicsContext2D();
		gc.setTextBaseline(VPos.CENTER);

		this.getChildren().add(this.canvas);
		UICanvas.CanvasMouseEvent mouseEvent = new UICanvas.CanvasMouseEvent(this);

		this.setOnMousePressed(mouseEvent);
		this.setOnMouseReleased(mouseEvent);
		this.setOnMouseMoved(mouseEvent);
		this.setOnMouseDragged(mouseEvent);
	}

	public int getCanvasWidth() {
		return this.canvasWidth;
	}

	public int getCanvasHeight() {
		return this.canvasHeight;
	}

	public void setCanvasSize(int width, int height) {
		this.canvasWidth = width;
		this.canvasHeight = height;
		this.canvas.setWidth(width);
		this.canvas.setHeight(height);
	}

	/** Adds a component to the canvas */
	public void addComponent(@NotNull Component component) {
		this.components.add(component);
		paint();
	}

	/**
	 Removes the given component from the canvas render and user interaction.

	 @param component component to remove
	 @return true if the component was removed, false if nothing was removed
	 */
	public boolean removeComponent(@NotNull Component component) {
		boolean removed = this.components.remove(component);
		paint();
		return removed;
	}


	/** Paint the canvas */
	public void paint() {
		gc.save();
		paintBackground();
		this.components.sort(PaintedRegion.RENDER_PRIORITY_COMPARATOR);
		for (Component component : components) {
			if (component.isGhost()) {
				continue;
			}
			paintComponent(component);
		}
		gc.restore();
	}

	protected void paintBackground() {
		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
		if (backgroundImage == null) {
			return;
		}
		gc.setFill(backgroundImage);
		gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
	}

	protected void paintComponent(Component component) {
		gc.save();
		if (!component.isGhost()) {
			component.paint(gc);
		}
		gc.restore();
	}

	/** Sets canvas background image and automatically repaints */
	public void setCanvasBackgroundImage(@Nullable ImagePattern background) {
		this.backgroundImage = background;
	}

	/** Sets canvas background color and repaints the canvas*/
	public void setCanvasBackgroundColor(@NotNull Color color) {
		this.backgroundColor = color;
		paint();
	}


	/**
	 This is called when the mouse listener is invoked and a mouse press was the event.
	 This method should be the only one dealing with adding and removing components from the selection, other than mouseMove which adds to the selection via the selection box

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was pressed
	 */
	protected abstract void mousePressed(int mousex, int mousey, @NotNull MouseButton mb);

	/**
	 This is called when the mouse listener is invoked and a mouse release was the event

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was released
	 */
	protected abstract void mouseReleased(int mousex, int mousey, @NotNull MouseButton mb);

	/**
	 This is called when the mouse is moved and/or dragged inside the canvas

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 */
	protected abstract void mouseMoved(int mousex, int mousey);


	/**
	 This should be called when any mouse event occurs (press, release, drag, move, etc)

	 @param shiftDown true if the shift key is down, false otherwise
	 @param ctrlDown true if the ctrl key is down, false otherwise
	 @param altDown true if alt key is down, false otherwise
	 */
	public void keyEvent(String key, boolean keyIsDown, boolean shiftDown, boolean ctrlDown, boolean altDown) {
		keys.update(key, keyIsDown, shiftDown, ctrlDown, altDown);
		paint();
	}


	/** This is called after mouseMove is called. This will ensure that no matter how mouse move exits, the last mouse position will be updated */
	private void setLastMousePosition(int mousex, int mousey) {
		lastMousePosition.set(mousex, mousey);
	}


	@Override
	protected double computeMinWidth(double height) {
		return getCanvasWidth();
	}

	@Override
	protected double computeMinHeight(double width) {
		return getCanvasHeight();
	}

	@Override
	protected double computePrefWidth(double height) {
		return getCanvasWidth();
	}

	@Override
	protected double computePrefHeight(double width) {
		return getCanvasHeight();
	}

	@Override
	protected double computeMaxWidth(double height) {
		return super.computeMaxWidth(height);
	}

	@Override
	protected double computeMaxHeight(double width) {
		return super.computeMaxHeight(width);
	}

	public Canvas getCanvas() {
		return canvas;
	}


	/**
	 Created by Kayler on 05/13/2016.
	 */
	private static class CanvasMouseEvent implements EventHandler<MouseEvent> {
		private final UICanvas canvas;

		CanvasMouseEvent(UICanvas canvas) {
			this.canvas = canvas;
		}

		@Override
		public void handle(MouseEvent event) {
			MouseButton btn = event.getButton();
			if (!(event.getTarget() instanceof Canvas)) {
				return;
			}

			Canvas c = (Canvas) event.getTarget();
			Point2D p = c.sceneToLocal(event.getSceneX(), event.getSceneY());
			int mousex = (int) p.getX();
			int mousey = (int) p.getY();

			if (event.getEventType() == MouseEvent.MOUSE_MOVED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				canvas.mouseMoved(mousex, mousey);
				canvas.setLastMousePosition(mousex, mousey);
			} else {
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					canvas.mousePressed(mousex, mousey, btn);
				} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					canvas.mouseReleased(mousex, mousey, btn);
				}
			}
			canvas.paint();

		}

	}

}
