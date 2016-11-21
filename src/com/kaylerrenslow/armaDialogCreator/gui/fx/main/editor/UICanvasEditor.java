/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.util.MathUtil;
import com.kaylerrenslow.armaDialogCreator.util.Point;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.util.Iterator;

/**
 @author Kayler
 @since 05/11/2016. */
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
	private boolean hasDoubleClicked;
	
	/** amount of change that has happened since last snap */
	private int dxAmount, dyAmount = 0;
	
	private KeyMap keyMap = new KeyMap();
	
	/** Component that is ready to be scaled, null if none is ready to be scaled */
	private CanvasControl scaleControl;
	/** Edge that the scaling will be conducted, or Edge.NONE is no scaling is being done */
	private Edge scaleEdge = Edge.NONE;
	/** Component that the mouse is over, or null if not over any component */
	private CanvasControl mouseOverControl;
	/** Component that the component context menu was created on, or null if the component context menu isn't open */
	private CanvasControl contextMenuControl;
	
	private SnapConfiguration calc;
	
	/** Class that generates context menus for the controls */
	private ComponentContextMenuCreator menuCreator;
	/** Context menu to show when user right clicks and no component is selected */
	private ContextMenu canvasContextMenu;
	/** The context menu that wants to be shown */
	private ContextMenu contextMenu;
	private final Point contextMenuPosition = new Point(-1, -1);
	
	/** If true, scaling and translating controls will only work when the actions don't put their bounds outside the canvas. If false, all scaling and translating is allowed. */
	private boolean safeMovement = false;
	
	private final ArmaAbsoluteBoxComponent absRegionComponent;
	
	private boolean waitingForZXRelease = false;
	private long zxPressStartTimeMillis;

	private ValueObserver<CanvasControl> doubleClickObserver = new ValueObserver<>(null);

	public UICanvasEditor(Resolution resolution, SnapConfiguration calculator, @NotNull Display<? extends CanvasControl> display) {
		super(resolution, display);
		
		setSnapConfig(calculator);
		
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

		keys.getKeyStateObserver().addListener(new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean newValue) {
				keyUpdate(newValue);
			}
		});
		absRegionComponent = new ArmaAbsoluteBoxComponent(resolution);
		selection.selected.addListener(new ListChangeListener<CanvasControl>() {
			@Override
			public void onChanged(Change<? extends CanvasControl> c) {
				paint();
			}
		});
	}
	
	/** Get the observer that watches what controls get doubled clicked on. If the passed value is null, nothing was double clicked */
	public ValueObserver<CanvasControl> getDoubleClickObserver() {
		return doubleClickObserver;
	}
	
	public void setSnapConfig(@NotNull SnapConfiguration snapConfig) {
		this.calc = snapConfig;
	}
	
	@NotNull
	public SnapConfiguration getSnapConfig() {
		return this.calc;
	}
	
	@NotNull
	public Selection getSelection() {
		return selection;
	}
	
	/**
	 @param ccm the context menu creator that is used to give controls context menus
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
	
	/** If true, scaling and translating controls will only work when the actions don't put their bounds outside the canvas. If false, all scaling and translating is allowed. */
	public boolean getSafeMovement() {
		return this.safeMovement;
	}

	public CanvasControl getMouseOverControl() {
		return mouseOverControl;
	}
	
	/**
	 Sets whether or not the grid should be shown. When this method is invoked, the canvas is repainted.
	 */
	public void showGrid(boolean showGrid) {
		this.showGrid = showGrid;
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
	}
	
	/** Paint the canvas */
	protected void paint() {
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
	protected void paintControls() {
		if (!absRegionComponent.alwaysRenderAtFront()) {
			paintAbsRegionComponent();
		}
		super.paintControls();
		gc.save();
		for (CanvasControl control : selection.getSelected()) {
			gc.setStroke(control.getRenderer().getBackgroundColor());
			control.getRenderer().drawRectangle(gc);
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
	protected void paintControl(CanvasControl control) {
		if (isSelectingArea() && !control.getRenderer().isEnabled()) {
			return;
		}
		boolean selected = selection.isSelected(control);
		if (selected) {
			gc.save();
			Color selectedBorderColor = selectionColor;
			int centerx = control.getRenderer().getCenterX();
			int centery = control.getRenderer().getCenterY();
			boolean noHoriz = keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT);
			boolean noVert = keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT);
			if (noHoriz) {
				gc.setStroke(selectedBorderColor);
				gc.setLineWidth(4);
				gc.strokeLine(centerx, 0, centerx, getCanvasHeight());
			}
			if (noVert) {
				gc.setStroke(selectedBorderColor);
				gc.setLineWidth(4);
				gc.strokeLine(0, centery, getCanvasWidth(), centery);
			}
			//draw selection 'shadow'
			gc.setGlobalAlpha(0.4d);
			gc.setStroke(selectedBorderColor);
			int offset = 4 + (control.getRenderer().getBorder() != null ? control.getRenderer().getBorder().getThickness() : 0);
			Region.fillRectangle(gc, control.getRenderer().getLeftX() - offset, control.getRenderer().getTopY() - offset, control.getRenderer().getRightX() + offset, control.getRenderer().getBottomY() + offset);
			gc.restore();
		}
		super.paintControl(control);
	}
	
	private boolean isSelectingArea() {
		return selection.isSelecting() && selection.getArea() > 10;
	}
	
	protected void paintAbsRegionComponent() {
		if (!absRegionComponent.isGhost() && !isSelectingArea()) {
			absRegionComponent.paint(gc);
		}
	}
	
	private void drawGrid() {
		if (keys.isShiftDown()) {
			double snap = calc.snapPercentage();
			double alt = calc.alternateSnapPercentage();
			//render such that the alternate is always more dominant
			if (snap > alt) {
				drawGrid(alt, true);
				drawGrid(snap, false);
			} else if (alt > snap) {
				drawGrid(alt, false);
				drawGrid(snap, true);
			} else {
				drawGrid(snap, false);
			}
		} else {
			drawGrid(calc.snapPercentage(), false);
		}
		
	}
	
	private void drawGrid(double snap, boolean light) {
		double spacingX = getSnapPixelsWidthF(snap);
		double spacingY = getSnapPixelsHeightF(snap);
		if (spacingX <= 0 || spacingY <= 0) {
			return;
		}
		int w = getCanvasWidth();
		int h = getCanvasHeight();
		int offsetx = 0;
		int offsety = 0;
		int numX = (int) (w / spacingX);
		int numY = (int) (h / spacingY);
		double ys, xs;
		double antiAlias = 0.5;
		gc.save();
		if (light) {
			gc.setGlobalAlpha(0.2);
		}
		gc.setStroke(gridColor);
		for (int y = 0; y <= numY; y++) {
			ys = Math.floor(y * spacingY);
			gc.strokeLine(0 + antiAlias - offsetx, ys + antiAlias, w - antiAlias + offsetx, ys + antiAlias);
		}
		for (int x = 0; x <= numX; x++) {
			xs = Math.floor(x * spacingX);
			gc.strokeLine(xs + antiAlias, 0 + antiAlias - offsety, xs + antiAlias, h - antiAlias + offsety);
		}
		gc.restore();
	}
	
	/**
	 This is called when the mouse listener is invoked and a mouse press was the event.
	 This method should be the only one dealing with adding and removing controls from the selection, other than mouseMove which adds to the selection via the selection box
	 
	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was pressed
	 */
	@Override
	protected void mousePressed(int mousex, int mousey, @NotNull MouseButton mb) {
		if (getContextMenu() != null) {
			getContextMenu().hide();
		}
		hasDoubleClicked = System.currentTimeMillis() - lastMousePressTime <= DOUBLE_CLICK_WAIT_TIME_MILLIS;
		lastMousePressTime = System.currentTimeMillis();
		selection.setSelecting(false);
		this.mouseButtonDown = mb;
		
		if (scaleControl != null && mb == MouseButton.PRIMARY) { //only select component that is being scaled to prevent multiple scaling
			selection.removeAllAndAdd(scaleControl);
			return;
		}
		if (selection.numSelected() == 0 && mouseOverControl != null) { //nothing is selected, however, mouse is over a component so we need to select that
			selection.addToSelection(mouseOverControl);
			return;
		}
		if (selection.numSelected() == 0 && mouseOverControl == null && mb == MouseButton.SECONDARY) { //nothing is selected and right clicking the canvas
			selection.clearSelected();
			return;
		}
		if (selection.numSelected() > 0 && mb == MouseButton.SECONDARY) { //check to see if right click is over a selected component
			CanvasControl control;
			for (int i = selection.numSelected() - 1; i >= 0; i--) {
				control = selection.getSelected().get(i);
				if (control.getRenderer().containsPoint(mousex, mousey)) {
					selection.removeAllAndAdd(control); //only 1 can be selected
					return;
				}
			}
			Iterator<? extends CanvasControl> controlIterator = display.iteratorForAllControls(true);
			while (controlIterator.hasNext()) {
				control = controlIterator.next();
				if (!control.getRenderer().isEnabled()) {
					continue;
				}
				if (control.getRenderer().containsPoint(mousex, mousey)) {
					selection.removeAllAndAdd(control);
					return;
				}
			}
			selection.clearSelected();
			return;
		}
		if (mouseOverControl != null) {
			if (keys.isCtrlDown()) {
				selection.toggleFromSelection(mouseOverControl);
				return;
			} else {
				if (selection.numSelected() > 0) {
					if (selection.isSelected(mouseOverControl)) {
						if (hasDoubleClicked && selection.numSelected() > 1) {
							selection.removeAllAndAdd(mouseOverControl);
							hasDoubleClicked = false;//don't open configure control properties
						}
						return;
					}
					if (!keys.spaceDown()) { //if space is down, mouse over component should be selected
						CanvasControl control;
						for (int i = selection.numSelected() - 1; i >= 0; i--) {
							control = selection.getSelected().get(i);
							if (control.getRenderer().containsPoint(mousex, mousey)) { //allow this one to stay selected despite the mouse not being over it
								return;
							}
						}
					}
				}
				selection.removeAllAndAdd(mouseOverControl);
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
		contextMenuControl = null;
		if (mb == MouseButton.SECONDARY) {
			if (menuCreator != null && selection.getFirst() != null) {
				contextMenuControl = selection.getFirst();
				setContextMenu(menuCreator.initialize(contextMenuControl.getRenderer()), mousex, mousey);
			} else if (canvasContextMenu != null) {
				setContextMenu(canvasContextMenu, mousex, mousey);
			}
		} else {
			if (hasDoubleClicked) {
				doubleClickObserver.updateValue(selection.getFirst());
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
			return;//not dragging mouse
		}
		hasDoubleClicked = false; //force no double click so that when dragging after a double click, nothing happens
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
		
		int xSnapCount; //how many snaps occurred for x
		int ySnapCount; //how many snaps occurred for y
		double snapPercentage = keys.isShiftDown() ? calc.alternateSnapPercentage() : calc.snapPercentage();
		if (!keys.isAltDown()) {
			int snapX = getSnapPixelsWidth(snapPercentage);
			int snapY = getSnapPixelsHeight(snapPercentage);
			
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
		if (scaleControl != null) { //scaling
			boolean squareScale = keys.keyIsDown(keyMap.SCALE_SQUARE);
			boolean symmetricScale = keys.isCtrlDown() || squareScale;
			doScaleOnComponent(symmetricScale, squareScale, dx1, dy1);
			return;
		}
		//not scaling and simply translating (moving)

		for (CanvasControl control : selection.getSelected()) {
			//only move-able controls should be inside selection
			if (!safeMovement || boundUpdateSafe(control.getRenderer(), dx, dx, dy, dy)) { //translate if safeMovement is off or safeMovement is on and the translation doesn't move component out of bounds
				control.getRenderer().translate(dx1, dy1);
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
		if (!safeMovement || boundUpdateSafe(scaleControl.getRenderer(), dxl, dxr, dyt, dyb)) {
			if (!scaleIsNegative(scaleControl.getRenderer(), dxl, dxr, dyt, dyb)) {
				scaleControl.getRenderer().scale(dxl, dxr, dyt, dyb);
			}
		}
	}
	
	private boolean basicMouseMovement(int mousex, int mousey) {
		
		updateContextMenu();
		mouseOverControl = null;

		CanvasControl control;
		Iterator<? extends CanvasControl> iteratorControl = display.iteratorForAllControls(true);
		while (iteratorControl.hasNext()) {
			control = iteratorControl.next();
			if (control.getRenderer().isEnabled()) {
				if (control.getRenderer().containsPoint(mousex, mousey)) {
					mouseOverControl = control;
					break;
				}
			}
		}
		
		if (scaleControl == null) {
			if (!selection.isSelecting() && mouseOverControl != null) {
				changeCursorToMove();
			} else {
				changeCursorToDefault();
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
			iteratorControl = display.iteratorForAllControls(false);
			while (iteratorControl.hasNext()) {
				control = iteratorControl.next();
				if (control.getRenderer().isEnabled()) {
					if (selection.contains(control.getRenderer())) {
						selection.addToSelection(control);
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
			if (mouseOverControl != contextMenuControl && cm != canvasContextMenu) {
				cm.hide();
			} else if (cm == canvasContextMenu && mouseOverControl != null) {
				cm.hide();
			}
		}
	}
	
	/** Called from mouseMove. Checks to see if the given mouse position is near a component edge. If it is, it will store the component as well as the edge. */
	private void checkForScaling(int mousex, int mousey) {
		Edge edge;
		setReadyForScale(null, Edge.NONE);
		CanvasControl component;
		for (int i = selection.numSelected() - 1; i >= 0; i--) {
			component = selection.getSelected().get(i);
			if (!component.getRenderer().isEnabled()) {
				continue;
			}
			edge = component.getRenderer().getEdgeForPoint(mousex, mousey, COMPONENT_EDGE_LEEWAY);
			if (edge == Edge.NONE) {
				continue;
			}
			setReadyForScale(component, edge);
			changeCursorToScale(edge);
			return;
		}
	}

	private void setReadyForScale(@Nullable CanvasControl toScale, @NotNull Edge scaleEdge) {
		this.scaleControl = toScale;
		this.scaleEdge = scaleEdge;
	}
	
	private double getSnapPixelsWidthF(double percentageDecimal) {
		int width = getCanvasWidth();
		return (width * percentageDecimal);
	}
	
	private double getSnapPixelsHeightF(double percentageDecimal) {
		int height = getCanvasHeight();
		return (height * percentageDecimal);
	}
	
	private int getSnapPixelsWidth(double percentageDecimal) {
		return (int) getSnapPixelsWidthF(percentageDecimal);
	}
	
	private int getSnapPixelsHeight(double percentageDecimal) {
		return (int) getSnapPixelsHeightF(percentageDecimal);
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
	
	/**
	 Check if scaling the given region will give it negative area (right most side is behind left side)
	 
	 @param r region to check bounds of
	 @param dxl change in x on the left side
	 @param dxr change in x on the right side
	 @param dyt change in y on the top side
	 @param dyb change in y on the bottom side
	 @return true if the scale results in a bad scale, false if scale is okay
	 */
	private boolean scaleIsNegative(Region r, int dxl, int dxr, int dyt, int dyb) {
		int xl = r.getLeftX() + dxl;
		int xr = r.getRightX() + dxr;
		int yt = r.getTopY() + dyt;
		int yb = r.getBottomY() + dyb;
		return xr < xl || yt > yb;
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
	
	
	private void keyUpdate(boolean keyIsDown) {
		if (keyIsDown) {
			if (keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT) && keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT)) {
				zxPressStartTimeMillis = System.currentTimeMillis();
				waitingForZXRelease = true;
			}
		} else {
			if (waitingForZXRelease && !keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT) && !keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT)) {
				if (zxPressStartTimeMillis + 500 <= System.currentTimeMillis()) {
					for (CanvasControl control : selection.getSelected()) {
						control.getRenderer().setEnabled(false);
					}
					selection.clearSelected();
					mouseOverControl = scaleControl = null;
					changeCursorToDefault();
					waitingForZXRelease = false;
				}
			}
		}
	}
	
	
	/**
	 @author Kayler
	 Created on 05/13/2016.
	 */
	private static class CanvasSelection extends SimpleCanvasComponent implements Selection {
		private ObservableList<CanvasControl> selected = FXCollections.observableArrayList(new ArrayList<>());
		private boolean isSelecting;
		
		@Override
		public @NotNull ObservableList<CanvasControl> getSelected() {
			return selected;
		}
		
		@Nullable
		@Override
		public CanvasControl getFirst() {
			if (selected.size() == 0) {
				return null;
			}
			return selected.get(0);
		}
		
		@Override
		public void toggleFromSelection(CanvasControl control) {
			if (isSelected(control)) {
				selected.remove(control);
			} else {
				this.selected.add(control);
			}
		}
		
		@Override
		public void addToSelection(CanvasControl control) {
			if (!isSelected(control)) {
				this.selected.add(control);
			}
		}
		
		@Override
		public boolean isSelected(@Nullable CanvasControl control) {
			if (control == null) {
				return false;
			}
			for (CanvasControl c : selected) {
				if (c == control) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean removeFromSelection(CanvasControl control) {
			return this.selected.remove(control);
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

		void removeAllAndAdd(@NotNull CanvasControl toAdd) {
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
