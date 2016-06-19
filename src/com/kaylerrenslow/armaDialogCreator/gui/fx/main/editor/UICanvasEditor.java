package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlClass;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Edge;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Region;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ViewportComponent;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.SimpleCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.util.MathUtil;
import com.kaylerrenslow.armaDialogCreator.util.Point;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author Kayler
 Created on 05/11/2016. */
public class UICanvasEditor extends UICanvas {

	/*** How many pixels the cursor can be off on a component's edge when choosing an edge for scaling */
	private static final int COMPONENT_EDGE_LEEWAY = 5;
	private static final long DOUBLE_CLICK_WAIT_TIME_MILLIS = 300;

	/** Color of the mouse selection box */
	private Color selectionColor = CanvasViewColors.SELECTION;

	/** Color of the grid */
	private Color gridColor = CanvasViewColors.GRID;

	/** True if grid is being shown, false otherwise */
	private boolean showGrid = true;

	private CanvasSelection selection = new CanvasSelection();

	/** Mouse button that is currently down */
	private MouseButton mouseButtonDown = MouseButton.NONE;
	private long lastMousePressTime;

	/** amount of change that has happened since last snap */
	private int dxAmount, dyAmount = 0;
	/** If true, snapping is calculated via the viewport width and height. If not, it's calculated by the canvas width and height */
	private boolean snapRelativeToViewport = true;

	private KeyMap keyMap = new KeyMap();

	/** Component that is ready to be scaled, null if none is ready to be scaled */
	private CanvasComponent scaleComponent;
	/** Edge that the scaling will be conducted, or Edge.NONE is no scaling is being done */
	private Edge scaleEdge = Edge.NONE;
	/** Component that the mouse is over, or null if not over any component */
	private CanvasComponent mouseOverComponent;
	/** Component that the component context menu was created on, or null if the component context menu isn't open */
	private CanvasComponent contextMenuComponent;

	private SnapConfiguration calc;

	/** Class that generates context menus for the components */
	private ComponentContextMenuCreator menuCreator;
	/** Context menu to show when user right clicks and no component is selected */
	private ContextMenu canvasContextMenu;
	/** The context menu that wants to be shown */
	private ContextMenu contextMenu;
	private final Point contextMenuPosition = new Point(-1, -1);

	/** If true, scaling and translating components will only work when the actions don't put their bounds outside the canvas. If false, all scaling and translating is allowed. */
	private boolean safeMovement = false;

	private final ArmaResolution resolution;
	private final ArmaAbsoluteBoxComponent absRegionComponent;

	private boolean waitingForZXRelease = false;
	private long zxPressStartTimeMillis;

	private final ValueListener<ArmaControlClass> CONTROL_LISTENER = new ValueListener<ArmaControlClass>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<ArmaControlClass> observer, ArmaControlClass newValue, ArmaControlClass oldValue) {
			paint();
		}
	};


	public UICanvasEditor(ArmaResolution resolution, SnapConfiguration calculator) {
		super(resolution.getScreenWidth(), resolution.getScreenHeight());
		this.resolution = resolution;

		setPositionCalculator(calculator);

		gc.setTextBaseline(VPos.CENTER);
		this.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				if (contextMenu != null) {
					Point2D p = getCanvas().localToScreen(contextMenuPosition.getX(), contextMenuPosition.getY());
					contextMenu.show(getCanvas(), p.getX(), p.getY());
				}
			}
		});

		keys.getKeyStateObserver().addValueListener(new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean newValue, Boolean oldValue) {
				keyUpdate(newValue);
			}
		});

		absRegionComponent = new ArmaAbsoluteBoxComponent(resolution);
	}

	private void keyUpdate(boolean keyIsDown) {
		if (keyIsDown) {
			if (keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT) && keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT)) {
				zxPressStartTimeMillis = System.currentTimeMillis();
				waitingForZXRelease = true;
			}
		} else {
			if (waitingForZXRelease && !keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT) && !keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT)) {
				if (zxPressStartTimeMillis + 500 <= System.currentTimeMillis()) {
					for (CanvasComponent component : selection.getSelected()) {
						component.setEnabled(false);
					}
					selection.clearSelected();
					mouseOverComponent = scaleComponent = null;
					changeCursorToDefault();
					waitingForZXRelease = false;
				}
			}
		}
	}

	@Override
	public void addComponentNoPaint(@NotNull CanvasComponent component) {
		super.addComponentNoPaint(component);
		if (component instanceof ArmaControlRenderer) {
			ArmaControlRenderer renderer = (ArmaControlRenderer) component;
			renderer.getMyControl().getControlListener().addValueListener(CONTROL_LISTENER);
		}
	}

	@Override
	public void addComponent(@NotNull CanvasComponent component) {
		addComponentNoPaint(component); //intentionally using addComponentNoPaint so that there is less duplicate code
		paint();
	}

	/**
	 Removes the given component from the canvas render and user interaction. This also removes the component from the selection

	 @param component component to remove
	 @return true if the component was removed, false if nothing was removed
	 */
	public boolean removeComponent(@NotNull CanvasComponent component) {
		boolean removed = removeComponentNoPaint(component);
		paint();
		return removed;
	}

	@Override
	public boolean removeComponentNoPaint(@NotNull CanvasComponent component) {
		boolean removed = super.removeComponentNoPaint(component);
		if (removed) {
			this.selection.removeFromSelection(component);
			if (component instanceof ArmaControlRenderer) {
				ArmaControlRenderer renderer = (ArmaControlRenderer) component;
				renderer.getMyControl().getControlListener().removeListener(CONTROL_LISTENER);
			}
		}
		return removed;
	}

	public void setPositionCalculator(@NotNull SnapConfiguration positionCalculator) {
		this.calc = positionCalculator;
	}

	public @NotNull SnapConfiguration getSnapConfig() {
		return this.calc;
	}

	public @NotNull Selection getSelection() {
		return selection;
	}

	/**
	 @param ccm the context menu creator that is used to give Components context menus
	 */
	public void setComponentMenuCreator(@Nullable ComponentContextMenuCreator ccm) {
		this.menuCreator = ccm;
	}

	public void setCanvasContextMenu(@Nullable ContextMenu contextMenu) {
		this.canvasContextMenu = contextMenu;
	}

	/** see getSafeMovement for more information */
	public void setSafeMovement(boolean safe) {
		this.safeMovement = safe;
	}

	/** If true, scaling and translating components will only work when the actions don't put their bounds outside the canvas. If false, all scaling and translating is allowed. */
	public boolean getSafeMovement() {
		return this.safeMovement;
	}

	/**
	 Updates the resolution of this canvas.
	 */
	public void updateResolution(ArmaResolution r) {
		this.resolution.setTo(r);
		absRegionComponent.updateToNewResolution(r);
		paint();
	}


	/** Paint the canvas */
	public void paint() {
		super.paint();
		if (selection.isSelecting()) {
			gc.save();
			gc.setStroke(selectionColor);
			gc.setLineWidth(2);
			selection.drawRectangle(gc);
			gc.restore();
		}
		if (absRegionComponent.alwaysRenderAtFront()) {
			paintAbsRegionComponent();
		}
	}

	@Override
	protected void paintComponents() {
		if (!absRegionComponent.alwaysRenderAtFront()) {
			paintAbsRegionComponent();
		}
		super.paintComponents();
		gc.save();
		for (CanvasComponent component : selection.getSelected()) {
			gc.setStroke(component.getBackgroundColor());
			component.drawRectangle(gc);
		}
		gc.restore();
	}

	@Override
	protected void paintBackground() {
		super.paintBackground();
		if (showGrid) {
			drawGrid();
		}
	}

	@Override
	protected void paintComponent(CanvasComponent component) {
		boolean selected = selection.isSelected(component);
		if (selected) {
			gc.save();
			int centerx = component.getCenterX();
			int centery = component.getCenterY();
			boolean noHoriz = keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT);
			boolean noVert = keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT);
			if (noHoriz) {
				gc.setStroke(selectionColor);
				gc.setLineWidth(4);
				gc.strokeLine(centerx, 0, centerx, getCanvasHeight());
			}
			if (noVert) {
				gc.setStroke(selectionColor);
				gc.setLineWidth(4);
				gc.strokeLine(0, centery, getCanvasWidth(), centery);
			}
			//draw selection 'shadow'
			gc.setGlobalAlpha(0.6);
			gc.setStroke(selectionColor);
			int offset = 4 + (component.getBorder() != null ? component.getBorder().getThickness() : 0);
			Region.fillRectangle(gc, component.getLeftX() - offset, component.getTopY() - offset, component.getRightX() + offset, component.getBottomY() + offset);
			gc.restore();
		}
		if (selection.isSelecting() && !component.isEnabled() && selection.getArea() > 10) {
			return;
		}
		super.paintComponent(component);
	}

	protected void paintAbsRegionComponent() {
		if (!absRegionComponent.isGhost()) {
			absRegionComponent.paint(gc);
		}
	}

	private void drawGrid() {
		int w = getCanvasWidth();
		int h = getCanvasHeight();
		int spacingX = getSnapPixelsWidth(calc.snapPercentage());
		int spacingY = getSnapPixelsHeight(calc.snapPercentage());
		if (spacingX <= 0 || spacingY <= 0) {
			return;
		}
		int numX = w / spacingX;
		int numY = h / spacingY;
		double ys, xs;
		double antiAlias = 0.5;
		gc.save();
		if (snapRelativeToViewport) {
			gc.translate(Math.floor(resolution.getViewportX() % spacingX) * 0.5, Math.floor(resolution.getViewportY() % spacingY) * 0.5);
		}
		gc.setStroke(gridColor);
		for (int y = 0; y <= numY; y++) {
			ys = y * spacingY;
			gc.strokeLine(0 + antiAlias, ys + antiAlias, w - antiAlias, ys + antiAlias);
			for (int x = 0; x <= numX; x++) {
				xs = x * spacingX;
				gc.strokeLine(xs + antiAlias, 0 + antiAlias, xs + antiAlias, h - antiAlias);
			}
		}
		gc.restore();
	}


	/**
	 Check if the bound update to the region will keep the boundaries inside the canvas

	 @param r region to check bounds of
	 @param dxLeft change in x on the left side
	 @param dxRight change in x on the right side
	 @param dyTop change in y on the top side
	 @param dyBottom change in y on the bottom side
	 @return true if the bounds can be updated, false otherwise
	 */
	private boolean boundUpdateSafe(Region r, int dxLeft, int dxRight, int dyTop, int dyBottom) {
		return boundSetSafe(r, r.getLeftX() + dxLeft, r.getRightX() + dxRight, r.getTopY() + dyTop, r.getBottomY() + dyBottom);
	}

	/**
	 Check if the bounds set to the region will keep the boundaries inside the canvas

	 @param r region to check bounds of
	 @param x1 new x1 position
	 @param x2 new x2 position
	 @param y1 new y1 position
	 @param y2 new y2 position
	 @return true if the bounds can be updated, false otherwise
	 */
	private boolean boundSetSafe(Region r, int x1, int x2, int y1, int y2) {
		boolean outX = MathUtil.outOfBounds(x1, 0, getCanvasWidth() - r.getWidth()) || MathUtil.outOfBounds(x2, 0, getCanvasWidth());
		if (!outX) {
			boolean outY = MathUtil.outOfBounds(y1, 0, getCanvasHeight() - r.getHeight()) || MathUtil.outOfBounds(y2, 0, getCanvasHeight());
			if (!outY) {
				return true;
			}
		}
		return false;
	}

	private void changeCursorToMove() {
		canvas.setCursor(Cursor.MOVE);
	}

	private void changeCursorToDefault() {
		canvas.setCursor(Cursor.DEFAULT);
	}

	private void changeCursorToScale(Edge edge) {
		if (edge == Edge.NONE) {
			changeCursorToDefault();
			return;
		}
		if (edge == Edge.TOP_LEFT || edge == Edge.BOTTOM_RIGHT) {
			canvas.setCursor(Cursor.NW_RESIZE);
			return;
		}
		if (edge == Edge.TOP_RIGHT || edge == Edge.BOTTOM_LEFT) {
			canvas.setCursor(Cursor.NE_RESIZE);
			return;
		}
		if (edge == Edge.TOP || edge == Edge.BOTTOM) {
			canvas.setCursor(Cursor.N_RESIZE);
			return;
		}
		if (edge == Edge.LEFT || edge == Edge.RIGHT) {
			canvas.setCursor(Cursor.W_RESIZE);
			return;
		}
		throw new IllegalStateException("couldn't find correct cursor for edge:" + edge.name());
	}

	/**
	 This is called when the mouse listener is invoked and a mouse press was the event.
	 This method should be the only one dealing with adding and removing components from the selection, other than mouseMove which adds to the selection via the selection box

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was pressed
	 */
	@Override
	protected void mousePressed(int mousex, int mousey, @NotNull MouseButton mb) {
		if (getContextMenu() != null) {
			getContextMenu().hide();
		}
		boolean doubleClick = System.currentTimeMillis() - lastMousePressTime <= DOUBLE_CLICK_WAIT_TIME_MILLIS;
		lastMousePressTime = System.currentTimeMillis();
		selection.setSelecting(false);
		this.mouseButtonDown = mb;

		if (scaleComponent != null && mb == MouseButton.PRIMARY) { //only select component that is being scaled to prevent multiple scaling
			selection.removeAllAndAdd(scaleComponent);
			return;
		}
		if (selection.numSelected() == 0 && mouseOverComponent != null) { //nothing is selected, however, mouse is over a component so we need to select that
			selection.addToSelection(mouseOverComponent);
			return;
		}
		if (selection.numSelected() == 0 && mouseOverComponent == null && mb == MouseButton.SECONDARY) { //nothing is selected and right clicking the canvas
			selection.clearSelected();
			return;
		}
		if (selection.numSelected() > 0 && mb == MouseButton.SECONDARY) { //check to see if right click is over a selected component
			CanvasComponent component;
			for (int i = selection.numSelected() - 1; i >= 0; i--) {
				component = selection.getSelected().get(i);
				if (component.containsPoint(mousex, mousey)) {
					selection.removeAllAndAdd(component); //only 1 can be selected
					return;
				}
			}
			for (int i = components.size() - 1; i >= 0; i--) {
				component = components.get(i);
				if (!component.isEnabled()) {
					continue;
				}
				if (component.containsPoint(mousex, mousey)) {
					selection.removeAllAndAdd(component);
					return;
				}
			}
			selection.clearSelected();
			return;
		}
		if (mouseOverComponent != null) {
			if (keys.isCtrlDown()) {
				selection.toggleFromSelection(mouseOverComponent);
				return;
			} else {
				if (selection.numSelected() > 0) {
					if (selection.isSelected(mouseOverComponent)) {
						if (doubleClick) {
							selection.removeAllAndAdd(mouseOverComponent);
						}
						return;
					}
					if (!keys.spaceDown()) { //if space is down, mouse over component should be selected
						CanvasComponent component;
						for (int i = selection.numSelected() - 1; i >= 0; i--) {
							component = selection.getSelected().get(i);
							if (component.containsPoint(mousex, mousey)) { //allow this one to stay selected despite the mouse not being over it
								return;
							}
						}
					}
				}
				selection.removeAllAndAdd(mouseOverComponent);
				return;
			}
		}

		selection.clearSelected();
		selection.beginSelecting(mousex, mousey);
	}

	/**
	 This is called when the mouse listener is invoked and a mouse release was the event

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was released
	 */
	protected void mouseReleased(int mousex, int mousey, @NotNull MouseButton mb) {
		this.mouseButtonDown = MouseButton.NONE;
		selection.setSelecting(false);
		setContextMenu(null, mousex, mousey);
		contextMenuComponent = null;
		if (mb == MouseButton.SECONDARY) {
			if (menuCreator != null && selection.getFirst() != null) {
				contextMenuComponent = selection.getFirst();
				setContextMenu(menuCreator.initialize(contextMenuComponent), mousex, mousey);
			} else if (canvasContextMenu != null) {
				setContextMenu(canvasContextMenu, mousex, mousey);
			}
		}
	}

	/**
	 This is called when the mouse is moved and/or dragged inside the canvas

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 */
	protected void mouseMoved(int mousex, int mousey) {
		if (!basicMouseMovement(mousex, mousey)) {
			return;
		}
		int dx = mousex - lastMousePosition.getX(); //change in x
		int dy = mousey - lastMousePosition.getY(); //change in y
		if (keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT)) {
			dy = 0;
		}
		if (keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT)) {
			dx = 0;
		}
		int dx1 = 0; //change in x that will be used for translation or scaling
		int dy1 = 0; //change in y that will be used for translation or scaling
		int dirx = dx < 0 ? -1 : 1; //change in direction for x
		int diry = dy < 0 ? -1 : 1; //change in direction for y

		int xSnapCount = 0; //how many snaps occurred for x
		int ySnapCount = 0; //how many snaps occurred for y
		if (!keys.isAltDown()) {
			int snapX = getSnapPixelsWidth(keys.isShiftDown() ? calc.alternateSnapPercentage() : calc.snapPercentage());
			int snapY = getSnapPixelsHeight(keys.isShiftDown() ? calc.alternateSnapPercentage() : calc.snapPercentage());

			dxAmount += dx;
			dyAmount += dy;
			int dxAmountAbs = Math.abs(dxAmount);
			int dyAmountAbs = Math.abs(dyAmount);
			if (dxAmountAbs >= snapX) {
				xSnapCount = (dxAmountAbs / snapX);
				dx1 = snapX * dirx * xSnapCount;
				dxAmount = (dxAmountAbs - Math.abs(dx1)) * dirx;
			}
			if (dyAmountAbs >= snapY) {
				ySnapCount = (dyAmountAbs / snapY);
				dy1 = snapY * diry * ySnapCount;
				dyAmount = (dyAmountAbs - Math.abs(dy1)) * diry;
			}
		} else {//translate or scale how much the mouse moved
			dx1 = dx;
			dy1 = dy;
		}

		boolean canSnapViewport = !keys.isAltDown() && snapRelativeToViewport;
		double vdx = calc.snapPercentageDecimal() * xSnapCount * dirx;
		double vdy = calc.snapPercentageDecimal() * ySnapCount * diry;

		if (scaleComponent != null) { //scaling
			boolean squareScale = keys.keyIsDown(keyMap.SCALE_SQUARE);
			boolean symmetricScale = keys.isCtrlDown() || squareScale;
			if (canSnapViewport && scaleComponent instanceof ViewportComponent) {
				doScaleOnViewportComponent(symmetricScale, squareScale, vdx, vdy);
			} else {
				doScaleOnComponent(symmetricScale, squareScale, dx1, dy1);
			}
			return;
		}
		//not scaling and simply translating (moving)
		ViewportComponent viewportComponent;
		double px, py, pw, ph;

		for (CanvasComponent component : selection.getSelected()) {
			//only move-able components should be inside selection
			if (canSnapViewport && component instanceof ViewportComponent) {
				viewportComponent = ((ViewportComponent) component);
				px = viewportComponent.getPercentX() + vdx;
				py = viewportComponent.getPercentY() + vdy;
				pw = viewportComponent.getPercentW();
				ph = viewportComponent.getPercentH();
				if (safeMovement) {
					int vx = viewportComponent.calcScreenX(px);
					int vy = viewportComponent.calcScreenY(py);
					if (!boundSetSafe(component, vx, vx + viewportComponent.calcScreenWidth(pw), vy, vy + viewportComponent.calcScreenHeight(ph))) {
						continue;
					}
				}
				viewportComponent.setPercentX(px);
				viewportComponent.setPercentY(py);
				viewportComponent.setPercentW(pw);
				viewportComponent.setPercentH(ph);
			} else {
				if (!safeMovement || boundUpdateSafe(component, dx, dx, dy, dy)) { //translate if safeMovement is off or safeMovement is on and the translation doesn't move component out of bounds
					component.translate(dx1, dy1);
				}
			}
		}
	}

	private void doScaleOnComponent(boolean symmetricScale, boolean squareScale, int dx, int dy) {
		int dxl = 0; //change in x left
		int dxr = 0; //change in x right
		int dyt = 0; //change in y top
		int dyb = 0; //change in y bottom
		if (squareScale) {//scale only as a square (all changes are equal)
			//set them equal to the biggest value
			if (Math.abs(dx) > Math.abs(dy)) {
				dy = dx;
			} else {
				dx = dy;
			}
		}
		if (scaleEdge == Edge.TOP_LEFT) {
			dyt = dy;
			dxl = dx;
			if (symmetricScale) {
				dyb = -dy;
				dxr = -dx;
			}
		} else if (scaleEdge == Edge.TOP_RIGHT) {
			dyt = dy;
			dxr = dx;
			if (symmetricScale) {
				dyb = -dy;
				dxl = -dx;
			}
		} else if (scaleEdge == Edge.BOTTOM_LEFT) {
			dyb = dy;
			dxl = dx;
			if (symmetricScale) {
				dyt = -dy;
				dxr = -dx;
			}
		} else if (scaleEdge == Edge.BOTTOM_RIGHT) {
			dyb = dy;
			dxr = dx;
			if (symmetricScale) {
				dyt = -dy;
				dxl = -dx;
			}
		} else if (scaleEdge == Edge.TOP) {
			dyt = dy;
			if (symmetricScale) {
				dyb = -dy;
			}
		} else if (scaleEdge == Edge.RIGHT) {
			dxr = dx;
			if (symmetricScale) {
				dxl = -dx;
			}
		} else if (scaleEdge == Edge.BOTTOM) {
			dyb = dy;
			if (symmetricScale) {
				dyt = -dy;
			}
		} else if (scaleEdge == Edge.LEFT) {
			dxl = dx;
			if (symmetricScale) {
				dxr = -dx;
			}
		}
		if (!safeMovement || boundUpdateSafe(scaleComponent, dxl, dxr, dyt, dyb)) {
			scaleComponent.scale(dxl, dxr, dyt, dyb);
		}
	}

	private void doScaleOnViewportComponent(boolean symmetricScale, boolean squareScale, double vdx, double vdy) {
		ViewportComponent viewportComponent = (ViewportComponent) scaleComponent; //should check if scale component is viewport component prior to this method call

		double dxl = 0; //change in x percent left
		double dxr = 0; //change in x percent right
		double dyt = 0; //change in y percent top
		double dyb = 0; //change in y percent bottom

		if (squareScale) {//scale only as a square (all changes are equal)
			//set them equal to the biggest value
			if (Math.abs(vdx) > Math.abs(vdy)) {
				vdy = vdx;
				if (scaleEdge == Edge.TOP_RIGHT || scaleEdge == Edge.BOTTOM_LEFT) {
					vdy = -vdy;
				}
			} else {
				vdx = vdy;
				if (scaleEdge == Edge.TOP_RIGHT || scaleEdge == Edge.BOTTOM_LEFT) {
					vdx = -vdx;
				}
			}
		}
		if (scaleEdge == Edge.TOP_LEFT) {
			dyt = vdy;
			dxl = vdx;
			if (symmetricScale) {
				dyb = -vdy;
				dxr = -vdx;
			}
		} else if (scaleEdge == Edge.TOP_RIGHT) {
			dyt = vdy;
			dxr = vdx;
			if (symmetricScale) {
				dyb = -vdy;
				dxl = -vdx;
			}
		} else if (scaleEdge == Edge.BOTTOM_LEFT) {
			dyb = vdy;
			dxl = vdx;
			if (symmetricScale) {
				dyt = -vdy;
				dxr = -vdx;
			}
		} else if (scaleEdge == Edge.BOTTOM_RIGHT) {
			dyb = vdy;
			dxr = vdx;
			if (symmetricScale) {
				dyt = -vdy;
				dxl = -vdx;
			}
		} else if (scaleEdge == Edge.TOP) {
			dyt = vdy;
			if (symmetricScale) {
				dyb = -vdy;
			}
		} else if (scaleEdge == Edge.RIGHT) {
			dxr = vdx;
			if (symmetricScale) {
				dxl = -vdx;
			}
		} else if (scaleEdge == Edge.BOTTOM) {
			dyb = vdy;
			if (symmetricScale) {
				dyt = -vdy;
			}
		} else if (scaleEdge == Edge.LEFT) {
			dxl = vdx;
			if (symmetricScale) {
				dxr = -vdx;
			}
		}
		double px = viewportComponent.getPercentX() + dxl;
		double pw = viewportComponent.getPercentW() + dxr - dxl;
		double py = viewportComponent.getPercentY() + dyt;
		double ph = viewportComponent.getPercentH() + dyb - dyt;

		int screenX = viewportComponent.calcScreenX(px);
		int screenY = viewportComponent.calcScreenY(py);
		int screenW = viewportComponent.calcScreenWidth(pw);
		int screenH = viewportComponent.calcScreenHeight(ph);

		if (!safeMovement || boundSetSafe(scaleComponent, screenX, screenX + screenW, screenY, screenY + screenH)) {
			viewportComponent.setPercentX(px);
			viewportComponent.setPercentY(py);
			viewportComponent.setPercentW(pw);
			viewportComponent.setPercentH(ph);
		}
	}

	private boolean basicMouseMovement(int mousex, int mousey) {
		updateContextMenu();
		mouseOverComponent = null;
		{
			CanvasComponent component;
			for (int i = components.size() - 1; i >= 0; i--) {
				component = components.get(i);
				if (component.isEnabled()) {
					if (component.containsPoint(mousex, mousey)) {
						mouseOverComponent = component;
						break;
					}
				}
			}
		}
		if (scaleComponent == null) {
			changeCursorToDefault();
			if (!selection.isSelecting() && mouseOverComponent != null) {
				changeCursorToMove();
			}
		}
		if (mouseButtonDown == MouseButton.NONE) {
			if (selection.numSelected() > 0) {
				checkForScaling(mousex, mousey);
			}
			return false;
		}
		if (mouseButtonDown == MouseButton.MIDDLE || (mouseButtonDown == MouseButton.SECONDARY && !selection.isSelecting())) {
			return false;
		}
		if (selection.isSelecting()) {
			selection.selectTo(mousex, mousey);
			selection.clearSelected();
			for (CanvasComponent component : components) {
				if (component.isEnabled()) {
					if (selection.contains(component)) {
						selection.addToSelection(component);
					}
				}
			}
			return false;
		}
		return true;
	}

	private void updateContextMenu() {
		ContextMenu cm = getContextMenu();

		if (cm != null && cm.isShowing()) {
			if (mouseOverComponent != contextMenuComponent && cm != canvasContextMenu) {
				cm.hide();
			} else if (cm == canvasContextMenu && mouseOverComponent != null) {
				cm.hide();
			}
		}
	}

	/** Called from mouseMove. Checks to see if the given mouse position is near a component edge. If it is, it will store the component as well as the edge. */
	private void checkForScaling(int mousex, int mousey) {
		Edge edge;
		setReadyForScale(null, Edge.NONE);
		CanvasComponent component;
		for (int i = selection.numSelected() - 1; i >= 0; i--) {
			component = selection.getSelected().get(i);
			if (!component.isEnabled()) {
				continue;
			}
			edge = component.getEdgeForPoint(mousex, mousey, COMPONENT_EDGE_LEEWAY);
			if (edge == Edge.NONE) {
				continue;
			}
			setReadyForScale(component, edge);
			changeCursorToScale(edge);
			return;
		}
	}

	private void setReadyForScale(@Nullable CanvasComponent toScale, @NotNull Edge scaleEdge) {
		this.scaleComponent = toScale;
		this.scaleEdge = scaleEdge;
	}

	private int getSnapPixelsWidth(double percentage) {
		double p = percentage / 100.0;
		int width = snapRelativeToViewport ? resolution.getViewportWidth() : getCanvasWidth();
		return (int) (width * p);
	}

	private int getSnapPixelsHeight(double percentage) {
		double p = percentage / 100.0;
		int height = snapRelativeToViewport ? resolution.getViewportHeight() : getCanvasHeight();
		return (int) (height * p);
	}

	/** Set the context menu that should be shown */
	private void setContextMenu(@Nullable ContextMenu contextMenu, int xpos, int ypos) {
		this.contextMenu = contextMenu;
		contextMenuPosition.set(xpos, ypos);
	}

	/** Get the context menu to be shown */
	private ContextMenu getContextMenu() {
		return contextMenu;
	}

	/**
	 Sets whether or not the grid should be shown. When this method is invoked, the canvas is repainted.
	 */
	public void showGrid(boolean showGrid) {
		this.showGrid = showGrid;
		paint();
	}

	/** Updates the UI colors like selection color, grid color, and bg color */
	public void updateColors() {
		this.gridColor = CanvasViewColors.GRID;
		this.selectionColor = CanvasViewColors.SELECTION;
		this.absRegionComponent.setBackgroundColor(CanvasViewColors.ABS_REGION);
		this.setCanvasBackgroundColor(CanvasViewColors.EDITOR_BG);
	}

	/**
	 Update the Absolute region box. For each parameter: -1 to leave unchanged, 0 for false, 1 for true

	 @param alwaysFront true if the region should always be rendered last, false if it should be rendered first
	 @param showing true the region is showing, false if not
	 */
	public void updateAbsRegion(int alwaysFront, int showing) {
		if (alwaysFront != -1) {
			absRegionComponent.setAlwaysRenderAtFront(alwaysFront == 1);
		}
		if (showing != -1) {
			absRegionComponent.setGhost(!(showing == 1));
		}
		paint();
	}

	/**
	 @author Kayler
	 Created on 05/13/2016.
	 */
	private static class CanvasSelection extends SimpleCanvasComponent implements Selection {
		private ArrayList<CanvasComponent> selected = new ArrayList<>();
		private boolean isSelecting;

		@Override
		@NotNull
		public ArrayList<CanvasComponent> getSelected() {
			return selected;
		}

		@Nullable
		@Override
		public CanvasComponent getFirst() {
			if (selected.size() == 0) {
				return null;
			}
			return selected.get(0);
		}

		@Override
		public void toggleFromSelection(CanvasComponent component) {
			if (isSelected(component)) {
				selected.remove(component);
			} else {
				this.selected.add(component);
			}
		}

		@Override
		public void addToSelection(CanvasComponent component) {
			if (!isSelected(component)) {
				this.selected.add(component);
			}
		}

		@Override
		public boolean isSelected(@Nullable CanvasComponent component) {
			if (component == null) {
				return false;
			}
			for (CanvasComponent c : selected) {
				if (c == component) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean removeFromSelection(CanvasComponent component) {
			return this.selected.remove(component);
		}

		@Override
		public void clearSelected() {
			this.selected.clear();
		}

		@Override
		public int numSelected() {
			return this.selected.size();
		}

		boolean isSelecting() {
			return this.isSelecting;
		}

		void setSelecting(boolean selecting) {
			this.isSelecting = selecting;
		}

		void removeAllAndAdd(@NotNull CanvasComponent toAdd) {
			clearSelected();
			this.selected.add(toAdd);
		}

		CanvasSelection() {
			super(0, 0, 0, 0);
		}

		void beginSelecting(int x, int y) {
			setPosition(x, y, x, y);
			this.isSelecting = true;
		}

		void selectTo(int x, int y) {
			setX2(x);
			setY2(y);
		}
	}


	private static class KeyMap {
		String SCALE_SQUARE = "s";
		String PREVENT_VERTICAL_MOVEMENT = "x";
		String PREVENT_HORIZONTAL_MOVEMENT = "z";
	}
}
