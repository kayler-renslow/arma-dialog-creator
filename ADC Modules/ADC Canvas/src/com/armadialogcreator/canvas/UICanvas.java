package com.armadialogcreator.canvas;

import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 @author Kayler
 @since 05/11/2016. */
public abstract class UICanvas<N extends UINode> extends AnchorPane {

	/** javafx Canvas */
	protected final Canvas canvas;

	/** Graphics for the canvas */
	protected final Graphics gc;

	/** The timer that handles repainting */
	protected final CanvasAnimationTimer timer;

	protected final Resolution resolution;

	protected UINode rootNode;

	/** Background image of the canvas */
	protected ImagePattern backgroundImage = null;

	/** Background color of the canvas */
	protected Color backgroundColor = Color.WHITE;

	/** Mouse button that is currently down */
	protected final Point lastMousePosition = new Point(-1, -1);//last x and y positions of the mouse relative to the canvas

	protected Keys keys = new Keys();

	/** All components added */
	private final List<CanvasComponent> components = new ArrayList<>();

	private volatile boolean needPaint = false;
	/** A synchronization lock for {@link #needPaint} to help prevent data races */
	private final Object needPaintLock = new Object();
	/** Set to true if {@link #requestPaint()} is not necessary and will always paint when {@link #timer} wants to */
	protected boolean alwaysPaint = false;

	private final UpdateGroupListener renderUpdateGroupListener = (group, data) -> {
		requestPaint();
	};

	public UICanvas(@NotNull Resolution resolution, @NotNull UINode rootNode) {
		this.resolution = resolution;
		resolution.getUpdateGroup().addListener(new UpdateGroupListener<Resolution>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<Resolution> group, @NotNull Resolution newResolution) {
				if (getCanvasHeight() != newResolution.getScreenHeight() || getCanvasWidth() != newResolution.getScreenWidth()) {
					canvas.setWidth(newResolution.getScreenWidth());
					canvas.setHeight(newResolution.getScreenHeight());
				}
				for (UINode node : UICanvas.this.rootNode.deepIterateChildren()) {
					CanvasComponent component = node.getComponent();
					if (component != null) {
						component.resolutionUpdate(newResolution);
					}
				}
				requestPaint();
			}
		});

		this.canvas = new Canvas(resolution.getScreenWidth(), resolution.getScreenHeight());
		this.gc = new Graphics(canvas.getGraphicsContext2D());

		this.getChildren().add(this.canvas);
		UICanvas.CanvasMouseEvent mouseEvent = new UICanvas.CanvasMouseEvent(this);

		this.setOnMousePressed(mouseEvent);
		this.setOnMouseReleased(mouseEvent);
		this.setOnMouseMoved(mouseEvent);
		this.setOnMouseDragged(mouseEvent);

		//do this last
		this.rootNode = rootNode;
		setUINodeListeners(true);

		timer = new CanvasAnimationTimer();
		timer.start();

	}

	@NotNull
	public UINode getRootNode() {
		return rootNode;
	}

	public int getCanvasWidth() {
		return (int) canvas.getWidth();
	}

	public int getCanvasHeight() {
		return (int) this.canvas.getHeight();
	}

	public void setRootNode(@NotNull UINode rootNode) {
		setUINodeListeners(false);
		this.rootNode = rootNode;
		setUINodeListeners(true);
		requestPaint();
	}

	@SuppressWarnings("unchecked")
	private void setUINodeListeners(boolean add) {
		if (add) {
			this.rootNode.renderUpdateGroup().addListener(renderUpdateGroupListener);
		} else {
			this.rootNode.renderUpdateGroup().removeListener(renderUpdateGroupListener);
		}
	}

	/** Adds a component to the canvas and repaints the canvas */
	public void addComponent(@NotNull CanvasComponent component) {
		this.components.add(component);
		this.components.sort(CanvasComponent.RENDER_PRIORITY_COMPARATOR);
		requestPaint();
	}

	/**
	 Removes the given component from the canvas render and user interaction.

	 @param component component to remove
	 @return true if the component was removed, false if nothing was removed
	 */
	public boolean removeComponent(@NotNull CanvasComponent component) {
		boolean ret = this.components.remove(component);
		this.components.sort(CanvasComponent.RENDER_PRIORITY_COMPARATOR);
		requestPaint();
		return ret;
	}

	/**
	 Paint the canvas. Order of painting is:
	 <ol>
	 <li>background</li>
	 <li>{@link #getRootNode()}</li>
	 <li>components inserted via {@link #addComponent(CanvasComponent)}</li>
	 </ol>
	 */
	protected void paint() {
		gc.setTextBaseline(VPos.TOP); //we actually need to run this with each call for some reason
		gc.save();
		paintBackground();
		paintRootNode();
		paintComponents();
		for (Consumer<Graphics> f : gc.getPaintLast()) {
			f.accept(gc);
		}
		gc.getPaintLast().clear();
		gc.restore();
	}

	/**
	 Request a repaint.
	 The paint operation won't happen until {@link #getTimer()} discovers the paint request.
	 Therefore, multiple requests can be made and not have any performance impacts.
	 <p>
	 This method can be used across multiple threads.
	 */
	public void requestPaint() {
		synchronized (needPaintLock) {
			needPaint = true;
		}
	}

	/**
	 Paints all nodes in {@link #getRootNode()} and will iterate each child's child as well.
	 Each component will get an individual render space (GraphicsContext attributes will not bleed through each component).
	 */
	protected void paintRootNode() {
		paintNodes(rootNode);
	}

	private void paintNodes(@NotNull UINode node) {
		for (UINode child : node.deepIterateChildren()) {
			paintNode(child);
		}
	}

	/**
	 Paints all components. Each component will get an individual render space
	 (GraphicsContext attributes will not bleed through each component).
	 Before the paint, the components are sorted with {@link CanvasComponent#RENDER_PRIORITY_COMPARATOR}
	 */
	protected void paintComponents() {
		for (CanvasComponent component : components) {
			paintComponent(component);
		}
	}

	protected void paintBackground() {
		gc.setFill(backgroundColor);
		gc.fillRectNoAA(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
		if (backgroundImage == null) {
			return;
		}
		gc.setFill(backgroundImage);
		gc.fillRectNoAA(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
	}

	protected void paintNode(@NotNull UINode node) {
		if (node.getComponent() == null) {
			return;
		}
		paintComponent(node.getComponent());
	}

	protected void paintComponent(@NotNull CanvasComponent component) {
		if (component.isGhost()) {
			return;
		}
		gc.save();
		component.paint(gc);
		gc.restore();
	}

	/** Sets canvas background image and automatically repaints */
	public void setCanvasBackgroundImage(@Nullable ImagePattern background) {
		this.backgroundImage = background;
		requestPaint();
	}

	/** Sets canvas background color and repaints the canvas */
	public void setCanvasBackgroundColor(@NotNull Color color) {
		this.backgroundColor = color;
		requestPaint();
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


	/**
	 This is called after mouseMove is called.
	 This will ensure that no matter how mouse move exits, the last mouse position will be updated
	 */
	private void setLastMousePosition(int mousex, int mousey) {
		lastMousePosition.set(mousex, mousey);
	}

	@NotNull
	public CanvasAnimationTimer getTimer() {
		return timer;
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

	/** Clear any listeners attached to {@link #rootNode} */
	public void clearListeners() {
		setUINodeListeners(false);
	}

	/**
	 Created by Kayler on 05/13/2016.
	 */
	private static class CanvasMouseEvent implements EventHandler<MouseEvent> {
		private final UICanvas canvas;
		private boolean mouseDown = false;

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
				if (mouseDown) {
					this.canvas.requestPaint();
				}
			} else {
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					mouseDown = true;
					canvas.mousePressed(mousex, mousey, btn);
				} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					canvas.mouseReleased(mousex, mousey, btn);
					mouseDown = false;
					canvas.requestPaint();
				}
			}

		}

	}

	public class CanvasAnimationTimer extends AnimationTimer {

		private final List<Runnable> runnables = new ArrayList<>();

		@Override
		public void handle(long now) {
			for (Runnable r : runnables) {
				r.run();
			}
			if (!alwaysPaint) {
				synchronized (needPaintLock) {
					//synchronize to prevent data race
					if (needPaint) {
						needPaint = false;
						paint();
					}
				}
			} else {
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
