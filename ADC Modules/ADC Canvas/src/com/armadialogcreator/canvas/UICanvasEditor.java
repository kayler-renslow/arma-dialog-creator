package com.armadialogcreator.canvas;

import com.armadialogcreator.layout.Bounds;
import com.armadialogcreator.layout.Edge;
import com.armadialogcreator.layout.SimpleBounds;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
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

	/** {@link RenderAnchorPoint} that is ready to be scaled, null if none is ready to be scaled */
	private RenderAnchorPoint scalePoint;
	/** Edge that the scaling will be conducted, or Edge.None is no scaling is being done */
	private @Nullable Edge scaleEdge = null;
	/** {@link RenderAnchorPoint} that the mouse is over, or null if not over any */
	private RenderAnchorPoint mouseOverPoint;
	/** Component that the component context menu was created on, or null if the component context menu isn't open */
	private RenderAnchorPoint contextMenuPoint;

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

	private final UpdateListenerGroup<RenderAnchorPoint> doubleClickUpdateGroup = new UpdateListenerGroup<>();
	private Effect selectionEffect;

	public UICanvasEditor(@NotNull Resolution resolution, @NotNull UICanvasConfiguration configuration, @NotNull UIRenderer rootRenderer) {
		super(resolution, rootRenderer);

		setConfig(configuration);

		absRegionComponent = new ArmaAbsoluteBoxComponent(colors.absRegion, resolution);
		selection.selected.addListener(new ListChangeListener<>() {
			@Override
			public void onChanged(Change<? extends RenderAnchorPoint> c) {
				requestPaint();
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
	public UpdateListenerGroup<RenderAnchorPoint> getDoubleClickUpdateGroup() {
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
	public RenderAnchorPoint getMouseOverPoint() {
		return mouseOverPoint;
	}


	/** Updates the UI colors like selection color, grid color, and bg color */
	public void updateColors() {
		this.setCanvasBackgroundColor(colors.editorBg);
		initializeSelectionEffect();
	}


	/** Paint the canvas */
	protected void paint() {
		super.paint();
		if (selection.isSelecting()) {
			gc.save();
			gc.setStroke(colors.selection);
			gc.setLineWidth(2);
			gc.strokeRectangle(selection.bounds);
			gc.restore();
		}
	}

	@Override
	protected void paintRootRenderer() {
		if (isSelectingArea()) {
			for (RenderAnchorPoint rap : selection.getSelected()) {
				paintRendererAsSelected(rap.getRenderer());
			}
		}

		super.paintRootRenderer();

		gc.save();
		Iterator<RenderAnchorPoint> iter = selection.getSelected().iterator();
		while (iter.hasNext()) {
			RenderAnchorPoint rap = iter.next();
			if (rap.getRenderer().isGhost()) {
				iter.remove();
				continue;
			}
			gc.setStroke(rap.getRenderer().getBackgroundColor());
			gc.strokeRectangle(rap.bounds);
		}
		gc.restore();
	}

	private void paintRendererAsSelected(@NotNull UIRenderer rend) {
		if (!rend.isEnabled()) {
			return;
		}
		gc.save();
		Color selectedBorderColor = colors.selection;
		int centerx = toPixelsX(rend.getBounds().getCenterX());
		int centery = toPixelsY(rend.getBounds().getCenterY());
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
		int offset = 4 + (rend.getBorder() != null ? rend.getBorder().getThickness() : 0);
		int leftX = toPixelsX(rend.getBounds().getLeftX());
		int width = toPixelsX(rend.getBounds().getWidth());
		int topY = toPixelsY(rend.getBounds().getTopY());
		int height = toPixelsY(rend.getBounds().getHeight());
		gc.setEffect(selectionEffect);
		gc.setFill(colors.selection);
		gc.fillRectNoAA(leftX - offset, topY - offset, width + offset + offset, height + offset + offset);
		gc.restore();
	}

	private boolean isSelectingArea() {
		return selection.isSelecting();
	}

	/**
	 This is called when the mouse listener is invoked and a mouse press was the event.
	 This method should be the only one dealing with adding and removing nodes from the selection, other than mouseMove which adds to the selection via the selection box

	 @param mouseXPixels x position of mouse relative to canvas
	 @param mouseYPixels y position of mouse relative to canvas
	 @param mb mouse button that was pressed
	 */
	@Override
	protected void mousePressed(int mouseXPixels, int mouseYPixels, @NotNull MouseButton mb) {
		final double mousex = toPercentageX(mouseXPixels);
		final double mousey = toPercentageY(mouseYPixels);
		if (getContextMenu() != null) {
			getContextMenu().hide();
		}
		hasDoubleClickedCtrl = System.currentTimeMillis() - lastMousePressTime <= DOUBLE_CLICK_WAIT_TIME_MILLIS
				&& selection.numSelected() > 0
		;
		lastMousePressTime = System.currentTimeMillis();
		selection.setSelecting(false);
		this.mouseButtonDown = mb;

		if (mouseOverPoint != null) {
			if (keys.isCtrlDown()) {
				selection.toggleFromSelection(mouseOverPoint);
				return;
			} else {
				if (selection.numSelected() > 0) {
					if (selection.isSelected(mouseOverPoint)) {
						if (hasDoubleClickedCtrl && selection.numSelected() > 1) {
							selection.removeAllAndAdd(mouseOverPoint);
							hasDoubleClickedCtrl = false;//don't open configure node properties
						}
						return;
					}
					if (!keys.spaceDown()) { //if space is down, mouse over component should be selected
						RenderAnchorPoint rap;
						for (int i = 0; i < selection.numSelected(); i++) {
							rap = selection.getSelected().get(i);
							if (rap.bounds.containsPoint(mousex, mousey)) { //allow this one to stay selected despite the mouse not being over it
								return;
							}
						}
					}
				}
				selection.removeAllAndAdd(mouseOverPoint);
				return;
			}
		}
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
		contextMenuPoint = null;
		if (mb == MouseButton.SECONDARY) {
			if (menuCreator != null && selection.getFirst() != null) {
				contextMenuPoint = selection.getFirst();
				setContextMenu(menuCreator.initialize(contextMenuPoint), mousex, mousey);
				showContextMenu();
			} else if (canvasContextMenu != null) {
				setContextMenu(canvasContextMenu, mousex, mousey);
				showContextMenu();
			}
		} else {
			if (hasDoubleClickedCtrl) {
				if (selection.getFirst() != null) {
					doubleClickUpdateGroup.update(selection.getFirst());
				}
			}
		}
	}

	/**
	 This is called when the mouse is moved and/or dragged inside the canvas

	 @param mousex x position of mouse relative to canvas
	 @param mousey y position of mouse relative to canvas
	 */
	private boolean basicMouseMovement(double mousex, double mousey) {
		updateContextMenu();
		mouseOverPoint = null;

		for (RenderAnchorPoint rap : rootRenderer.iterateAnchorPoints()) {
			if (rap.bounds.containsPoint(mousex, mousey)) {
				mouseOverPoint = rap;
			}
			if (rap.getRenderer().isEnabled()) {
				if (rap.bounds.containsPoint(mousex, mousey)) {
					mouseOverPoint = rap;
				}
			}
		}

		if (mouseButtonDown == MouseButton.NONE) {
			return false;
		}
		if (mouseButtonDown == MouseButton.MIDDLE || (mouseButtonDown == MouseButton.SECONDARY && !selection.isSelecting())) {
			return false;
		}
		if (selection.isSelecting()) {
			selection.selectTo(mousex, mousey);
			selection.clearSelected();
			for (RenderAnchorPoint rap : rootRenderer.iterateAnchorPoints()) {
				if (rap.getRenderer().isEnabled()) {
					if (selection.isSelected(rap)) {
						selection.addToSelection(rap);
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
			if (mouseOverPoint != contextMenuPoint && cm != canvasContextMenu) {
				cm.hide();
			} else if (cm == canvasContextMenu && mouseOverPoint != null) {
				cm.hide();
			}
		}
	}

	private void showContextMenu() {
		if (contextMenu != null) {
			Point2D p = getCanvas().localToScreen(contextMenuPosition.getX(), contextMenuPosition.getY());
			contextMenu.show(getCanvas(), p.getX(), p.getY());
		}
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

	private void changeCursorToMove() {
		canvas.setCursor(Cursor.MOVE);
	}

	private void changeCursorToDefault() {
		canvas.setCursor(Cursor.DEFAULT);
	}

	private void changeCursorToScale(Edge edge) {
		if (edge == null) {
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
					for (RenderAnchorPoint rap : selection.getSelected()) {
						rap.getRenderer().setEnabled(false);
					}
					selection.clearSelected();
					mouseOverPoint = scalePoint = null;
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
	private static class CanvasSelection implements Selection<RenderAnchorPoint> {
		private final ObservableList<RenderAnchorPoint> selected = FXCollections.observableList(new LinkedList<>());
		public final Bounds bounds = new SelectionBounds(this);
		// selection.x1, selection.y1, selection.getWidth(), selection.getHeight()
		private boolean isSelecting;
		private double x1, x2;
		private double y1, y2;

		@Override
		public @NotNull ObservableList<RenderAnchorPoint> getSelected() {
			return selected;
		}

		@Nullable
		@Override
		public RenderAnchorPoint getFirst() {
			if (selected.size() == 0) {
				return null;
			}
			return selected.get(0);
		}

		@Override
		public void toggleFromSelection(@NotNull RenderAnchorPoint rap) {
			if (isSelected(rap)) {
				selected.remove(rap);
			} else {
				this.selected.add(rap);
			}
		}

		@Override
		public void addToSelection(@NotNull RenderAnchorPoint rap) {
			if (!isSelected(rap)) {
				this.selected.add(rap);
			}
		}

		@Override
		public boolean isSelected(@Nullable RenderAnchorPoint rap) {
			if (rap == null) {
				return false;
			}
			for (RenderAnchorPoint r : selected) {
				if (r == rap) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean removeFromSelection(@NotNull RenderAnchorPoint rap) {
			return this.selected.remove(rap);
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

		void removeAllAndAdd(@NotNull RenderAnchorPoint toAdd) {
			clearSelected();
			this.selected.add(toAdd);
		}

		void beginSelecting(double x, double y) {
			this.x1 = this.x2 = x;
			this.y1 = this.y2 = y;
			this.isSelecting = true;
		}

		void selectTo(double x, double y) {
			this.x2 = x;
			this.y2 = y;
		}

		public double getWidth() {
			return Math.abs(x2 - x1);
		}

		public double getHeight() {
			return Math.abs(y2 - y1);
		}
	}

	private static class SelectionBounds extends SimpleBounds {

		private final CanvasSelection selection;

		public SelectionBounds(@NotNull CanvasSelection selection) {
			super(null);
			this.selection = selection;
		}

		@Override
		public double getX() {
			return selection.x1;
		}

		@Override
		public double getY() {
			return selection.y1;
		}

		@Override
		public double getWidth() {
			return selection.getWidth();
		}

		@Override
		public double getHeight() {
			return selection.getHeight();
		}
	}

	private static class KeyMap {
		String SCALE_SQUARE = "s";
		String PREVENT_VERTICAL_MOVEMENT = "x";
		String PREVENT_HORIZONTAL_MOVEMENT = "z";
	}
}
