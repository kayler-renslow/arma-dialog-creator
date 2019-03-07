package com.armadialogcreator.canvas;

import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 @author Kayler
 @since 05/11/2016. */
public class UICanvasEditor extends UICanvas {

	/*** How many pixels the cursor can be off on a component's edge when choosing an edge for scaling */
	private static final int COMPONENT_EDGE_LEEWAY = 5;
	private static final long DOUBLE_CLICK_WAIT_TIME_MILLIS = 300;

	private final UICanvasEditorColors colors = new UICanvasEditorColors();


	private CanvasSelection selection = new CanvasSelection();

	/** Mouse button that is currently down */
	private MouseButton mouseButtonDown = MouseButton.NONE;
	private long lastMousePressTime;
	private boolean hasDoubleClickedCtrl;

	/** amount of change that has happened since last snap */
	private int dxAmount, dyAmount = 0;

	private KeyMap keyMap = new KeyMap();

	/** Component that is ready to be scaled, null if none is ready to be scaled */
	private UINode scaleNode;
	/** Edge that the scaling will be conducted, or Edge.None is no scaling is being done */
	private Edge scaleEdge = Edge.None;
	/** Component that the mouse is over, or null if not over any component */
	private UINode mouseOverNode;
	/** Component that the component context menu was created on, or null if the component context menu isn't open */
	private UINode contextMenuControl;

	private UICanvasConfiguration calc;

	/** Class that generates context menus for the nodes */
	private ComponentContextMenuCreator menuCreator;
	/** Context menu to show when user right clicks and no component is selected */
	private ContextMenu canvasContextMenu;
	/** The context menu that wants to be shown */
	private ContextMenu contextMenu;
	private final Point contextMenuPosition = new Point(-1, -1);

	private final ArmaAbsoluteBoxComponent absRegionComponent;

	private boolean waitingForZXRelease = false;
	private long zxPressStartTimeMillis;

	private final UpdateListenerGroup<UINode> doubleClickUpdateGroup = new UpdateListenerGroup<>();
	private Effect selectionEffect;

	public UICanvasEditor(@NotNull Resolution resolution, @NotNull UICanvasConfiguration configuration, @NotNull UINode rootNode) {
		super(resolution, rootNode);

		setConfig(configuration);

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

		absRegionComponent = new ArmaAbsoluteBoxComponent(colors.absRegion, resolution);
		selection.selected.addListener(new ListChangeListener<UINode>() {
			@Override
			public void onChanged(Change<? extends UINode> c) {
				requestPaint();
			}
		});

		getTimer().getRunnables().add(new Runnable() {
			@Override
			public void run() {
				prepaint();
			}
		});

		initializeSelectionEffect();
	}

	private void initializeSelectionEffect() {
		selectionEffect = new DropShadow(10, backgroundColor.invert());
	}

	/**
	 @return an update group that watches what nodes get doubled clicked on.
	 If the passed value is null, nothing was double clicked
	 */
	@NotNull
	public UpdateListenerGroup<UINode> getDoubleClickUpdateGroup() {
		return doubleClickUpdateGroup;
	}

	public void setConfig(@NotNull UICanvasConfiguration snapConfig) {
		this.calc = snapConfig;
	}

	@NotNull
	public UICanvasConfiguration getConfig() {
		return this.calc;
	}

	@NotNull
	public Selection getSelection() {
		return selection;
	}

	/**
	 @param ccm the context menu creator that is used to give nodes context menus
	 */
	public void setComponentMenuCreator(@Nullable ComponentContextMenuCreator ccm) {
		this.menuCreator = ccm;
	}

	public void setCanvasContextMenu(@Nullable ContextMenu contextMenu) {
		this.canvasContextMenu = contextMenu;
	}

	@Nullable
	public UINode getMouseOverNode() {
		return mouseOverNode;
	}


	/** Updates the UI colors like selection color, grid color, and bg color */
	public void updateColors() {
		this.absRegionComponent.setBackgroundColor(colors.absRegion);
		this.setCanvasBackgroundColor(colors.editorBg);

		initializeSelectionEffect();
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
		requestPaint();
	}

	private void prepaint() {
		if (scaleNode != null) {
			if (scaleNode.getRootNode() == null) {
				scaleNode = null;
			}
		}
		if (mouseOverNode != null) {
			if (mouseOverNode.getRootNode() == null) {
				mouseOverNode = null;
			}
		}
		selection.getSelected().removeIf(next -> next.getRootNode() != this.getRootNode());
	}

	/** Paint the canvas */
	protected void paint() {
		super.paint();
		if (selection.isSelecting()) {
			gc.save();
			gc.setStroke(colors.selection);
			gc.setLineWidth(2);
			graphics.strokeRectangle(selection);
			gc.restore();
		}
		if (absRegionComponent.alwaysRenderAtFront()) {
			paintAbsRegionComponent();
		}
	}

	@Override
	protected void paintRootNode() {
		if (!absRegionComponent.alwaysRenderAtFront()) {
			paintAbsRegionComponent();
		}
		super.paintRootNode();
		gc.save();
		graphics.save();
		Iterator<UINode> iter = selection.getSelected().iterator();
		while (iter.hasNext()) {
			UINode node = iter.next();
			if (node.getComponent() == null) {
				continue;
			}
			if (node.getComponent().isGhost()) {
				iter.remove();
				continue;
			}
			graphics.setStroke(node.getComponent().getBackgroundColorARGB());
			graphics.strokeRectangle(node.getComponent());
		}
		gc.restore();
		graphics.restore();
	}

	@Override
	protected void paintBackground() {
		super.paintBackground();
		if (getConfig().showGrid()) {
			drawGrid();
		}
	}

	@Override
	protected void paintNode(@NotNull UINode node) {
		if (node.getComponent() == null) {
			return;
		}
		if (isSelectingArea() && !node.getComponent().isEnabled()) {
			return;
		}
		boolean selected = selection.isSelected(node);
		if (selected) {
			gc.save();
			Color selectedBorderColor = colors.selection;
			int centerx = node.getComponent().getCenterX();
			int centery = node.getComponent().getCenterY();
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
			gc.setLineDashes(1, 1);
			gc.setLineDashOffset(5);
			int offset = 4 + (node.getComponent().getBorder() != null ? node.getComponent().getBorder().getThickness() : 0);
			int leftX = node.getComponent().getLeftX();
			int width = node.getComponent().getWidth();
			int topY = node.getComponent().getTopY();
			int height = node.getComponent().getHeight();
			gc.setEffect(selectionEffect);
			gc.setFill(colors.selection);
			gc.fillRect(leftX - offset, topY - offset, width + offset + offset, height + offset + offset);
			gc.restore();
		}
		super.paintNode(node);
	}

	private boolean isSelectingArea() {
		return selection.isSelecting() && selection.getArea() > 10;
	}

	protected void paintAbsRegionComponent() {
		if (!absRegionComponent.isGhost() && !isSelectingArea()) {
			absRegionComponent.paint(graphics);
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
		gc.setStroke(colors.grid);
		if (getConfig().viewportSnapEnabled()) {
			offsetx = (int) (resolution.getViewportX() % spacingX);
			offsety = (int) (resolution.getViewportY() % spacingY);
			gc.translate(offsetx, offsety);
		}
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
	 This method should be the only one dealing with adding and removing nodes from the selection, other than mouseMove which adds to the selection via the selection box

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 @param mb mouse button that was pressed
	 */
	@Override
	protected void mousePressed(int mousex, int mousey, @NotNull MouseButton mb) {
		if (getContextMenu() != null) {
			getContextMenu().hide();
		}
		hasDoubleClickedCtrl = System.currentTimeMillis() - lastMousePressTime <= DOUBLE_CLICK_WAIT_TIME_MILLIS
				&& selection.numSelected() > 0
		;
		lastMousePressTime = System.currentTimeMillis();
		selection.setSelecting(false);
		this.mouseButtonDown = mb;

		if (scaleNode != null && mb == MouseButton.PRIMARY) { //only select component that is being scaled to prevent multiple scaling
			selection.removeAllAndAdd(scaleNode);
			return;
		}
		if (selection.numSelected() == 0 && mouseOverNode != null) { //nothing is selected, however, mouse is over a component so we need to select that
			selection.addToSelection(mouseOverNode);
			return;
		}
		if (selection.numSelected() == 0 && mouseOverNode == null && mb == MouseButton.SECONDARY) { //nothing is selected and right clicking the canvas
			selection.clearSelected();
			return;
		}
		if (selection.numSelected() > 0 && mb == MouseButton.SECONDARY) { //check to see if right click is over a selected component
			{
				UINode node;
				for (int i = 0; i < selection.numSelected(); i++) {
					node = selection.getSelected().get(i);
					if (node.getComponent() == null) {
						continue;
					}
					if (node.getComponent().containsPoint(mousex, mousey)) {
						selection.removeAllAndAdd(node); //only 1 can be selected
						return;
					}
				}
			}
			for (UINode node : rootNode.deepIterateChildren()) {
				if (node.getComponent() == null) {
					continue;
				}
				if (!node.getComponent().isEnabled()) {
					continue;
				}
				if (node.getComponent().containsPoint(mousex, mousey)) {
					selection.removeAllAndAdd(node);
					continue; //continue to make sure we right click on front most component
				}
			}
			selection.clearSelected();
			return;
		}
		if (mouseOverNode != null) {
			if (keys.isCtrlDown()) {
				selection.toggleFromSelection(mouseOverNode);
				return;
			} else {
				if (selection.numSelected() > 0) {
					if (selection.isSelected(mouseOverNode)) {
						if (hasDoubleClickedCtrl && selection.numSelected() > 1) {
							selection.removeAllAndAdd(mouseOverNode);
							hasDoubleClickedCtrl = false;//don't open configure node properties
						}
						return;
					}
					if (!keys.spaceDown()) { //if space is down, mouse over component should be selected
						UINode node;
						for (int i = 0; i < selection.numSelected(); i++) {
							node = selection.getSelected().get(i);
							if (node.getComponent() == null) {
								continue;
							}
							if (node.getComponent().containsPoint(mousex, mousey)) { //allow this one to stay selected despite the mouse not being over it
								return;
							}
						}
					}
				}
				selection.removeAllAndAdd(mouseOverNode);
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
				setContextMenu(menuCreator.initialize(contextMenuControl.getComponent()), mousex, mousey);
			} else if (canvasContextMenu != null) {
				setContextMenu(canvasContextMenu, mousex, mousey);
			}
		} else {
			if (hasDoubleClickedCtrl) {
				doubleClickUpdateGroup.update(selection.getFirst());
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
		hasDoubleClickedCtrl = false; //force no double click so that when dragging after a double click, nothing happens
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
		boolean canSnapViewport = !keys.isAltDown() && getConfig().viewportSnapEnabled();
		double vdx = snapPercentage * xSnapCount * dirx;
		double vdy = snapPercentage * ySnapCount * diry;
		if (scaleNode != null) { //scaling
			boolean squareScale = keys.keyIsDown(keyMap.SCALE_SQUARE);
			boolean symmetricScale = keys.isCtrlDown() || squareScale;
			if (canSnapViewport && scaleNode.getComponent() instanceof ViewportCanvasComponent) {
				doScaleOnViewportComponent(symmetricScale, squareScale, vdx, vdy);
			} else {
				doScaleOnComponent(symmetricScale, squareScale, dx1, dy1);
			}
			return;
		}
		//not scaling and simply translating (moving)

		for (UINode node : selection.getSelected()) {
			//only move-able nodes should be inside selection
			if (canSnapViewport && node.getComponent() instanceof ViewportCanvasComponent) {
				ViewportCanvasComponent viewportComponent = ((ViewportCanvasComponent) node.getComponent());
				double px = viewportComponent.getPercentX() + vdx;
				double py = viewportComponent.getPercentY() + vdy;
				double pw = viewportComponent.getPercentW();
				double ph = viewportComponent.getPercentH();
				if (getConfig().isSafeMovement()) {
					int vx = viewportComponent.calcScreenX(px);
					int vy = viewportComponent.calcScreenY(py);
					if (!boundSetSafe(node.getComponent(), vx, vx + viewportComponent.calcScreenWidth(pw), vy, vy + viewportComponent.calcScreenHeight(ph))) {
						continue;
					}
				}

				viewportComponent.setPositionPercent(px, py, pw, ph);
			} else if (!getConfig().isSafeMovement() || boundUpdateSafe(node.getComponent(), dx, dx, dy, dy)) { //translate if safeMovement is off or safeMovement is on and the translation doesn't
				// move component out of bounds
				if (node.getComponent() == null) {
					//shouldn't be able to scale a node which has no component
					throw new IllegalStateException();
				}
				node.getComponent().translate(dx1, dy1);
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
				if (scaleEdge == Edge.TopRight || scaleEdge == Edge.BottomLeft) {
					dy = -dy;
				}
			} else {
				dx = dy;
				if (scaleEdge == Edge.TopRight || scaleEdge == Edge.BottomLeft) {
					dx = -dx;
				}
			}
		}
		if (scaleEdge == Edge.TopLeft) {
			dyt = dy;
			dxl = dx;
			if (symmetricScale) {
				dyb = -dy;
				dxr = -dx;
			}
		} else if (scaleEdge == Edge.TopRight) {
			dyt = dy;
			dxr = dx;
			if (symmetricScale) {
				dyb = -dy;
				dxl = -dx;
			}
		} else if (scaleEdge == Edge.BottomLeft) {
			dyb = dy;
			dxl = dx;
			if (symmetricScale) {
				dyt = -dy;
				dxr = -dx;
			}
		} else if (scaleEdge == Edge.BottomRight) {
			dyb = dy;
			dxr = dx;
			if (symmetricScale) {
				dyt = -dy;
				dxl = -dx;
			}
		} else if (scaleEdge == Edge.Top) {
			dyt = dy;
			if (symmetricScale) {
				dyb = -dy;
			}
		} else if (scaleEdge == Edge.Right) {
			dxr = dx;
			if (symmetricScale) {
				dxl = -dx;
			}
		} else if (scaleEdge == Edge.Bottom) {
			dyb = dy;
			if (symmetricScale) {
				dyt = -dy;
			}
		} else if (scaleEdge == Edge.Left) {
			dxl = dx;
			if (symmetricScale) {
				dxr = -dx;
			}
		}
		if (!getConfig().isSafeMovement() || boundUpdateSafe(scaleNode.getComponent(), dxl, dxr, dyt, dyb)) {
			if (scaleNode.getComponent() == null) {
				//shouldn't be able to scale a node which has no component
				throw new IllegalStateException();
			}
			if (!scaleIsNegative(scaleNode.getComponent(), dxl, dxr, dyt, dyb)) {
				scaleNode.getComponent().scale(dxl, dxr, dyt, dyb);
			}
		}
	}

	private void doScaleOnViewportComponent(boolean symmetricScale, boolean squareScale, double vdx, double vdy) {
		ViewportCanvasComponent viewportComponent = (ViewportCanvasComponent) scaleNode.getComponent();

		if (viewportComponent == null) {
			//shouldn't be able to scale a node that has no component
			throw new IllegalStateException();
		}

		double dxl = 0; //change in x percent left
		double dxr = 0; //change in x percent right
		double dyt = 0; //change in y percent top
		double dyb = 0; //change in y percent bottom

		if (squareScale) {//scale only as a square (all changes are equal)
			//set them equal to the biggest value
			if (Math.abs(vdx) > Math.abs(vdy)) {
				vdy = vdx;
				if (scaleEdge == Edge.TopRight || scaleEdge == Edge.BottomLeft) {
					vdy = -vdy;
				}
			} else {
				vdx = vdy;
				if (scaleEdge == Edge.TopRight || scaleEdge == Edge.BottomLeft) {
					vdx = -vdx;
				}
			}
		}
		if (scaleEdge == Edge.TopLeft) {
			dyt = vdy;
			dxl = vdx;
			if (symmetricScale) {
				dyb = -vdy;
				dxr = -vdx;
			}
		} else if (scaleEdge == Edge.TopRight) {
			dyt = vdy;
			dxr = vdx;
			if (symmetricScale) {
				dyb = -vdy;
				dxl = -vdx;
			}
		} else if (scaleEdge == Edge.BottomLeft) {
			dyb = vdy;
			dxl = vdx;
			if (symmetricScale) {
				dyt = -vdy;
				dxr = -vdx;
			}
		} else if (scaleEdge == Edge.BottomRight) {
			dyb = vdy;
			dxr = vdx;
			if (symmetricScale) {
				dyt = -vdy;
				dxl = -vdx;
			}
		} else if (scaleEdge == Edge.Top) {
			dyt = vdy;
			if (symmetricScale) {
				dyb = -vdy;
			}
		} else if (scaleEdge == Edge.Right) {
			dxr = vdx;
			if (symmetricScale) {
				dxl = -vdx;
			}
		} else if (scaleEdge == Edge.Bottom) {
			dyb = vdy;
			if (symmetricScale) {
				dyt = -vdy;
			}
		} else if (scaleEdge == Edge.Left) {
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

		if (!getConfig().isSafeMovement() || boundSetSafe(scaleNode.getComponent(), screenX, screenX + screenW, screenY, screenY + screenH)) {
			if (screenH < 0 || screenW < 0) { //negative scale
				return;
			}
			viewportComponent.setPositionPercent(px, py, pw, ph);
		}
	}

	private boolean basicMouseMovement(int mousex, int mousey) {

		updateContextMenu();
		mouseOverNode = null;

		for (UINode node : rootNode.deepIterateChildren()) {
			if (node.getComponent() == null) {
				continue;
			}
			if (node.getComponent().isEnabled()) {
				if (node.getComponent().containsPoint(mousex, mousey)) {
					mouseOverNode = node;
					break;
				}
			}
		}

		if (scaleNode == null) {
			if (!selection.isSelecting() && mouseOverNode != null) {
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
			for (UINode node : rootNode.deepIterateChildren()) {
				if (node.getComponent() == null) {
					continue;
				}
				if (node.getComponent().isEnabled()) {
					if (selection.contains(node.getComponent())) {
						selection.addToSelection(node);
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
			if (mouseOverNode != contextMenuControl && cm != canvasContextMenu) {
				cm.hide();
			} else if (cm == canvasContextMenu && mouseOverNode != null) {
				cm.hide();
			}
		}
	}

	/** Called from mouseMove. Checks to see if the given mouse position is near a component edge. If it is, it will store the component as well as the edge. */
	private void checkForScaling(int mousex, int mousey) {
		Edge edge;
		setReadyForScale(null, Edge.None);
		UINode component;
		for (int i = 0; i < selection.numSelected(); i++) {
			component = selection.getSelected().get(i);
			if (component.getComponent() == null) {
				continue;
			}
			if (!component.getComponent().isEnabled()) {
				continue;
			}
			edge = component.getComponent().getEdgeForPoint(mousex, mousey, COMPONENT_EDGE_LEEWAY);
			if (edge == Edge.None) {
				continue;
			}
			setReadyForScale(component, edge);
			changeCursorToScale(edge);
			return;
		}
	}

	private void setReadyForScale(@Nullable UINode toScale, @NotNull Edge scaleEdge) {
		this.scaleNode = toScale;
		this.scaleEdge = scaleEdge;
	}

	private double getSnapPixelsWidthF(double percentageDecimal) {
		int width = getConfig().viewportSnapEnabled() ? resolution.getViewportWidth() : getCanvasWidth();
		return (width * percentageDecimal);
	}

	private double getSnapPixelsHeightF(double percentageDecimal) {
		int height = getConfig().viewportSnapEnabled() ? resolution.getViewportHeight() : getCanvasHeight();
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
		if (edge == Edge.None) {
			changeCursorToDefault();
			return;
		}
		if (edge == Edge.TopLeft || edge == Edge.BottomRight) {
			canvas.setCursor(Cursor.NW_RESIZE);
			return;
		}
		if (edge == Edge.TopRight || edge == Edge.BottomLeft) {
			canvas.setCursor(Cursor.NE_RESIZE);
			return;
		}
		if (edge == Edge.Top || edge == Edge.Bottom) {
			canvas.setCursor(Cursor.N_RESIZE);
			return;
		}
		if (edge == Edge.Left || edge == Edge.Right) {
			canvas.setCursor(Cursor.W_RESIZE);
			return;
		}
		throw new IllegalStateException("couldn't find correct cursor for edge:" + edge.name());
	}


	@Override
	public void keyEvent(String key, boolean keyIsDown, boolean shiftDown, boolean ctrlDown, boolean altDown) {
		super.keyEvent(key, keyIsDown, shiftDown, ctrlDown, altDown);

		if (selection.getFirst() != null) {
			boolean movementStop = keys.keyIsDown(keyMap.PREVENT_HORIZONTAL_MOVEMENT) && keys.keyIsDown(keyMap.PREVENT_VERTICAL_MOVEMENT);
			if (!waitingForZXRelease && movementStop) {
				zxPressStartTimeMillis = System.currentTimeMillis();
				waitingForZXRelease = true;
			} else if (waitingForZXRelease && movementStop) {
				if (zxPressStartTimeMillis + 500 <= System.currentTimeMillis()) {
					for (UINode node : selection.getSelected()) {
						if (node.getComponent() == null) {
							continue;
						}
						node.getComponent().setEnabled(false);
					}
					selection.clearSelected();
					mouseOverNode = scaleNode = null;
					changeCursorToDefault();
					waitingForZXRelease = false;
				}
			}
		}
	}

	@NotNull
	public UICanvasEditorColors getColors() {
		return colors;
	}

	/**
	 @author Kayler
	 Created on 05/13/2016.
	 */
	private static class CanvasSelection extends SimpleCanvasComponent implements Selection {
		private final ObservableList<UINode> selected = FXCollections.observableList(new LinkedList<>());
		private boolean isSelecting;

		@Override
		public @NotNull ObservableList<UINode> getSelected() {
			return selected;
		}

		@Nullable
		@Override
		public UINode getFirst() {
			if (selected.size() == 0) {
				return null;
			}
			return selected.get(0);
		}

		@Override
		public void toggleFromSelection(@NotNull UINode node) {
			if (isSelected(node)) {
				selected.remove(node);
			} else {
				this.selected.add(node);
			}
		}

		@Override
		public void addToSelection(@NotNull UINode node) {
			if (!isSelected(node)) {
				this.selected.add(node);
			}
		}

		@Override
		public boolean isSelected(@Nullable UINode node) {
			if (node == null) {
				return false;
			}
			for (UINode c : selected) {
				if (c == node) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean removeFromSelection(@NotNull UINode node) {
			return this.selected.remove(node);
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

		void removeAllAndAdd(@NotNull UINode toAdd) {
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
