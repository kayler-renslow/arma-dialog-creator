package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import com.kaylerrenslow.armaDialogCreator.gui.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Point;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.util.List;

/**
 @author Kayler
 @since 05/11/2016. */
public abstract class UICanvas extends AnchorPane {

	/** javafx Canvas */
	protected final Canvas canvas;
	/** GraphicsContext for the canvas */
	protected final GraphicsContext gc;
	/** {@link DataContext} for the canvas */
	protected final DataContext dataContext = new DataContext();
	/** The timer that handles repainting */
	protected final CanvasAnimationTimer timer;

	protected final Resolution resolution;

	protected @NotNull CanvasDisplay<? extends CanvasControl> display;

	/** Background image of the canvas */
	protected ImagePattern backgroundImage = null;

	/** Background color of the canvas */
	protected Color backgroundColor = CanvasViewColors.EDITOR_BG;

	/** Mouse button that is currently down */
	protected final Point lastMousePosition = new Point(-1, -1);//last x and y positions of the mouse relative to the canvas

	protected Keys keys = new Keys();

	/** All components added */
	protected final ObservableList<CanvasComponent> components = FXCollections.observableArrayList(new ArrayList<>());

	private boolean needPaint;
	private final UpdateGroupListener renderUpdateGroupListener = new UpdateGroupListener() {
		@Override
		public void update(@NotNull UpdateListenerGroup group, @Nullable Object data) {
			requestPaint();
		}
	};

	public UICanvas(@NotNull Resolution resolution, @NotNull CanvasDisplay<? extends CanvasControl> display) {
		this.resolution = resolution;
		resolution.getUpdateGroup().addListener(new UpdateGroupListener<Resolution>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<Resolution> group, Resolution newResolution) {
				if (getCanvasHeight() != newResolution.getScreenHeight() || getCanvasWidth() != newResolution.getScreenWidth()) {
					canvas.setWidth(newResolution.getScreenWidth());
					canvas.setHeight(newResolution.getScreenHeight());
				}
				display.resolutionUpdate(newResolution);
				requestPaint();
			}
		});

		this.canvas = new Canvas(resolution.getScreenWidth(), resolution.getScreenHeight());
		this.gc = this.canvas.getGraphicsContext2D();

		this.getChildren().add(this.canvas);
		UICanvas.CanvasMouseEvent mouseEvent = new UICanvas.CanvasMouseEvent(this);

		this.setOnMousePressed(mouseEvent);
		this.setOnMouseReleased(mouseEvent);
		this.setOnMouseMoved(mouseEvent);
		this.setOnMouseDragged(mouseEvent);
		components.addListener(new ListChangeListener<CanvasComponent>() {
			@Override
			public void onChanged(Change<? extends CanvasComponent> c) {
				requestPaint();
			}
		});

		//do this last
		this.display = display;
		setDisplayListeners(true);

		timer = new CanvasAnimationTimer();
		timer.start();

	}

	public int getCanvasWidth() {
		return (int) canvas.getWidth();
	}

	public int getCanvasHeight() {
		return (int) this.canvas.getHeight();
	}

	public void setDisplay(@NotNull CanvasDisplay<? extends CanvasControl> display) {
		setDisplayListeners(false);
		this.display = display;
		setDisplayListeners(true);
		requestPaint();
	}

	@SuppressWarnings("unchecked")
	private void setDisplayListeners(boolean add) {
		if (add) {
			this.display.getReRenderUpdateGroup().addListener(renderUpdateGroupListener);
		} else {
			this.display.getReRenderUpdateGroup().removeListener(renderUpdateGroupListener);
		}
	}

	/** Adds a component to the canvas and repaints the canvas */
	public void addComponent(@NotNull CanvasComponent component) {
		this.components.add(component);
	}

	/**
	 Removes the given component from the canvas render and user interaction.

	 @param component component to remove
	 @return true if the component was removed, false if nothing was removed
	 */
	public boolean removeComponent(@NotNull CanvasComponent component) {
		return this.components.remove(component);
	}

	/**
	 Paint the canvas. Order of painting is:
	 <ol>
	 <li>background</li>
	 <li>display/controls</li>
	 <li>components inserted via {@link #addComponent(CanvasComponent)}</li>
	 </ol>
	 */
	protected void paint() {
		gc.setTextBaseline(VPos.BASELINE); //we actually need to run this with each call for some reason
		gc.save();
		paintBackground();
		paintControls();
		paintComponents();
		gc.restore();
	}

	/**
	 Request a repaint.
	 The paint operation won't happen until {@link #getTimer()} discovers the paint request.
	 Therefore, multiple requests can be made and not have any performance impacts.
	 */
	public void requestPaint() {
		needPaint = true;
	}

	/**
	 Paints all controls inside the display set {@link #display}.
	 Each component will get an individual render space (GraphicsContext attributes will not bleed through each component).
	 The background controls are painted first, then controls are painted
	 */
	protected void paintControls() {
		for (CanvasControl control : display.getBackgroundControls()) {
			paintControl(control);
		}
		for (CanvasControl control : display.getControls()) {
			paintControl(control);
		}
	}

	/**
	 Paints all components. Each component will get an individual render space
	 (GraphicsContext attributes will not bleed through each component).
	 Before the paint, the components are sorted with {@link CanvasComponent#RENDER_PRIORITY_COMPARATOR}
	 */
	protected void paintComponents() {
		this.components.sort(CanvasComponent.RENDER_PRIORITY_COMPARATOR);
		for (CanvasComponent component : components) {
			paintComponent(component);
		}
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

	protected void paintControl(CanvasControl control) {
		gc.save();
		paintComponent(control.getRenderer());
		gc.restore();
	}

	protected void paintComponent(CanvasComponent component) {
		if (component.isGhost()) {
			return;
		}
		gc.save();
		component.paint(gc, dataContext);
		gc.restore();
	}

	/** Sets canvas background image and automatically repaints */
	public void setCanvasBackgroundImage(@Nullable ImagePattern background) {
		this.backgroundImage = background;
	}

	/** Sets canvas background color and repaints the canvas */
	public void setCanvasBackgroundColor(@NotNull Color color) {
		this.backgroundColor = color;
	}

	/** @return the background image, or null if not set */
	@Nullable
	public ImagePattern getBackgroundImage() {
		return backgroundImage;
	}

	/** @return the background color */
	@NotNull
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 This is called when the mouse listener is invoked and a mouse press was the event.
	 Default implementation does nothing.

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was pressed
	 */
	protected void mousePressed(int mousex, int mousey, @NotNull MouseButton mb) {
	}

	/**
	 This is called when the mouse listener is invoked and a mouse release was the event.
	 Default implementation does nothing.

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was released
	 */
	protected void mouseReleased(int mousex, int mousey, @NotNull MouseButton mb) {
	}

	/**
	 This is called when the mouse is moved and/or dragged inside the canvas. Default implementation does nothing.

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 */
	protected void mouseMoved(int mousex, int mousey) {
	}


	/**
	 This should be called when any mouse event occurs (press, release, drag, move, etc)

	 @param shiftDown true if the shift key is down, false otherwise
	 @param ctrlDown true if the ctrl key is down, false otherwise
	 @param altDown true if alt key is down, false otherwise
	 */
	public void keyEvent(String key, boolean keyIsDown, boolean shiftDown, boolean ctrlDown, boolean altDown) {
		keys.update(key, keyIsDown, shiftDown, ctrlDown, altDown);
		requestPaint();
	}


	/** This is called after mouseMove is called.
	 This will ensure that no matter how mouse move exits, the last mouse position will be updated */
	private void setLastMousePosition(int mousex, int mousey) {
		lastMousePosition.set(mousex, mousey);
	}

	@NotNull
	public CanvasAnimationTimer getTimer() {
		return timer;
	}

	@NotNull
	public DataContext getDataContext() {
		return dataContext;
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

	@NotNull
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
			this.canvas.requestPaint();
		}

	}

	public class CanvasAnimationTimer extends AnimationTimer {

		private final List<Runnable> runnables = new ArrayList<>();

		@Override
		public void handle(long now) {
			for (Runnable r : runnables) {
				r.run();
			}
			if (needPaint) {
				needPaint = false;
				paint();
			}
		}

		/** @return a list of runnables to run on each timer update */
		@NotNull
		public List<Runnable> getRunnables() {
			return runnables;
		}
	}

}
