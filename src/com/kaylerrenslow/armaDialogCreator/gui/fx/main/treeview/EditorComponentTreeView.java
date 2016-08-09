/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.FoundChild;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeUtil;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 Houses the actual tree view. This class is the link between the application data and the tree view's data.
 Anything that happens to the gui tree view will echo through the application data and vice versa through this class.
 Created on 06/08/2016. */
public class EditorComponentTreeView<T extends TreeItemEntry> extends EditableTreeView<T> {
	
	private final ContextMenu controlCreationContextMenu = new ControlCreationContextMenu(this, true);
	private final ArmaDisplay editingDisplay;
	private final boolean backgroundControlEditor;
	
	public EditorComponentTreeView(ArmaDisplay editingDisplay, boolean backgroundControlEditor) {
		super(null);
		this.editingDisplay = editingDisplay;
		this.backgroundControlEditor = backgroundControlEditor;
		setContextMenu(controlCreationContextMenu);
		EditorComponentTreeView treeView = this;
		getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<T>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<T>> observable, TreeItem<T> oldValue, TreeItem<T> selected) {
				if (selected != null && selected.getValue() instanceof ControlTreeItemEntry) {
					treeView.setContextMenu(new ControlEditContextMenu((ControlTreeItemEntry) selected.getValue()));
				} else {
					treeView.setContextMenu(controlCreationContextMenu);
				}
			}
		});
	}
	
	/**
	 Sets the selection such that only the given controls are selected
	 
	 @param controlList list of controls to select
	 */
	public void setSelectedControls(List<ArmaControl> controlList) {
		getSelectionModel().clearSelection();
		TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<T>() {
			@Override
			public void found(TreeItem<T> found) {
				if (found.getValue() instanceof ControlTreeItemEntry) {
					ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) found.getValue();
					Iterator<ArmaControl> controlIterator = controlList.iterator();
					ArmaControl iterNext;
					while (controlIterator.hasNext()) {
						iterNext = controlIterator.next();
						if (iterNext == treeItemEntry.getMyArmaControl()) {
							getSelectionModel().select(found);
						}
					}
				}
			}
		});
	}
	
	public void setToDisplay(ArmaDisplay display) {
		getRoot().getChildren().clear();
		if (backgroundControlEditor) {
			addControls(getRoot(), null, display.getBackgroundControls());
		} else {
			addControls(getRoot(), null, display.getControls());
		}
	}
	
	private void addControls(TreeItem<T> parentTreeItem, ArmaControl parent, Iterable<ArmaControl> controls) {
		for (ArmaControl control : controls) {
			if (control instanceof ArmaControlGroup) {
				addControls(getTreeData(parent), control, ((ArmaControlGroup) control).getControls());
			} else {
				addChildToParent2(parentTreeItem, getTreeData(control), parentTreeItem.getChildren().size());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private TreeItem<T> getTreeData(ArmaControl control) {
		return new TreeItem<>(control instanceof ArmaControlGroup ? (T) new ControlGroupTreeItemEntry((ArmaControlGroup) control) : (T) new ControlTreeItemEntry(control));
	}
	
	@SuppressWarnings("unchecked")
	private void addChildToParent2(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> child, int index) {
		super.addChildToParent(parent, child, index);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		int correctedIndex;
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = this.editingDisplay;
		
		ControlGroupTreeItemEntry group = null;
		TreeItem<ControlGroupTreeItemEntry> groupTreeItem;
		if (parent.getValue() instanceof ControlGroupTreeItemEntry) {
			groupTreeItem = (TreeItem<ControlGroupTreeItemEntry>) parent;
			group = (ControlGroupTreeItemEntry) parent.getValue();
		} else {
			groupTreeItem = getAncestorOfEntryType(parent, ControlGroupTreeItemEntry.class);
			if (groupTreeItem != null) {
				group = groupTreeItem.getValue();
			}
		}
		
		if (group != null) {
			correctedIndex = getCorrectedIndex(groupTreeItem, child);
			if (group.getControlGroup().getControls().contains(childControlEntry.getMyArmaControl())) {
				return;
			}
			group.getControlGroup().getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
		} else { //didn't go into a control group, so it is in a folder.
			correctedIndex = getCorrectedIndex(getRoot(), child);
			if (backgroundControlEditor) {
				if (display.getBackgroundControls().contains(childControlEntry.getMyArmaControl())) {
					return;
				}
				display.getBackgroundControls().add(correctedIndex, childControlEntry.getMyArmaControl());
			} else {
				if (display.getControls().contains(childControlEntry.getMyArmaControl())) {
					return;
				}
				display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addChildToParent(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> child, int index) {
		addChildToParent2(parent, child, index);
	}
	
	@Override
	protected void addChildToRoot(@NotNull TreeItem<T> child) {
		super.addChildToRoot(child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = this.editingDisplay;
		if (backgroundControlEditor) {
			if (display.getBackgroundControls().contains(childControlEntry.getMyArmaControl())) {
				return;
			}
			display.getBackgroundControls().add(childControlEntry.getMyArmaControl());
		} else {
			if (display.getControls().contains(childControlEntry.getMyArmaControl())) {
				return;
			}
			display.getControls().add(childControlEntry.getMyArmaControl());
		}
	}
	
	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<T> child) {
		super.addChildToRoot(index, child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		int correctedIndex = getCorrectedIndex(getRoot(), child);
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = this.editingDisplay;
		if (backgroundControlEditor) {
			if (display.getBackgroundControls().contains(childControlEntry.getMyArmaControl())) {
				return;
			}
			display.getBackgroundControls().add(correctedIndex, childControlEntry.getMyArmaControl());
		} else {
			if (display.getControls().contains(childControlEntry.getMyArmaControl())) {
				return;
			}
			display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void removeChild(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> toRemove) {
		super.removeChild(parent, toRemove);
		if (toRemove.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry toRemoveControlEntry = (ControlTreeItemEntry) toRemove.getValue();
		ArmaDisplay display = this.editingDisplay;
		if (parent == getRoot()) {
			if (backgroundControlEditor) {
				display.getBackgroundControls().remove(toRemoveControlEntry.getMyArmaControl());
			} else {
				display.getControls().remove(toRemoveControlEntry.getMyArmaControl());
			}
			return;
		}
		
		ControlGroupTreeItemEntry group = null;
		if (parent.getValue() instanceof ControlGroupTreeItemEntry) {
			group = (ControlGroupTreeItemEntry) parent.getValue();
		} else {
			TreeItem<ControlGroupTreeItemEntry> groupTreeItem = getAncestorOfEntryType(parent, ControlGroupTreeItemEntry.class);
			if (groupTreeItem != null) {
				group = groupTreeItem.getValue();
			}
		}
		if (group != null) {
			group.getControlGroup().getControls().remove(toRemoveControlEntry.getMyArmaControl());
		} else {
			if (backgroundControlEditor) {
				display.getBackgroundControls().remove(toRemoveControlEntry.getMyArmaControl());
			} else {
				display.getControls().remove(toRemoveControlEntry.getMyArmaControl());
			}
		}
		
	}
	
	//	private int getCorrectedIndex(int rowOriginStart, int row, TreeItem<T> start) {
	//		//get row after insertion. traverse upwards and count how many folders there are and subtract that from row
	//		System.out.println("EditorComponentTreeView.getCorrectedIndex row = " + row);
	//		int correctedIndex = row - rowOriginStart; //index such that the folders weren't used to calculate index
	//		TreeItem<T> cursor = start;
	//		while (cursor != getRoot() && cursor.getValue().getCellType() == CellType.FOLDER) {
	//			for (TreeItem<T> child : cursor.getChildren()) {
	//				if (child.getValue().getCellType() == CellType.FOLDER && getRow(child) < row) { //only subtract the folders preceding the row
	//					System.out.println("EditorComponentTreeView.getCorrectedIndex child = " + child);
	//					correctedIndex--;
	//				}
	//			}
	//			cursor = cursor.getParent();
	//		}
	//		for (TreeItem<T> child : cursor.getChildren()) {
	//			if (child.getValue().getCellType() == CellType.FOLDER && getRow(child) < row) { //only subtract the folders preceding the row
	//				correctedIndex--;
	//			}
	//		}
	//
	//		return correctedIndex;
	//	}
	
	protected int getCorrectedIndex(@Nullable TreeItem parent, @NotNull TreeItem<T> childAdded) {
		return getNumNonFolders(parent == null ? getRoot() : parent, childAdded, new BooleanEdit(false));
	}
	
	/**
	 Gets the number of descendants of the tree item that aren't a folder. However, when {@link CellType#COMPOSITE} is reached, the depth count will not go deeper and the size counter will only
	 increment by one.
	 */
	private int getNumNonFolders(TreeItem<T> treeItem, TreeItem<T> stopAt, BooleanEdit found) {
		LinkedList<TreeItem<T>> q = new LinkedList<>();
		q.addAll(treeItem.getChildren());
		TreeItem<T> pop;
		int size = 0;
		while (q.size() > 0) {
			pop = q.pop();
			if (pop == stopAt) {
				found.setValue(true);
				return size;
			}
			switch (pop.getValue().getCellType()) {
				case FOLDER: {
					size += getNumNonFolders(pop, stopAt, found);
					if (found.isTrue()) {
						return size;
					}
					break;
				}
				case COMPOSITE: {
					size += 1;
					break;
				}
				case LEAF: {
					size += 1;
					break;
				}
				default: {
					throw new IllegalStateException("unknown cell type " + pop.getValue().getCellType());
				}
			}
		}
		return size;
		
	}
	
	@SuppressWarnings("unchecked")
	protected static <T, T2 extends TreeItemEntry> TreeItem<T2> getAncestorOfEntryType(TreeItem<T> start, Class<T2> clazz) {
		if (start.getParent() == null || start.getParent().getValue() == null) {
			return null;
		}
		if (clazz.isAssignableFrom(start.getParent().getValue().getClass())) {
			return (TreeItem<T2>) start.getParent();
		}
		return getAncestorOfEntryType(start.getParent(), clazz);
	}
	
	
	static ImageView createFolderIcon() {
		return new ImageView(ImagePaths.ICON_FOLDER);
	}
	
	static ImageView createCompositeIcon() {
		return new ImageView(ImagePaths.ICON_COMPOSITE);
	}
	
	private static class BooleanEdit {
		private boolean value;
		
		public BooleanEdit(boolean init) {
			this.value = init;
		}
		
		public void setValue(boolean value) {
			this.value = value;
		}
		
		public boolean isTrue() {
			return value;
		}
	}
	
}
