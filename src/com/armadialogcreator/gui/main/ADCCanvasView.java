package com.armadialogcreator.gui.main;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.canvas.*;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.data.tree.TreeStructure;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.GUITreeStructure;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.EditorComponentTreeView;
import com.armadialogcreator.gui.main.treeview.TreeItemEntry;
import com.armadialogcreator.gui.notification.NotificationPane;
import com.armadialogcreator.gui.notification.Notifications;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

	ADCCanvasView() {

		this.display = ApplicationManager.getInstance().getCurrentProject().getEditingDisplay();
		canvasControls = new CanvasControls(this);

		this.uiCanvasEditor = new UICanvasEditor(DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()), canvasControls, display);
		initializeUICanvasEditor(display);

		//init notification pane
		{
			VBox vboxNotifications = new VBox(10);
			vboxNotifications.setFillWidth(true);
			vboxNotifications.setAlignment(Pos.BOTTOM_RIGHT);
			vboxNotifications.setPadding(new Insets(5));
			vboxNotifications.setMinWidth(120);

			notificationPane = new NotificationPane(vboxNotifications);
			Notifications.setDefaultNotificationPane(notificationPane);
		}

		final StackPane stackPane = new StackPane(uiCanvasEditor, notificationPane.getContentPane());
		notificationPane.getContentPane().setMouseTransparent(true);

		this.getChildren().addAll(stackPane, canvasControls);
		HBox.setHgrow(canvasControls, Priority.ALWAYS);

		setOnMouseMoved(new CanvasViewMouseEvent(this));
		focusToCanvas(true);
	}

	private void initializeUICanvasEditor(@NotNull ArmaDisplay display) {
		canvasControls.getTreeViewMain().setToDisplay(display);
		canvasControls.getTreeViewBackground().setToDisplay(display);

		uiCanvasEditor.setComponentMenuCreator(new ComponentContextMenuCreator() {
			@Override
			public @NotNull ContextMenu initialize(CanvasComponent component) {
				return new DefaultComponentContextMenu(/*((ArmaControlRenderer) component).getMyControl()*/);
			}
		});
		uiCanvasEditor.getDoubleClickUpdateGroup().addListener((group, clickedControl) -> {
			if (clickedControl != null && clickedControl == uiCanvasEditor.getSelection().getFirst()) {
				//DefaultComponentContextMenu.showControlPropertiesPopup((ArmaControl) clickedControl);
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
		//If focusToCanvas, prevent key strokes from going to buttons or something else
		//and send them to the canvas
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
	@Nullable
	public ImagePattern getCanvasBackgroundImage() {
		return uiCanvasEditor.getBackgroundImage();
	}

	@Override
	@NotNull
	public Color getCanvasBackgroundColor() {
		return uiCanvasEditor.getBackgroundColor();
	}

	@Override
	public void updateAbsRegion(int alwaysFront, int showing) {
		uiCanvasEditor.updateAbsRegion(alwaysFront, showing);
	}

	@Override
	public void setTreeStructure(boolean background, @Nullable TreeStructure<ArmaControl> treeStructure) {
		if (background) {
			canvasControls.getTreeViewBackground().loadStructure(treeStructure);
		} else {
			canvasControls.getTreeViewMain().loadStructure(treeStructure);
		}
	}

	@NotNull
	@Override
	public GUITreeStructure<ArmaControl> getMainControlsTreeStructure() {
		return canvasControls.getTreeViewMain().exportStructure();
	}

	@NotNull
	@Override
	public GUITreeStructure<ArmaControl> getBackgroundControlsTreeStructure() {
		return canvasControls.getTreeViewBackground().exportStructure();
	}

	@NotNull
	@Override
	public EditableTreeView<ArmaControl, ? extends TreeItemEntry> getMainControlTreeView() {
		return canvasControls.getTreeViewMain();
	}

	@NotNull
	@Override
	public EditableTreeView<ArmaControl, ? extends TreeItemEntry> getBackgroundControlTreeView() {
		return canvasControls.getTreeViewBackground();
	}

	@NotNull
	@Override
	public UICanvasConfiguration getConfiguration() {
		return canvasControls;
	}

	void keyEvent(String text, boolean keyDown, boolean shiftDown, boolean controlDown, boolean altDown) {
		//forward the event
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
			for (Node node : canvasView.notificationPane.getContentPane().getChildren()) {
				Point2D point2D = node.sceneToLocal(sceneX, sceneY);
				if (node.contains(point2D)) {
					canvasView.notificationPane.getContentPane().setMouseTransparent(false);
					return;
				}
			}
			canvasView.notificationPane.getContentPane().setMouseTransparent(true);

		}
	}
}
