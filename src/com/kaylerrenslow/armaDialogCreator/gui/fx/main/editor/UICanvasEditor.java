package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.util.MathUtil;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Component;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Edge;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.Region;
import com.kaylerrenslow.armaDialogCreator.util.Point;
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

	private Selection selection = new Selection();

	/** Mouse button that is currently down */
	private MouseButton mouseButtonDown = MouseButton.NONE;
	private long lastMousePressTime;

	private int dxAmount, dyAmount = 0; //amount of change that has happened since last snap

	private KeyMap keyMap = new KeyMap();

	/** Component that is ready to be scaled, null if none is ready to be scaled */
	private Component scaleComponent;
	/** Edge that the scaling will be conducted, or Edge.NONE is no scaling is being done */
	private Edge scaleEdge = Edge.NONE;
	/** Component that the mouse is over, or null if not over any component */
	private Component mouseOverComponent;
	/** Component that the component context menu was created on, or null if the component context menu isn't open */
	private Component contextMenuComponent;

	private ISnapConfiguration calc;

	/** Class that generates context menus for the components */
	private IComponentContextMenuCreator menuCreator;
	/** Context menu to show when user right clicks and no component is selected */
	private ContextMenu canvasContextMenu;
	/** The context menu that wants to be shown */
	private ContextMenu contextMenu;
	private final Point contextMenuPosition = new Point(-1, -1);

	/** If true, scaling and translating components will only work when the actions don't put their bounds outside the canvas. If false, all scaling and translating is allowed. */
	private boolean safeMovement = false;

	private final Resolution resolution;
	private final ArmaAbsoluteBoxComponent absRegionComponent;

	public UICanvasEditor(Resolution resolution, ISnapConfiguration calculator) {
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

		absRegionComponent = new ArmaAbsoluteBoxComponent(resolution);
		addComponent(absRegionComponent);
	}

	/**
	 Removes the given component from the canvas render and user interaction.

	 @param component component to remove
	 @return true if the component was removed, false if nothing was removed
	 */
	public boolean removeComponent(@NotNull Component component) {
		boolean removed = super.removeComponent(component);
		if (removed) {
			this.selection.removeFromSelection(component);
		}
		return removed;
	}

	public void setPositionCalculator(@NotNull ISnapConfiguration positionCalculator) {
		this.calc = positionCalculator;
	}

	@NotNull
	public ISnapConfiguration getSnapConfig() {
		return this.calc;
	}

	@NotNull
	public ISelection getSelection() {
		return selection;
	}

	/**
	 @param ccm the context menu creator that is used to give Components context menus
	 */
	public void setMenuCreator(@Nullable IComponentContextMenuCreator ccm) {
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
	public void updateResolution(Resolution r) {
		this.resolution.setTo(r);
		absRegionComponent.updateToNewResolution(r);
		paint();
	}


	/** Paint the canvas */
	public void paint() {
		super.paint();
		gc.save();
		for (Component component : selection.getSelected()) {
			gc.setStroke(component.getBackgroundColor());
			component.drawRectangle(gc);
		}
		gc.restore();

		if (selection.isSelecting()) {
			gc.save();
			gc.setStroke(selectionColor);
			gc.setLineWidth(2);
			selection.drawRectangle(gc);
			gc.restore();
		}
	}

	@Override
	protected void paintBackground() {
		super.paintBackground();
		if (showGrid) {
			drawGrid();
		}
	}

	@Override
	protected void paintComponent(Component component) {
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
	 Translates the given region by dx and dy only if the given dx and dy won't put the region out of bounds with the canvas. If either put the region out of bounds, the translate won't occur

	 @param r the region to translate
	 @param dx change in x
	 @param dy change in y
	 */
	private void safeTranslate(Region r, int dx, int dy) {
		if (boundUpdateSafe(r, dx, dx, dy, dy)) {
			r.translate(dx, dy);
		}
	}

	/**
	 Check if the bound updateToNewResolution will keep the boundaries inside the canvas

	 @param r region to check bounds of
	 @param dxLeft change in x on the left side
	 @param dxRight change in x on the right side
	 @param dyTop change in y on the top side
	 @param dyBottom change in y on the bottom side
	 @return true if the bounds can be updated, false otherwise
	 */
	private boolean boundUpdateSafe(Region r, int dxLeft, int dxRight, int dyTop, int dyBottom) {
		boolean outX = MathUtil.outOfBounds(r.getLeftX() + dxLeft, 0, getCanvasWidth() - r.getWidth()) || MathUtil.outOfBounds(r.getRightX() + dxRight, 0, getCanvasWidth());
		if (!outX) {
			boolean outY = MathUtil.outOfBounds(r.getTopY() + dyTop, 0, getCanvasHeight() - r.getHeight()) || MathUtil.outOfBounds(r.getBottomY() + dyBottom, 0, getCanvasHeight());
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
			Component component;
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
						Component component;
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
		boolean continueIt = basicMouseMovement(mousex, mousey);
		if (!continueIt) {
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
		int ddx = dx < 0 ? -1 : 1; //change in direction for x
		int ddy = dy < 0 ? -1 : 1; //change in direction for y
		int snapX = getSnapPixelsWidth(keys.isShiftDown() ? calc.alternateSnapPercentage() : calc.snapPercentage());
		int snapY = getSnapPixelsHeight(keys.isShiftDown() ? calc.alternateSnapPercentage() : calc.snapPercentage());

		dxAmount += dx;
		dyAmount += dy;
		int dxAmountAbs = Math.abs(dxAmount);
		int dyAmountAbs = Math.abs(dyAmount);
		if (dxAmountAbs >= snapX) {
			dx1 = snapX * ddx * (dxAmountAbs / snapX);
			dxAmount = (dxAmountAbs - Math.abs(dx1)) * ddx;
		}
		if (dyAmountAbs >= snapY) {
			dy1 = snapY * ddy * (dyAmountAbs / snapY);
			dyAmount = (dyAmountAbs - Math.abs(dy1)) * ddy;
		}

		if (scaleComponent != null) { //scaling
			doScaleOnComponent(dx1, dy1, snapX, snapY);
			return;
		}
		//not scaling and simply translating (moving)
		int moveX, moveY, nearestGridX, nearestGridY;
		for (Component component : selection.getSelected()) {
			//only moveable components should be inside selection
			if (keys.isAltDown()) {
				moveX = component.getLeftX();
				moveY = component.getTopY();
				nearestGridX = moveX - moveX % snapX;
				nearestGridY = moveY - moveY % snapY;
				System.out.println("nearest grid:" + nearestGridX + "," + nearestGridY);
				dx1 = nearestGridX - moveX;
				dy1 = nearestGridY - moveY;
			}
			if (!safeMovement) {
				component.translate(dx1, dy1);
			} else {
				safeTranslate(component, dx1, dy1);
			}
		}
	}

	private void doScaleOnComponent(int dx1, int dy1, int snapX, int snapY) {
		int dxl = 0; //change in x left
		int dxr = 0; //change in x right
		int dyt = 0; //change in y top
		int dyb = 0; //change in y bottom
		if (scaleEdge == Edge.TOP_LEFT) {
			dyt = dy1;
			dxl = dx1;
			if (keys.isCtrlDown()) {
				dyb = -dy1;
				dxr = -dx1;
			}
		} else if (scaleEdge == Edge.TOP_RIGHT) {
			dyt = dy1;
			dxr = dx1;
			if (keys.isCtrlDown()) {
				dyb = -dy1;
				dxl = -dx1;
			}
		} else if (scaleEdge == Edge.BOTTOM_LEFT) {
			dyb = dy1;
			dxl = dx1;
			if (keys.isCtrlDown()) {
				dyt = -dy1;
				dxr = -dx1;
			}
		} else if (scaleEdge == Edge.BOTTOM_RIGHT) {
			dyb = dy1;
			dxr = dx1;
			if (keys.isCtrlDown()) {
				dyt = -dy1;
				dxl = -dx1;
			}
		} else if (scaleEdge == Edge.TOP) {
			dyt = dy1;
			if (keys.isCtrlDown()) {
				dyb = -dy1;
			}
		} else if (scaleEdge == Edge.RIGHT) {
			dxr = dx1;
			if (keys.isCtrlDown()) {
				dxl = -dx1;
			}
		} else if (scaleEdge == Edge.BOTTOM) {
			dyb = dy1;
			if (keys.isCtrlDown()) {
				dyt = -dy1;
			}
		} else if (scaleEdge == Edge.LEFT) {
			dxl = dx1;
			if (keys.isCtrlDown()) {
				dxr = -dx1;
			}
		}
		if (keys.isAltDown()) { //scale only to the nearest grid size
			int leftX = scaleComponent.getLeftX();
			int rightX = scaleComponent.getRightX();
			int topY = scaleComponent.getTopY();
			int botY = scaleComponent.getBottomY();
			if (dxl != 0) {
				int p = leftX + dxl;
				int nearestGridLeftX = p - p % snapX;
				dxl = nearestGridLeftX - p;
			}
			if (dxr != 0) {
				int p = rightX + dxr;
				int nearestGridRightX = p - p % snapX;
				dxr = nearestGridRightX - p;
			}
			if (dyt != 0) {
				int p = topY + dyt;
				int nearestGridTopY = p - p % snapY;
				dyt = nearestGridTopY - p;
			}
			if (dyb != 0) {
				int p = botY + dyb;
				int nearestGridBotY = p - p % snapY;
				dyb = nearestGridBotY - p;
			}
		}
		if (!safeMovement || boundUpdateSafe(scaleComponent, dxl, dxr, dyt, dyb)) {
			scaleComponent.scale(dxl, dxr, dyt, dyb);
		}
	}

	private boolean basicMouseMovement(int mousex, int mousey) {
		updateContextMenu();
		mouseOverComponent = null;
		{
			Component component;
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
			for (Component component : components) {
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
		Component component;
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

	private void setReadyForScale(@Nullable Component toScale, @NotNull Edge scaleEdge) {
		this.scaleComponent = toScale;
		this.scaleEdge = scaleEdge;
	}

	private int getSnapPixelsWidth(double percentage) {
		double p = percentage / 100.0;
		return (int) (getCanvasWidth() * p);
	}

	private int getSnapPixelsHeight(double percentage) {
		double p = percentage / 100.0;
		return (int) (getCanvasHeight() * p);
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
	private static class Selection extends Region implements ISelection {
		private ArrayList<Component> selected = new ArrayList<>();
		private boolean isSelecting;

		@Override
		@NotNull
		public ArrayList<Component> getSelected() {
			return selected;
		}

		@Nullable
		@Override
		public Component getFirst() {
			if (selected.size() == 0) {
				return null;
			}
			return selected.get(0);
		}

		@Override
		public void toggleFromSelection(Component component) {
			if (isSelected(component)) {
				selected.remove(component);
			} else {
				this.selected.add(component);
			}
		}

		@Override
		public void addToSelection(Component component) {
			if (!isSelected(component)) {
				this.selected.add(component);
			}
		}

		@Override
		public boolean isSelected(@Nullable Component component) {
			if (component == null) {
				return false;
			}
			for (Component c : selected) {
				if (c == component) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean removeFromSelection(Component component) {
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

		void removeAllAndAdd(@NotNull Component toAdd) {
			clearSelected();
			this.selected.add(toAdd);
		}

		Selection() {
			super(0, 0, 0, 0);
		}

		void beginSelecting(int x, int y) {
			this.x1 = x;
			this.y1 = y;
			this.x2 = x;
			this.y2 = y;
			this.isSelecting = true;
		}

		void selectTo(int x, int y) {
			this.x2 = x;
			this.y2 = y;
		}
	}


	private static class KeyMap {
		String PREVENT_VERTICAL_MOVEMENT = "x";
		String PREVENT_HORIZONTAL_MOVEMENT = "z";
	}
}
