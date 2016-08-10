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
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.FoundChild;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeUtil;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
	private ArmaDisplay editingDisplay;
	private final boolean backgroundControlEditor;
	private final ListChangeListener<? super ArmaControl> displayListener = new ListChangeListener<ArmaControl>() {
		
		@Override
		public void onChanged(Change<? extends ArmaControl> c) {
			while (c.next()) {
				doOperations(c);
			}
		}
		
		private void doOperations(Change<? extends ArmaControl> c) {
			final ArrayList<ArmaControl> toAdd = new ArrayList<>(c.getAddedSubList());
			final ArrayList<ArmaControl> toRemove = new ArrayList<>(c.getRemoved());
			
			int ind = 0;
			ArmaControl controlToAdd;
			while (ind < toAdd.size()) {
				controlToAdd = toAdd.get(ind);
				ObservableList<ArmaControl> insertList = backgroundControlEditor ? editingDisplay.getBackgroundControls() : editingDisplay.getControls();
				int insertIndex = insertList.indexOf(controlToAdd);
				TreeItem<T> newTreeItem = createTreeItemForControl(controlToAdd);
				getRoot().getChildren().add(insertIndex, newTreeItem);
				if (controlToAdd instanceof ArmaControlGroup) {
					addControls(newTreeItem, ((ArmaControlGroup) controlToAdd).getControls());
				}
				ind++;
			}
			if (toRemove.size() > 0) {
				final LinkedList<Pair<TreeItem<T>, TreeItem<T>>> removeMeWhenDone = new LinkedList<>();
				TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<T>() {
					
					@Override
					public void found(TreeItem<T> found) {
						if (found.getValue() instanceof ControlTreeItemEntry) {
							ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) found.getValue();
							int ind = 0;
							ArmaControl control;
							while (ind < toRemove.size()) {
								control = toRemove.get(ind);
								if (treeItemEntry.getMyArmaControl() == control) {
									removeMeWhenDone.add(new Pair<>(found.getParent(), found));
								}
								ind++;
							}
						}
					}
				});
				if (removeMeWhenDone.size() != toRemove.size()) {
					throw new IllegalStateException("too many or too few controls were located for removal. found:" + removeMeWhenDone.size() + ", requested:" + toRemove.size());
				}
				while (removeMeWhenDone.size() > 0) {
					Pair<TreeItem<T>, TreeItem<T>> removed = removeMeWhenDone.removeFirst();
					removed.getKey().getChildren().remove(removed.getValue());
					if(removed.getValue().getValue() instanceof ControlGroupTreeItemEntry){
						ControlGroupTreeItemEntry removeTreeItemEntry = (ControlGroupTreeItemEntry) removed.getValue().getValue();
						removeTreeItemEntry.getControlGroup().getControls().removeListener(controlGroupListener);
					}
				}
			}
		}
	};
	private final ListChangeListener<? super ArmaControl> controlGroupListener = new ListChangeListener<ArmaControl>() {
		
		@Override
		public void onChanged(Change<? extends ArmaControl> c) {
			while (c.next()) {
				doOperations(c);
			}
		}
		
		private void doOperations(Change<? extends ArmaControl> c) {
			final ArrayList<ArmaControl> toAdd = new ArrayList<>(c.getAddedSubList());
			final ArrayList<ArmaControl> toRemove = new ArrayList<>(c.getRemoved());
			
			final LinkedList<Pair<TreeItem<T>, TreeItem<T>>> removeMeWhenDone = new LinkedList<>();
			final LinkedList<Pair<TreeItem<T>, ArmaControl>> addMeWhenDone = new LinkedList<>();
			
			TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<T>() {
				
				@Override
				public void found(TreeItem<T> found) {
					if (found.getValue() instanceof ControlTreeItemEntry) {
						ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) found.getValue();
						int ind = 0;
						ArmaControl controlToAdd;
						while (ind < toAdd.size()) {
							controlToAdd = toAdd.get(ind);
							
							if (controlToAdd.getParent() == treeItemEntry.getMyArmaControl()) {
								addMeWhenDone.add(new Pair<>(found, controlToAdd));
							}
							ind++;
						}
						
						ind = 0;
						ArmaControl control;
						while (ind < toRemove.size()) {
							control = toRemove.get(ind);
							if (treeItemEntry.getMyArmaControl() == control) {
								removeMeWhenDone.add(new Pair<>(found.getParent(), found));
							}
							ind++;
						}
						
					}
				}
			});
			if (addMeWhenDone.size() != toAdd.size()) {
				throw new IllegalStateException("too many or too few controls were located for insertion. found:" + addMeWhenDone.size() + ", requested:" + toAdd.size());
			}
			
			if (removeMeWhenDone.size() != toRemove.size()) {
				throw new IllegalStateException("too many or too few controls were located for removal. found:" + removeMeWhenDone.size() + ", requested:" + toRemove.size());
			}
			Pair<TreeItem<T>, TreeItem<T>> removeMe;
			while (removeMeWhenDone.size() > 0) {
				removeMe = removeMeWhenDone.removeFirst();
				removeMe.getKey().getChildren().remove(removeMe.getValue());
				if(removeMe.getValue().getValue() instanceof ControlGroupTreeItemEntry){
					ControlGroupTreeItemEntry removeTreeItemEntry = (ControlGroupTreeItemEntry) removeMe.getValue().getValue();
					removeTreeItemEntry.getControlGroup().getControls().removeListener(controlGroupListener);
				}
				
			}
			
			Pair<TreeItem<T>, ArmaControl> addMe;
			while (addMeWhenDone.size() > 0) {
				addMe = addMeWhenDone.removeFirst();
				ArmaControlGroup group = (ArmaControlGroup) addMe.getValue().getParent();
				int index = group.getControls().indexOf(addMe.getValue());
				addMe.getKey().getChildren().add(index, createTreeItemForControl(addMe.getValue()));
			}
			
		}
	};
	
	public EditorComponentTreeView(boolean backgroundControlEditor) {
		super(null);
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
	
	/**Set whether or not this tree view's display control/background control listener is activated. The listener is required to be disabled when a new tree item is being inserted into the tree
	 because if it wasn't, the tree view would get 2 tree items inserted*/
	private void setDisplayListener(boolean activate) {
		ObservableList<ArmaControl> list = backgroundControlEditor ? editingDisplay.getBackgroundControls() : editingDisplay.getControls();
		if (activate) {
			list.addListener(displayListener);
		} else {
			list.removeListener(displayListener);
		}
	}
	
	/** Set whether or not the given control group's controls listener is activated */
	private void setControlGroupListener(boolean activate, ArmaControlGroup group) {
		if (activate) {
			group.getControls().addListener(controlGroupListener);
		} else {
			group.getControls().removeListener(controlGroupListener);
		}
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
		if (this.editingDisplay != null) {
			setDisplayListener(false); //clear the old listeners on the old editing display since they are no longer needed
		}
		this.editingDisplay = display;
		setDisplayListener(true);
		getRoot().getChildren().clear();
		if (backgroundControlEditor) {
			addControls(getRoot(), display.getBackgroundControls());
		} else {
			addControls(getRoot(), display.getControls());
		}
	}
	
	private void addControls(TreeItem<T> parentTreeItem, Iterable<ArmaControl> controls) {
		for (ArmaControl control : controls) {
			if (control instanceof ArmaControlGroup) {
				addControls(createTreeItemForControl(control), ((ArmaControlGroup) control).getControls());
			} else {
				parentTreeItem.getChildren().add(createTreeItemForControl(control));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private TreeItem<T> createTreeItemForControl(ArmaControl control) {
		if (control instanceof ArmaControlGroup) {
			ArmaControlGroup group = (ArmaControlGroup) control;
			setControlGroupListener(true, group);
			return new TreeItem<>((T) new ControlGroupTreeItemEntry(group));
		}
		return new TreeItem<>((T) new ControlTreeItemEntry(control));
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addChildToParent(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> child, int index) {
		super.addChildToParent(parent, child, index);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		
		int correctedIndex;
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		
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
			if (!group.getControlGroup().getControls().contains(childControlEntry.getMyArmaControl())) {
				setControlGroupListener(false, group.getControlGroup());
				group.getControlGroup().getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
				setControlGroupListener(true, group.getControlGroup());
			}
		} else { //didn't go into a control group, so it is in a folder.
			setDisplayListener(false);
			correctedIndex = getCorrectedIndex(getRoot(), child);
			if (backgroundControlEditor) {
				if (!editingDisplay.getBackgroundControls().contains(childControlEntry.getMyArmaControl())) {
					editingDisplay.getBackgroundControls().add(correctedIndex, childControlEntry.getMyArmaControl());
				}
			} else {
				if (!editingDisplay.getControls().contains(childControlEntry.getMyArmaControl())) {
					editingDisplay.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
				}
			}
			setDisplayListener(true);
		}
		
	}
	
	@Override
	protected void addChildToRoot(@NotNull TreeItem<T> child) {
		super.addChildToRoot(child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		setDisplayListener(false);
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		if (backgroundControlEditor) {
			if (!editingDisplay.getBackgroundControls().contains(childControlEntry.getMyArmaControl())) {
				editingDisplay.getBackgroundControls().add(childControlEntry.getMyArmaControl());
			}
		} else {
			if (!editingDisplay.getControls().contains(childControlEntry.getMyArmaControl())) {
				editingDisplay.getControls().add(childControlEntry.getMyArmaControl());
			}
		}
		setDisplayListener(true);
	}
	
	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<T> child) {
		super.addChildToRoot(index, child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		setDisplayListener(false);
		int correctedIndex = getCorrectedIndex(getRoot(), child);
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		if (backgroundControlEditor) {
			if (!editingDisplay.getBackgroundControls().contains(childControlEntry.getMyArmaControl())) {
				editingDisplay.getBackgroundControls().add(correctedIndex, childControlEntry.getMyArmaControl());
			}
		} else {
			if (!editingDisplay.getControls().contains(childControlEntry.getMyArmaControl())) {
				editingDisplay.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
			}
		}
		setDisplayListener(true);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void removeChild(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> toRemove) {
		super.removeChild(parent, toRemove);
		if (toRemove.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry toRemoveControlEntry = (ControlTreeItemEntry) toRemove.getValue();
		if (parent == getRoot()) {
			if (backgroundControlEditor) {
				editingDisplay.getBackgroundControls().remove(toRemoveControlEntry.getMyArmaControl());
			} else {
				editingDisplay.getControls().remove(toRemoveControlEntry.getMyArmaControl());
			}
			
		} else {
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
				setControlGroupListener(false, group.getControlGroup());
				group.getControlGroup().getControls().remove(toRemoveControlEntry.getMyArmaControl());
				setControlGroupListener(true, group.getControlGroup());
			} else {
				setDisplayListener(false);
				if (backgroundControlEditor) {
					editingDisplay.getBackgroundControls().remove(toRemoveControlEntry.getMyArmaControl());
				} else {
					editingDisplay.getControls().remove(toRemoveControlEntry.getMyArmaControl());
				}
				setDisplayListener(true);
			}
		}
		
	}
	//@formatter:off
	/**
	 This is meant to insert controls correctly into the tree view and inserted at the right indexes in the display. The algorithm should work as follows:<br>
	 <ul>
	 <li>"corrected index" is the index that the control will be inserted at in a controls list (this index can greatly differ from the tree index/row)</li>
	 <li>the corrected index is the index that does NOT include folders, therefore, if there exists a folder, the index will be -1 the child's index</li>
	 <li>if there exists a folder with n control children, given_index-n will be corrected index</li>
	 <li>if there exists a control group, only the control group itself will be a part of the corrected index and not it's children</li>
	 <li>if adding a control in a control group, the corrected index will not take into account any ancestor controls.<br>
	 </li>
	 <pre>
	 [root]
	 ..+ first_control
	 ..+ control group
	 ....+ control - insert here, corrected index is 0, while the TREE index is 2 (start counting from first_control)
	 ..+ control
	 </pre>
	 <pre>
	 [root]
	 ..+ leaf
	 ..+ folder
	 ....+ leaf2
	 ....+ leaf3
	 ....+ folder2
	 ......+ leaf4 - insert, corrected index is 3
	 </pre>
	 <pre>
	 [root]
	 ..+ leaf
	 ..+ folder
	 ....+ leaf2
	 ....+ leaf3
	 ....+ folder2
	 ......+ leaf4
	 ....+ leaf5 - insert, corrected index is 4
	 </pre>
	 <pre>
	 [root]
	 ..+ leaf
	 ..+ folder
	 ....+ leaf2
	 ....+ leaf3
	 ....+ folder2
	 ......+ control group
	 ........+ leaf 4
	 ....+ leaf5 - insert, corrected index is 4
	 </pre>
	 </ul>
	 */
	protected int getCorrectedIndex(@Nullable TreeItem parent, @NotNull TreeItem<T> childAdded) {
		return getNumNonFolders(parent == null ? getRoot() : parent, childAdded, new BooleanEdit(false));
	}
	//@formatter:on
	
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
