package com.kaylerrenslow.armaDialogCreator.gui.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.TreeDataToValueConverter;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.TreeUtil;
import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImages;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.*;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 Houses the actual tree view for showing what controls and folders exist in the current project. This class is the link between the project's display and the tree view's data.
 Anything that happens to the gui tree view will echo through the display and vice versa through this class.

 @author Kayler
 @since 06/08/2016. */
public class EditorComponentTreeView<T extends TreeItemEntry> extends EditableTreeView<ArmaControl, T> {

	private final Key<TreeItem<T>> TREE_ITEM_KEY = new Key<>("EditorComponentTreeView.TreeItemKey");

	private final ContextMenu controlCreationContextMenu = new EditorTreeViewContextMenu(this);
	private ArmaDisplay editingDisplay;
	private final boolean backgroundControlEditor;
	private final UpdateGroupListener<ControlListChange<ArmaControl>> controlListChangeListener = new UpdateGroupListener<ControlListChange<ArmaControl>>() {

		@Override
		public void update(@NotNull UpdateListenerGroup<ControlListChange<ArmaControl>> group, @Nullable ControlListChange<ArmaControl> change) {
			if (change == null) {
				throw new IllegalArgumentException("change is null");
			}
			if (change.wasSet()) {
				handleSet(change);
			} else if (change.wasRemoved()) {
				handleRemove(change);
			} else if (change.wasAdded()) {
				handleAdd(change);
			} else if (change.wasMoved()) {
				handleMove(change);
			} else {
				throw new IllegalStateException("unhandled change type:" + change.getChangeType());
			}
		}

		private void handleMove(ControlListChange<ArmaControl> change) {
			ControlMove<ArmaControl> moved = change.getMoved();
			ArmaControl movedControl = moved.getMovedControl();
			if (!moved.isEntryUpdate()) {
				//add to this tree view
				TreeItem<T> movedControlTreeItem = createTreeItemForControl(movedControl);
				if (movedControl.getHolder() instanceof ArmaControlGroup) {
					//insert into group
					ArmaControlGroup group = (ArmaControlGroup) movedControl.getHolder();
					TreeItem<T> groupTreeItem = TREE_ITEM_KEY.get(group.getUserData());
					if (groupTreeItem == null) {
						throw new IllegalStateException(group.getClassName());
					}
					groupTreeItem.getChildren().add(moved.getDestinationIndex(), movedControlTreeItem);
				} else {
					//insert into root
					getRoot().getChildren().add(moved.getDestinationIndex(), movedControlTreeItem);
				}
			} else {
				//remove from this tree view
				TreeItem<T> movedControlTreeItem = TREE_ITEM_KEY.get(movedControl.getUserData());
				if (movedControlTreeItem == null) {
					throw new IllegalStateException(movedControl.getClassName());
				}
				movedControlTreeItem.getParent().getChildren().remove(movedControlTreeItem);
				movedControl.getUserData().put(TREE_ITEM_KEY, null);
			}
		}

		private void handleAdd(ControlListChange<ArmaControl> change) {
			ControlAdd<ArmaControl> added = change.getAdded();
			ArmaControl newControl = added.getControl();
			TreeItem<T> newControlTreeItem = createTreeItemForControl(newControl);
			if (newControl.getHolder() instanceof ArmaControlGroup) {
				//add to group
				ArmaControlGroup group = (ArmaControlGroup) newControl.getHolder();
				TreeItem<T> groupTreeItem = TREE_ITEM_KEY.get(group.getUserData());
				if (groupTreeItem == null) {
					throw new IllegalStateException(group.getClassName());
				}
				groupTreeItem.getChildren().add(added.getIndex(), newControlTreeItem);
			} else {
				//add to root
				getRoot().getChildren().add(added.getIndex(), newControlTreeItem);
			}
		}

		private void handleRemove(ControlListChange<ArmaControl> change) {
			ControlRemove<ArmaControl> removed = change.getRemoved();
			ArmaControl removedControl = removed.getControl();
			TreeItem<T> removedTreeItem = TREE_ITEM_KEY.get(removedControl.getUserData());
			if (removedTreeItem == null) {
				throw new IllegalStateException(removedControl.getClassName());
			}
			removedTreeItem.getParent().getChildren().remove(removedTreeItem);
			removedControl.getUserData().put(TREE_ITEM_KEY, null);
		}

		private void handleSet(ControlListChange<ArmaControl> change) {
			ControlSet<ArmaControl> set = change.getSet();

			//remove old control tree item
			ArmaControl oldControl = set.getOldControl();
			TreeItem<T> oldControlTreeItem = TREE_ITEM_KEY.get(oldControl.getUserData());
			if (oldControlTreeItem == null) {
				throw new IllegalStateException(set.getOldControl().getClassName());
			}
			oldControlTreeItem.getParent().getChildren().remove(oldControlTreeItem);
			oldControl.getUserData().put(TREE_ITEM_KEY, null);

			//insert new tree item
			ArmaControl newControl = set.getNewControl();
			TreeItem<T> newControlTreeItem = createTreeItemForControl(newControl);
			if (newControl.getHolder() instanceof ArmaControlGroup) {
				//add to group
				ArmaControlGroup group = (ArmaControlGroup) newControl.getHolder();
				TreeItem<T> groupTreeItem = TREE_ITEM_KEY.get(group.getUserData());
				if (groupTreeItem == null) {
					throw new IllegalStateException(group.getClassName());
				}
				groupTreeItem.getChildren().add(set.getIndex(), newControlTreeItem);
			} else {
				//add to root
				getRoot().getChildren().add(set.getIndex(), newControlTreeItem);
			}
		}

	};


	public EditorComponentTreeView(boolean backgroundControlEditor) {
		super(null);

		setConverter(new TreeDataToValueConverter<T, ArmaControl>() {

			@Nullable
			@Override
			public ArmaControl convert(@NotNull T data) {
				if (data instanceof ControlTreeItemEntry) {
					return ((ControlTreeItemEntry) data).getMyArmaControl();
				}
				return null;
			}

			@NotNull
			@Override
			@SuppressWarnings("unchecked")
			public T convert(@Nullable ArmaControl data, boolean asFolder, @NotNull String name) {
				if (asFolder) {
					return (T) new FolderTreeItemEntry(name);
				}
				if (data == null) {
					throw new IllegalArgumentException("data is null and not a folder");
				}

				if (data instanceof ArmaControlGroup) {
					return (T) new ControlGroupTreeItemEntry((ArmaControlGroup) data);
				}

				//if this returns null, let it exception
				return (T) new ControlTreeItemEntry(data);
			}
		});

		this.backgroundControlEditor = backgroundControlEditor;
		setContextMenu(controlCreationContextMenu);
		EditorComponentTreeView treeView = this;
		getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<T>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<T>> observable, TreeItem<T> oldValue, TreeItem<T> selected) {
				if (selected != null && selected.getValue() instanceof ControlTreeItemEntry) {
					treeView.setContextMenu(new ControlEditContextMenu(EditorComponentTreeView.this, (ControlTreeItemEntry) selected.getValue()));
				} else {
					treeView.setContextMenu(controlCreationContextMenu);
				}
			}
		});
	}


	/**
	 Get either the display's background controls ({@link #backgroundControlEditor} == true)
	 or the display main controls ({@link #backgroundControlEditor} == false).
	 */
	@NotNull
	private DisplayControlList<ArmaControl> getTargetControlList() {
		return backgroundControlEditor ? editingDisplay.getBackgroundControls() : editingDisplay.getControls();
	}

	/**
	 Set whether or not this tree view's display control/background control listener is activated.
	 The listener is required to be disabled when a new tree item is being inserted into the tree
	 because if it wasn't, the tree view would get 2 tree items inserted
	 */
	private void setDisplayListener(boolean activate) {
		if (activate) {
			getTargetControlList().getUpdateGroup().addListener(controlListChangeListener);
		} else {
			getTargetControlList().getUpdateGroup().removeListener(controlListChangeListener);
		}
	}

	/**
	 Sets the selection such that only the given controls are selected

	 @param controlList list of controls to select
	 */
	public void setSelectedControls(@NotNull List<ArmaControl> controlList) {
		getSelectionModel().clearSelection();

		for (ArmaControl control : controlList) {
			TreeItem<T> tiFromKey = TREE_ITEM_KEY.get(control.getUserData());
			//make sure we are selecting a control that is in this treeview!
			if (control.isBackgroundControl() != backgroundControlEditor) {
				continue;
			}
			if (tiFromKey != null) {
				getSelectionModel().select(tiFromKey);
			} else {
				throw new IllegalStateException("couldn't get control's tree item for " + control.getClassName());
			}
		}
	}

	public void setToDisplay(@NotNull ArmaDisplay display) {
		if (this.editingDisplay != null) {
			setDisplayListener(false); //clear the old listeners on the old editing display since they are no longer needed
		}
		this.editingDisplay = display;
		setDisplayListener(true);
		getRoot().getChildren().clear();
		addControls(getRoot(), getTargetControlList());
	}

	private void addControls(@NotNull TreeItem<T> parentTreeItem, @NotNull ControlList<ArmaControl> controls) {
		for (ArmaControl control : controls) {
			if (control instanceof ArmaControlGroup) {
				addControls(createTreeItemForControl(control), ((ArmaControlGroup) control).getControls());
			} else {
				parentTreeItem.getChildren().add(createTreeItemForControl(control)); //faster than addChildToParent()
			}
		}
	}


	@SuppressWarnings("unchecked")
	private TreeItem<T> createTreeItemForControl(@NotNull ArmaControl control) {
		TreeItem<T> ti;
		if (control instanceof ArmaControlGroup) {
			ArmaControlGroup group = (ArmaControlGroup) control;
			ti = new TreeItem<>((T) new ControlGroupTreeItemEntry(group));
			for (ArmaControl c : group.getControls()) {
				ti.getChildren().add(createTreeItemForControl(c));
			}
		} else {
			ti = new TreeItem<>((T) new ControlTreeItemEntry(control));
		}

		control.getUserData().put(TREE_ITEM_KEY, ti);

		return ti;
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

		childControlEntry.getMyArmaControl().getUserData().put(TREE_ITEM_KEY, child);

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
				setDisplayListener(false);
				group.getControlGroup().getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
				setDisplayListener(true);
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
		childControlEntry.getMyArmaControl().getUserData().put(TREE_ITEM_KEY, child);

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
		childControlEntry.getMyArmaControl().getUserData().put(TREE_ITEM_KEY, child);
		if (!getTargetControlList().contains(childControlEntry.getMyArmaControl())) {
			setDisplayListener(false);
			int correctedIndex = getCorrectedIndex(getRoot(), child);
			getTargetControlList().add(correctedIndex, childControlEntry.getMyArmaControl());
			setDisplayListener(true);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public void moveTreeItem(@NotNull TreeItem<T> toMove, @NotNull TreeItem<T> newParent, int index) {
		super.moveTreeItem(toMove, newParent, index);
		if (toMove.getValue().getCellType() == CellType.FOLDER) {
			setDisplayListener(false);
			TreeUtil.stepThroughDescendants(getRoot(), found -> {
				if (!(found.getValue() instanceof ControlTreeItemEntry)) {
					return false;
				}
				ControlTreeItemEntry controlTreeItemEntry = (ControlTreeItemEntry) found.getValue();
				ArmaControl control = controlTreeItemEntry.getMyArmaControl();
				ControlList holderList = control.getHolder() == control.getDisplay() ?
						(
								//is in display
								getTargetControlList()
						)
						: control.getHolder().getControls();
				holderList.move(control, getCorrectedIndex(null, found));
				getSelectionModel().clearSelection();
				getSelectionModel().select(toMove);
				return false;
			});
			setDisplayListener(true);
			return;
		}
		ControlTreeItemEntry controlEntry = (ControlTreeItemEntry) toMove.getValue();

		ControlGroupTreeItemEntry newGroupEntry = null;
		TreeItem<ControlGroupTreeItemEntry> groupTreeItem = null;
		if (newParent.getValue() instanceof ControlGroupTreeItemEntry) {
			groupTreeItem = (TreeItem<ControlGroupTreeItemEntry>) newParent;
			newGroupEntry = groupTreeItem.getValue();
		} else {
			groupTreeItem = getAncestorOfEntryType(newParent, ControlGroupTreeItemEntry.class);
			if (groupTreeItem != null) {
				newGroupEntry = groupTreeItem.getValue();
			}
		}
		ControlGroupTreeItemEntry newParentEntry = newParent == getRoot()
				? null : newGroupEntry;
		//if newGroupEntry is null, then it is in the display

		ArmaControl armaControl = controlEntry.getMyArmaControl();
		ControlList holderList = armaControl.getHolder() == armaControl.getDisplay() ?
				(
						//is in display
						getTargetControlList()
				)
				: armaControl.getHolder().getControls();
		ControlList destinationList = newParentEntry == null ?
				(
						//is display
						getTargetControlList()
				)
				: newParentEntry.getControlGroup().getControls();

		setDisplayListener(false);
		int correctedIndex = getCorrectedIndex(groupTreeItem == null ? getRoot() : groupTreeItem, toMove);
		holderList.move(armaControl, destinationList, correctedIndex);
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

		ArmaControl removeControl = toRemoveControlEntry.getMyArmaControl();
		TreeItem<T> removeControlTreeItem = TREE_ITEM_KEY.get(removeControl.getUserData());
		if (removeControlTreeItem == null) {
			throw new IllegalStateException(removeControl.getClassName());
		}
		setDisplayListener(false);
		if (removeControl.getHolder() == removeControl.getDisplay()) {
			getTargetControlList().remove(removeControl);
		} else {
			removeControl.getHolder().getControls().remove(removeControl);
		}
		removeControl.getUserData().put(TREE_ITEM_KEY, null);
		setDisplayListener(true);
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
		return new ImageView(ADCImages.ICON_FOLDER);
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
