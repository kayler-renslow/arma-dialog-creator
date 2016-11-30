package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasControl;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.EditorComponentTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.notification.NotificationPane;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 Used to hold the canvas editor itself and canvas controls (tree view, step, etc). This class is also used to update the editor when need be.

 @author Kayler
 @since 05/15/2016. */
class ADCCanvasView extends HBox implements CanvasView {
	private final UICanvasEditor uiCanvasEditor;
	private final CanvasControls canvasControls;
	private final NotificationPane notificationPane;

	/** True when the treeView selection is being updated from the canvas, false when it isn't */
	private boolean selectFromCanvas = false;
	/** True when the editor's selection is being updated from the treeView, false when it isn't */
	private boolean selectFromTreeView = false;
	private ArmaDisplay display;

	ADCCanvasView(@NotNull NotificationPane notificationPane) {
		this.notificationPane = notificationPane;
		this.display = ArmaDialogCreator.getApplicationData().getCurrentProject().getEditingDisplay();
		canvasControls = new CanvasControls(this);

		this.uiCanvasEditor = new UICanvasEditor(DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()), canvasControls, display);
		initializeUICanvasEditor(display);

		final StackPane stackPane = new StackPane(uiCanvasEditor, notificationPane.getNotificationsPane());
		notificationPane.getNotificationsPane().setMouseTransparent(true);


		this.getChildren().addAll(stackPane, canvasControls);
		HBox.setHgrow(canvasControls, Priority.ALWAYS);

		setOnMouseMoved(new CanvasViewMouseEvent(this));
		focusToCanvas(true);
	}

	private void initializeUICanvasEditor(ArmaDisplay display) {
		canvasControls.getTreeViewMain().setToDisplay(display);
		canvasControls.getTreeViewBackground().setToDisplay(display);

		uiCanvasEditor.setComponentMenuCreator(new ComponentContextMenuCreator() {
			@Override
			public @NotNull ContextMenu initialize(CanvasComponent component) {
				return new DefaultComponentContextMenu(((ArmaControlRenderer) component).getMyControl());
			}
		});
		uiCanvasEditor.getDoubleClickObserver().addListener(new ValueListener<CanvasControl>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<CanvasControl> observer, CanvasControl oldValue, CanvasControl newValue) {
				if (newValue != null && uiCanvasEditor.getMouseOverControl() == uiCanvasEditor.getSelection().getFirst()) {
					DefaultComponentContextMenu.showControlPropertiesPopup((ArmaControl) newValue);
				}
			}
		});
		uiCanvasEditor.setCanvasContextMenu(new CanvasContextMenu());
		setupEditorSelectionSync();
	}

	private void setupEditorSelectionSync() {
		syncTreeView(canvasControls.getTreeViewMain());
		syncTreeView(canvasControls.getTreeViewBackground());
	}

	private void syncTreeView(EditorComponentTreeView<? extends TreeItemEntry> treeView) {
		uiCanvasEditor.getSelection().getSelected().addListener(new ListChangeListener<CanvasControl>() {
			@Override
			public void onChanged(Change<? extends CanvasControl> c) {
				if (selectFromTreeView) {
					return;
				}
				selectFromCanvas = true;
				List<ArmaControl> controlList = new ArrayList<>(uiCanvasEditor.getSelection().getSelected().size());
				for (CanvasControl control : uiCanvasEditor.getSelection().getSelected()) {
					if (control instanceof ArmaControl) {
						controlList.add((ArmaControl) control);
					}
				}
				treeView.setSelectedControls(controlList);
				selectFromCanvas = false;
			}
		});
		treeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<? extends TreeItemEntry>>() {
			@Override
			public void onChanged(Change<? extends TreeItem<? extends TreeItemEntry>> c) {
				if (selectFromCanvas) {
					return;
				}
				selectFromTreeView = true;
				Selection selection = uiCanvasEditor.getSelection();
				selection.clearSelected();
				for (TreeItem<? extends TreeItemEntry> treeItem : c.getList()) {
					if (treeItem.getValue() instanceof ControlTreeItemEntry) {
						ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) treeItem.getValue();
						if (treeItemEntry.isEnabled()) {
							selection.addToSelection(treeItemEntry.getMyArmaControl());
						}
					}
				}
				selectFromTreeView = false;
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
		uiCanvasEditor.requestPaint();
	}

	@Override
	public void updateAbsRegion(int alwaysFront, int showing) {
		uiCanvasEditor.updateAbsRegion(alwaysFront, showing);
	}

	@Override
	public void setTreeStructure(boolean background, TreeStructure<TreeItemEntry> treeStructure) {
		if (background) {
			canvasControls.getTreeViewBackground().loadStructure(treeStructure);
		} else {
			canvasControls.getTreeViewMain().loadStructure(treeStructure);
		}
	}

	@NotNull
	@Override
	public TreeStructure<? extends TreeItemEntry> getMainControlsTreeStructure() {
		return canvasControls.getTreeViewMain().exportStructure();
	}

	@NotNull
	@Override
	public TreeStructure<? extends TreeItemEntry> getBackgroundControlsTreeStructure() {
		return canvasControls.getTreeViewBackground().exportStructure();
	}

	@NotNull
	@Override
	public EditableTreeView<? extends TreeItemEntry> getMainControlTreeView() {
		return canvasControls.getTreeViewMain();
	}

	@NotNull
	@Override
	public EditableTreeView<? extends TreeItemEntry> getBackgroundControlTreeView() {
		return canvasControls.getTreeViewBackground();
	}

	@NotNull
	@Override
	public UICanvasConfiguration getConfiguration() {
		return canvasControls;
	}

	void keyEvent(String text, boolean keyDown, boolean shiftDown, boolean controlDown, boolean altDown) {
		uiCanvasEditor.keyEvent(text, keyDown, shiftDown, controlDown, altDown);
	}


	void hideCanvasControls(boolean hide) {
		getChildren().remove(canvasControls);
		if (hide) {
			return;
		}
		getChildren().add(canvasControls);
	}

	public ArmaDisplay getEditingDisplay() {
		return this.display;
	}

	public void repaintCanvas() {
		uiCanvasEditor.requestPaint();
	}


	private static class CanvasViewMouseEvent implements EventHandler<MouseEvent> {

		private final ADCCanvasView canvasView;

		CanvasViewMouseEvent(ADCCanvasView canvasView) {
			this.canvasView = canvasView;
		}

		@Override
		public void handle(MouseEvent event) {
			//use this so that when the mouse moves over the canvas and something in canvas controls has focus, the key presses
			//and mouse events are sent to the canvas rather than the focuses control
			canvasView.focusToCanvas(event.getTarget() == canvasView.uiCanvasEditor.getCanvas());

			double sceneX = event.getSceneX();
			double sceneY = event.getSceneY();
			for (Node node : canvasView.notificationPane.getNotificationsPane().getChildren()) {
				Point2D point2D = node.sceneToLocal(sceneX, sceneY);
				if (node.contains(point2D)) {
					canvasView.notificationPane.getNotificationsPane().setMouseTransparent(false);
					return;
				}
			}
			canvasView.notificationPane.getNotificationsPane().setMouseTransparent(true);

		}
	}
}
