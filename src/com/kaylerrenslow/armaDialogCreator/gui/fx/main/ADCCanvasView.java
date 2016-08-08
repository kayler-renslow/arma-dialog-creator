/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Control;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.ComponentContextMenuCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.DefaultComponentContextMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.Selection;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.UICanvasEditor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlCreationContextMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.EditorComponentTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
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
	private final UICanvasEditor uiCanvasEditor;
	private final CanvasControls canvasControls = new CanvasControls(this);
	
	/** True when the treeView selection is being updated from the canvas, false when it isn't */
	private boolean selectFromCanvas = false;
	/** True when the editor's selection is being updated from the treeView, false when it isn't */
	private boolean selectFromTreeView = false;
	
	ADCCanvasView() {
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getCurrentProject().getEditingDisplay();
		this.uiCanvasEditor = new UICanvasEditor(DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()), canvasControls, display);
		initializeUICanvasEditor(display);
		
		this.getChildren().addAll(uiCanvasEditor, canvasControls);
		HBox.setHgrow(canvasControls, Priority.ALWAYS);
		
		setOnMouseMoved(new CanvasViewMouseEvent(this));
		focusToCanvas(true);
	}
	
	private void initializeUICanvasEditor(ArmaDisplay display) {
		canvasControls.getTreeViewMain().setToDisplay(display);
		
		uiCanvasEditor.setComponentMenuCreator(new ComponentContextMenuCreator() {
			@Override
			public @NotNull ContextMenu initialize(CanvasComponent component) {
				return new DefaultComponentContextMenu(((ArmaControlRenderer) component).getMyControl());
			}
		});
		uiCanvasEditor.getDoubleClickObserver().addValueListener(new ValueListener<Control>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Control> observer, Control oldValue, Control newValue) {
				if (newValue != null && uiCanvasEditor.getMouseOverControl() == uiCanvasEditor.getSelection().getFirst()) {
					DefaultComponentContextMenu.showControlPropertiesPopup((ArmaControl) newValue);
				}
			}
		});
		uiCanvasEditor.setCanvasContextMenu(new ControlCreationContextMenu(canvasControls.getTreeViewMain(), false));
		setupEditorSelectionSync();
	}
	
	private void setupEditorSelectionSync() {
		syncTreeView(canvasControls.getTreeViewMain());
		syncTreeView(canvasControls.getTreeViewBackground());
	}
	
	private void syncTreeView(EditorComponentTreeView<? extends TreeItemEntry> treeView) {
		uiCanvasEditor.getSelection().getSelected().addListener(new ListChangeListener<Control>() {
			@Override
			public void onChanged(Change<? extends Control> c) {
				if (selectFromTreeView) {
					return;
				}
				selectFromCanvas = true;
				List<ArmaControl> controlList = new ArrayList<>(uiCanvasEditor.getSelection().getSelected().size());
				for (Control control : uiCanvasEditor.getSelection().getSelected()) {
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
				repaintCanvas();
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
	public void setTreeStructure(TreeStructure<TreeItemEntry> treeStructure) {
		canvasControls.getTreeViewMain().loadStructure(treeStructure);
	}
	
	void keyEvent(String text, boolean keyDown, boolean shiftDown, boolean controlDown, boolean altDown) {
		uiCanvasEditor.keyEvent(text, keyDown, shiftDown, controlDown, altDown);
	}
	
	void repaintCanvas() {
		uiCanvasEditor.paint();
	}
	
	void hideCanvasControls(boolean hide) {
		getChildren().remove(canvasControls);
		if (hide) {
			return;
		}
		getChildren().add(canvasControls);
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
		}
	}
}
