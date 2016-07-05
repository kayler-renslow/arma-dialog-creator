package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.ComponentContextMenuCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.DefaultComponentContextMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.Selection;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlCreationContextMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 Used to hold the canvas editor itself and canvas controls (tree view, step, etc). This class is also used to update the editor when need be.
 Created on 05/15/2016. */
class ADCCanvasView extends HBox implements CanvasView {
	private UICanvasEditor uiCanvasEditor;
	private final CanvasControls canvasControls = new CanvasControls(this);
	private ArmaResolution resolution;
	private ArmaDisplay display;

	/** True when the treeView selection is being updated from the canvas, false when it isn't */
	private boolean selectFromCanvas = false;
	/** True when the editor's selection is being updated from the treeView, false when it isn't */
	private boolean selectFromTreeview = false;
	private final UpdateListener<ArmaDisplay.DisplayUpdate> displayListener = new UpdateListener<ArmaDisplay.DisplayUpdate>() {
		@Override
		public void update(ArmaDisplay.DisplayUpdate data) {
			uiCanvasEditor.removeAllComponents();
			addAllControls(display.getControls(), true, uiCanvasEditor);
			uiCanvasEditor.paint();
		}
	};

	ADCCanvasView(ArmaResolution resolution) {
		this.resolution = resolution;
		initializeUICanvasEditor(resolution);

		this.getChildren().addAll(uiCanvasEditor, canvasControls);
		HBox.setHgrow(canvasControls, Priority.ALWAYS);

		setOnMouseMoved(new CanvasViewMouseEvent(this));
		focusToCanvas(true);
	}

	private void initializeUICanvasEditor(ArmaResolution r) {
		this.uiCanvasEditor = new UICanvasEditor(r, canvasControls);

		setToDisplay(ArmaDialogCreator.getApplicationData().getEditingDisplay());

		uiCanvasEditor.setComponentMenuCreator(new ComponentContextMenuCreator() {
			@Override
			public @NotNull ContextMenu initialize(CanvasComponent component) {
				return new DefaultComponentContextMenu(((ArmaControlRenderer) component).getMyControl());
			}
		});
		uiCanvasEditor.getDoubleClickObserver().addValueListener(new ValueListener<CanvasComponent>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<CanvasComponent> observer, CanvasComponent oldValue, CanvasComponent newValue) {
				if (newValue != null) {
					DefaultComponentContextMenu.showControlPropertiesPopup(((ArmaControlRenderer) newValue).getMyControl());
				}
			}
		});
		uiCanvasEditor.setCanvasContextMenu(new ControlCreationContextMenu(canvasControls.getEditorComponentTreeView(), false));
		setupEditorSelectionSync();
	}

	private void setupEditorSelectionSync() {
		uiCanvasEditor.getSelection().getSelected().addListener(new ListChangeListener<CanvasComponent>() {
			@Override
			public void onChanged(Change<? extends CanvasComponent> c) {
				if (selectFromTreeview) {
					return;
				}
				selectFromCanvas = true;
				List<ArmaControl> controlList = new ArrayList<>();
				for (CanvasComponent component : uiCanvasEditor.getSelection().getSelected()) {
					if (component instanceof ArmaControlRenderer) {
						controlList.add(((ArmaControlRenderer) component).getMyControl());
					}
				}
				canvasControls.getEditorComponentTreeView().setSelectedControls(controlList);
				selectFromCanvas = false;
			}
		});

		canvasControls.getEditorComponentTreeView().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<? extends TreeItemEntry>>() {
			@Override
			public void onChanged(Change<? extends TreeItem<? extends TreeItemEntry>> c) {
				if (selectFromCanvas) {
					return;
				}
				selectFromTreeview = true;
				Selection selection = uiCanvasEditor.getSelection();
				selection.clearSelected();
				for (TreeItem<? extends TreeItemEntry> treeItem : c.getList()) {
					if (treeItem.getValue() instanceof ControlTreeItemEntry) {
						ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) treeItem.getValue();
						selection.addToSelection(treeItemEntry.getMyArmaControl().getRenderer());
					}
				}
				repaintCanvas();
				selectFromTreeview = false;
			}
		});
	}

	private void setToDisplay(@NotNull ArmaDisplay display) {
		if (this.display != null) {
			this.display.getUpdateListenerGroup().removeUpdateListener(displayListener);
		}
		this.display = display;
		display.getUpdateListenerGroup().addListener(displayListener);
		addAllControls(display.getControls(), true, uiCanvasEditor);
		uiCanvasEditor.paint();
	}

	private static void addAllControls(List<ArmaControl> controls, boolean setVisible, UICanvas canvas) {
		for (ArmaControl control : controls) {
			canvas.addComponentNoPaint(control.getRenderer());
			control.getRenderer().disablePaintFromCanvas(!setVisible);
			if (control instanceof ArmaControlGroup) {
				addAllControls(((ArmaControlGroup) control).getControls(), false, canvas);
			}
		}
	}

	private void focusToCanvas(boolean focusToCanvas) {
		canvasControls.setFocusTraversable(!focusToCanvas);
		uiCanvasEditor.setFocusTraversable(focusToCanvas);
		if (focusToCanvas) {
			uiCanvasEditor.requestFocus();
		}
	}

	@Override
	public void setCanvasSize(int width, int height) {
		this.uiCanvasEditor.setCanvasSize(width, height);
	}

	@Override
	public void showGrid(boolean showGrid) {
		uiCanvasEditor.showGrid(showGrid);
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
	}

	@Override
	public void updateAbsRegion(int alwaysFront, int showing) {
		uiCanvasEditor.updateAbsRegion(alwaysFront, showing);
	}

	@Override
	public ArmaResolution getCurrentResolution() {
		return resolution;
	}

	void keyEvent(String text, boolean keyDown, boolean shiftDown, boolean controlDown, boolean altDown) {
		uiCanvasEditor.keyEvent(text, keyDown, shiftDown, controlDown, altDown);
	}

	void repaintCanvas() {
		uiCanvasEditor.paint();
	}


	private static class CanvasViewMouseEvent implements EventHandler<MouseEvent> {

		private final ADCCanvasView canvasView;

		CanvasViewMouseEvent(ADCCanvasView canvasView) {
			this.canvasView = canvasView;
		}

		@Override
		public void handle(MouseEvent event) {
			canvasView.focusToCanvas(event.getTarget() == canvasView.uiCanvasEditor.getCanvas());
		}
	}
}
