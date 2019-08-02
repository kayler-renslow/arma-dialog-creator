package com.armadialogcreator.canvas;

import com.armadialogcreator.layout.Edge;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
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
		this.setCanvasBackgroundColor(colors.editorBg);
		initializeSelectionEffect();
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
			gc.strokeRectangle(selection);
			gc.restore();
		}
	}

	@Override
	protected void paintRootNode() {
		super.paintRootNode();
		gc.save();
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
			gc.setStroke(node.getComponent().getBackgroundColor());
			gc.strokeRectangle(node.getComponent());
		}
		gc.restore();
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
			gc.fillRectNoAA(leftX - offset, topY - offset, width + offset + offset, height + offset + offset);
			gc.restore();
		}
		super.paintNode(node);
	}

	private boolean isSelectingArea() {
		return selection.isSelecting();
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
	private boolean basicMouseMovement(int mousex, int mousey) {
		updateContextMenu();
		mouseOverNode = null;

		for (UINode node : rootNode.deepIterateChildren()) {
			if (node.getComponent() == null) {
				continue;
			}
			if (node.isEnabled()) {
				if (node.getBounds().containsPoint(mousex, mousey)) {
					mouseOverNode = node;
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
			for (UINode node : rootNode.deepIterateChildren()) {
				if (node.getComponent() == null) {
					continue;
				}
				if (node.isEnabled()) {
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
						node.setEnabled(false);
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
	private static class CanvasSelection implements Selection {
		private final ObservableList<UINode> selected = FXCollections.observableList(new LinkedList<>());
		private boolean isSelecting;
		private int x1, x2;
		private int y1, y2;

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

		void beginSelecting(int x, int y) {
			this.x1 = this.x2 = x;
			this.y1 = this.y2 = y;
			this.isSelecting = true;
		}

		void selectTo(int x, int y) {
			this.x2 = x;
			this.y2 = y;
		}
	}


	private static class KeyMap {
		String SCALE_SQUARE = "s";
		String PREVENT_VERTICAL_MOVEMENT = "x";
		String PREVENT_HORIZONTAL_MOVEMENT = "z";
	}
}
