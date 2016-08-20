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
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlList;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlListChange;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlListChangeListener;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Display;
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
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 Houses the actual tree view for showing what controls and folders exist in the current project. This class is the link between the project's display and the tree view's data.
 Anything that happens to the gui tree view will echo through the display and vice versa through this class.
 Created on 06/08/2016. */
public class EditorComponentTreeView<T extends TreeItemEntry> extends EditableTreeView<T> {

	private final ContextMenu controlCreationContextMenu = new ControlCreationContextMenu(this, true);
	private ArmaDisplay editingDisplay;
	private final boolean backgroundControlEditor;
	private final ControlListChangeListener<ArmaControl> controlListChangeListener = new ControlListChangeListener<ArmaControl>() {

		@Override
		public void onChanged(ControlList<ArmaControl> controlList, ControlListChange<ArmaControl> change) {
			if (change.getModifiedList() == getTargetControlList()) {//no need to search for things that shouldn't exist in the tree view
				if (change.wasSet()) {
					handleSet(change);
					return;
				} else if (change.wasRemoved()) {
					handleRemove(change);
					return;
				} else if (change.wasAdded()) {
					handleAdd(controlList, change);
					return;
				}
			}
			if (change.wasMoved()) {
				handleMove(controlList, change);
			}
		}

		private void handleMove(final ControlList<ArmaControl> controlList, final ControlListChange<ArmaControl> change) {
			final LinkedList<TreeItem<T>> removeMeWhenDone = new LinkedList<>();
			final LinkedList<TreeItem<T>> addToMeWhenDone = new LinkedList<>();
			if (controlList == getTargetControlList() && change.getMoved().getDestinationList() == getTargetControlList()) { //adding to root
				addToMeWhenDone.add(getRoot());
			}

			/*
			Locate the old tree item and remove it. Then, find the new parent (if it exists in this tree view) and add the child there
			*/
			TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<T>() {
				private boolean foundOldTreeItem = false;

				//no need to find parent if the parent is the display (will be the root of the tree)
				private boolean foundNewParent = addToMeWhenDone.size() > 0;

				@Override
				public boolean found(TreeItem<T> found) {
					if (found.getValue() instanceof ControlTreeItemEntry) { //maybe found old tree item
						ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) found.getValue();
						if (treeItemEntry.getMyArmaControl() == change.getMoved().getMovedControl()) { //found old tree item
							removeMeWhenDone.add(found);
							foundOldTreeItem = true;
						}
						if (!foundNewParent && found.getValue() instanceof ControlGroupTreeItemEntry) {
							ControlGroupTreeItemEntry groupTreeItemEntry = (ControlGroupTreeItemEntry) found.getValue();
							if (groupTreeItemEntry.getMyArmaControl() == change.getMoved().getDestinationHolder()) { //found new parent
								addToMeWhenDone.add(found);
								foundNewParent = true;
							}
						}
					}
					return foundOldTreeItem && foundNewParent;
				}
			});
			while (removeMeWhenDone.size() > 0) {
				System.out.println("EditorComponentTreeView.handleMove REMOVE backgroundControlEditor=" + backgroundControlEditor);
				TreeItem<T> toRemove = removeMeWhenDone.removeFirst();
				toRemove.getParent().getChildren().remove(toRemove);
				if (toRemove.getValue() instanceof ControlGroupTreeItemEntry) {
					ControlGroupTreeItemEntry removeTreeItemEntry = (ControlGroupTreeItemEntry) toRemove.getValue();
					removeListeners(removeTreeItemEntry.getControlGroup().getControls());
				}
			}
			while (addToMeWhenDone.size() > 0) {
				System.out.println("EditorComponentTreeView.handleMove ADD backgroundControlEditor=" + backgroundControlEditor);
				TreeItem<T> addToMe = addToMeWhenDone.removeFirst();
				TreeItem<T> newTreeItem = createTreeItemForControl(change.getMoved().getMovedControl());
				addToMe.getChildren().add(change.getMoved().getDestinationIndex(), newTreeItem);
				if (change.getMoved().getMovedControl() instanceof ArmaControlGroup) {
					ArmaControlGroup group = (ArmaControlGroup) change.getMoved().getMovedControl();
					addControls(newTreeItem, group.getControls());
				}
			}
		}

		private void handleAdd(ControlList<ArmaControl> controlList, final ControlListChange<ArmaControl> change) {
			int insertIndex = controlList.indexOf(change.getAdded().getControl());
			TreeItem<T> newTreeItem = createTreeItemForControl(change.getAdded().getControl());
			if (change.getAdded().getControl().getHolder() instanceof Display) {
				getRoot().getChildren().add(insertIndex, newTreeItem);
				if (change.getAdded().getControl() instanceof ArmaControlGroup) {
					addControls(newTreeItem, ((ArmaControlGroup) change.getAdded().getControl()).getControls());
				}
				return;
			}
			/*
			Since the holder is not a display, must be in a control group
			*/
			final LinkedList<TreeItem<T>> addMeWhenDone = new LinkedList<>();
			TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<T>() {

				@Override
				public boolean found(TreeItem<T> found) {
					if (found.getValue() instanceof ControlGroupTreeItemEntry) {
						ControlGroupTreeItemEntry treeItemEntry = (ControlGroupTreeItemEntry) found.getValue();
						if (treeItemEntry.getMyArmaControl() == change.getAdded().getControl().getHolder()) {
							addMeWhenDone.add(found);
							return true;
						}
					}
					return false;
				}
			});

			while (addMeWhenDone.size() > 0) {
				TreeItem<T> parent = addMeWhenDone.removeFirst();
				parent.getChildren().add(change.getAdded().getIndex(), newTreeItem);
				if (change.getAdded().getControl() instanceof ArmaControlGroup) {
					addControls(newTreeItem, ((ArmaControlGroup) change.getAdded().getControl()).getControls());
				}
			}
		}

		private void handleRemove(final ControlListChange<ArmaControl> change) {
			final LinkedList<TreeItem<T>> removeMeWhenDone = new LinkedList<>();
			/*
			Search for the TreeItem that holds the control that was removed
			*/
			TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<T>() {

				@Override
				public boolean found(TreeItem<T> found) {
					if (found.getValue() instanceof ControlTreeItemEntry) {
						ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) found.getValue();
						if (treeItemEntry.getMyArmaControl() == change.getRemoved().getControl()) {
							removeMeWhenDone.add(found);
							return true;
						}
					}
					return false;
				}
			});

			while (removeMeWhenDone.size() > 0) {
				TreeItem<T> toRemove = removeMeWhenDone.removeFirst();
				//remove the matched tree item
				toRemove.getParent().getChildren().remove(toRemove);
				if (toRemove.getValue() instanceof ControlGroupTreeItemEntry) {
					ControlGroupTreeItemEntry removeTreeItemEntry = (ControlGroupTreeItemEntry) toRemove.getValue();
					//don't need this listener anymore and would be wrong to keep it
					removeListeners(removeTreeItemEntry.getControlGroup().getControls());
				}
			}
		}

		private void handleSet(final ControlListChange<ArmaControl> change) {
			final LinkedList<Pair<TreeItem<T>, Integer>> setMeWhenDone = new LinkedList<>();
			/*
			Search for the TreeItem that holds the control that was modified.
			*/
			TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<T>() {

				@Override
				public boolean found(TreeItem<T> found) {
					if (found.getValue() instanceof ControlTreeItemEntry) {
						ControlTreeItemEntry treeItemEntry = (ControlTreeItemEntry) found.getValue();
						if (treeItemEntry.getMyArmaControl() == change.getSet().getOldControl()) {
							setMeWhenDone.add(new Pair<>(found.getParent(), found.getParent().getChildren().indexOf(found))); //tree view index != control index in list
							return true;
						}
					}
					return false;
				}
			});

			while (setMeWhenDone.size() > 0) {
				Pair<TreeItem<T>, Integer> removed = setMeWhenDone.removeFirst();
				//update the old tree item to a new one with the correct control
				TreeItem<T> set = removed.getKey().getChildren().set(removed.getValue(), createTreeItemForControl(change.getSet().getNewControl()));
				if (set.getValue() instanceof ControlGroupTreeItemEntry) {
					ControlGroupTreeItemEntry groupTreeItemEntry = (ControlGroupTreeItemEntry) set.getValue();
					removeListeners(groupTreeItemEntry.getControlGroup().getControls());
				}
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

	/**Get either the display's background controls ({@link #backgroundControlEditor} == true) or the display main controls ({@link #backgroundControlEditor} == false).*/
	private ControlList<ArmaControl> getTargetControlList() {
		return backgroundControlEditor ? editingDisplay.getBackgroundControls() : editingDisplay.getControls();
	}

	/**
	 Set whether or not this tree view's display control/background control listener is activated. The listener is required to be disabled when a new tree item is being inserted into the tree
	 because if it wasn't, the tree view would get 2 tree items inserted
	 */
	private void setDisplayListener(boolean activate) {
		if (activate) {
			editingDisplay.getBackgroundControls().addChangeListener(controlListChangeListener);
			editingDisplay.getControls().addChangeListener(controlListChangeListener);
		} else {
			editingDisplay.getBackgroundControls().removeChangeListener(controlListChangeListener);
			editingDisplay.getControls().removeChangeListener(controlListChangeListener);
		}
	}

	/** Set whether or not the given control group's controls listener is activated */
	private void setControlGroupListener(boolean activate, ArmaControlGroup group) {
		if (activate) {
			group.getControls().addChangeListener(controlListChangeListener);
		} else {
			group.getControls().removeChangeListener(controlListChangeListener);
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
			public boolean found(TreeItem<T> found) {
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
				return false; //iterate through entire tree view
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
		addControls(getRoot(), getTargetControlList());
	}

	private void addControls(TreeItem<T> parentTreeItem, ControlList<ArmaControl> controls) {
		for (ArmaControl control : controls) {
			if (control instanceof ArmaControlGroup) {
				addControls(createTreeItemForControl(control), ((ArmaControlGroup) control).getControls());
			} else {
				parentTreeItem.getChildren().add(createTreeItemForControl(control)); //faster than addChildToParent()
			}
		}
	}

	/**Removes the control list listener from the list*/
	private void removeListeners(ControlList<ArmaControl> controls) {
		controls.removeChangeListener(controlListChangeListener);
		for (ArmaControl control : controls) {
			if (control instanceof ArmaControlGroup) {
				ArmaControlGroup group = ((ArmaControlGroup) control);
				removeListeners(group.getControls());
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
			if (!group.getControlGroup().getControls().contains(childControlEntry.getMyArmaControl())) {
				correctedIndex = getCorrectedIndex(groupTreeItem, child);
				setControlGroupListener(false, group.getControlGroup());
				group.getControlGroup().getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
				setControlGroupListener(true, group.getControlGroup());
			}
		} else { //didn't go into a control group, so it is in a folder or in the root's children.
			if (!getTargetControlList().contains(childControlEntry.getMyArmaControl())) {
				setDisplayListener(false);
				correctedIndex = getCorrectedIndex(getRoot(), child);
				getTargetControlList().add(correctedIndex, childControlEntry.getMyArmaControl());
				setDisplayListener(true);
			}
		}

	}

	@Override
	protected void addChildToRoot(@NotNull TreeItem<T> child) {
		super.addChildToRoot(child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		if (!getTargetControlList().contains(childControlEntry.getMyArmaControl())) {
			setDisplayListener(false);
			getTargetControlList().add(childControlEntry.getMyArmaControl());
			setDisplayListener(true);
		}
	}

	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<T> child) {
		super.addChildToRoot(index, child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		if (!getTargetControlList().contains(childControlEntry.getMyArmaControl())) {
			setDisplayListener(false);
			int correctedIndex = getCorrectedIndex(getRoot(), child);
			getTargetControlList().add(correctedIndex, childControlEntry.getMyArmaControl());
			setDisplayListener(true);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void moveTreeItem(@NotNull TreeItem<T> toMove, @NotNull TreeItem<T> newParent, int index) {
		super.moveTreeItem(toMove, newParent, index);
		if (toMove.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry controlEntry = (ControlTreeItemEntry) toMove.getValue();

		ControlGroupTreeItemEntry group = null;
		TreeItem<ControlGroupTreeItemEntry> groupTreeItem;
		if (newParent.getValue() instanceof ControlGroupTreeItemEntry) {
			groupTreeItem = (TreeItem<ControlGroupTreeItemEntry>) newParent;
			group = (ControlGroupTreeItemEntry) newParent.getValue();
		} else {
			groupTreeItem = getAncestorOfEntryType(newParent, ControlGroupTreeItemEntry.class);
			if (groupTreeItem != null) {
				group = groupTreeItem.getValue();
			}
		}
		if (group == null) {
			setDisplayListener(false);
			int correctedIndex = getCorrectedIndex(getRoot(), toMove);
			getTargetControlList().move(controlEntry.getMyArmaControl(), correctedIndex);

			setDisplayListener(true);
		} else {
			int correctedIndex = getCorrectedIndex(groupTreeItem, toMove);
			setControlGroupListener(false, group.getControlGroup());
			group.getControlGroup().getControls().move(controlEntry.getMyArmaControl(), correctedIndex);
			setControlGroupListener(true, group.getControlGroup());
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
		ControlGroupTreeItemEntry group = null;
		TreeItem<ControlGroupTreeItemEntry> groupTreeItem;
		if (parent.getValue() instanceof ControlGroupTreeItemEntry) {
			group = (ControlGroupTreeItemEntry) parent.getValue();
		} else {
			groupTreeItem = getAncestorOfEntryType(parent, ControlGroupTreeItemEntry.class);
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
			getTargetControlList().remove(toRemoveControlEntry.getMyArmaControl());
			setDisplayListener(true);
		}
		if (toRemoveControlEntry instanceof ControlGroupTreeItemEntry) {
			ControlGroupTreeItemEntry groupTreeItemEntry = (ControlGroupTreeItemEntry) toRemoveControlEntry;
			removeListeners(groupTreeItemEntry.getControlGroup().getControls());
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
	 @param parent parent of where to get corrected index from. This should either be a control group or the root. If null, the root will be used
	 @param childAdded child that was added to the parent (must already be added to parent)
	 */
	protected int getCorrectedIndex(@Nullable TreeItem parent, @NotNull TreeItem<T> childAdded) {
		if(parent != null && !(parent.getValue() instanceof ControlGroupTreeItemEntry) && parent != getRoot()){
			throw new IllegalArgumentException("parent is not the root or a control group");
		}
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
					break; //break switch
				}
				case COMPOSITE: {
					size += 1;
					break; //break switch
				}
				case LEAF: {
					size += 1;
					break; //break switch
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
