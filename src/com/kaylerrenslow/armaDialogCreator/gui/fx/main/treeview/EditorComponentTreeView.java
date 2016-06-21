package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.FoundChild;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeUtil;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 Houses the actual tree view. This class is the link between the application data and the tree view's data.
 Anything that happens to the gui tree view will echo through the application data and vice versa through this class.
 Created on 06/08/2016. */
public class EditorComponentTreeView extends EditableTreeView<TreeItemEntry> {

	public EditorComponentTreeView() {
		super(null);
		setContextMenu(new ControlCreationContextMenu(this, true));
	}

	/**
	 Sets the selection such that only the given controls are selected

	 @param controlList list of controls to select
	 */
	public void setSelectedControls(List<ArmaControl> controlList) {
		getSelectionModel().clearSelection();
		TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<TreeItemEntry>() {
			@Override
			public void found(TreeItem<TreeItemEntry> found) {
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

	@Override
	protected void addChildToParent(@NotNull TreeItem<TreeItemEntry> parent, @NotNull TreeItem<TreeItemEntry> child) {
		addChildToParent(parent, child, parent.getChildren().size());
	}

	@Override
	protected void addChildToParent(@NotNull TreeItem<TreeItemEntry> parent, @NotNull TreeItem<TreeItemEntry> child, int index) {
		int correctedIndex = getCorrectedIndex(index, parent); //needs to come before invoking super method
		super.addChildToParent(parent, child, index);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		if (parent == getRoot()) {
			display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
		} else {
			ControlGroupTreeItemEntry groupAncestor = getAncestorOfEntryType(parent, ControlGroupTreeItemEntry.class);
			if (groupAncestor != null) {
				groupAncestor.getControlGroup().getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
			} else { //didn't go into a control group, so it is in a folder. Therefore, the correctedIndex is relative to the root
				LinkedList<TreeItem<TreeItemEntry>> children = new LinkedList<>();
				children.addAll(parent.getChildren());
				correctedIndex = 0;
				while (!children.isEmpty()) {
					child = children.removeFirst();
					children.addAll(child.getChildren());
					if (child.getValue().getCellType() != CellType.FOLDER) {
						correctedIndex++;
					}
				}
				System.out.println("EditorComponentTreeView.addChildToParent correctedIndex = " + correctedIndex);
				//TODO fix corrected index as it is wrong when child goes into a folder of folder
				if (display.getControls().size() == 0 && correctedIndex == 1) {
					display.getControls().add(childControlEntry.getMyArmaControl());
				} else {
					display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl()); //was added in a folder
				}
			}
		}
	}

	@Override
	protected void addChildToRoot(@NotNull TreeItem<TreeItemEntry> child) {
		super.addChildToRoot(child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		display.getControls().add(childControlEntry.getMyArmaControl());
	}

	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<TreeItemEntry> child) {
		int correctedIndex = getCorrectedIndex(index, getRoot());
		super.addChildToRoot(index, child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
	}

	@Override
	protected void removeChild(@NotNull TreeItem<TreeItemEntry> parent, @NotNull TreeItem<TreeItemEntry> toRemove) {
		super.removeChild(parent, toRemove);
		if (toRemove.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry toRemoveControlEntry = (ControlTreeItemEntry) toRemove.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		if (parent == getRoot()) {
			display.getControls().remove(toRemoveControlEntry.getMyArmaControl());
			return;
		}

		ControlGroupTreeItemEntry groupAncestor = getAncestorOfEntryType(parent, ControlGroupTreeItemEntry.class);
		if (groupAncestor != null) {
			groupAncestor.getControlGroup().getControls().remove(toRemoveControlEntry.getMyArmaControl());
		} else {
			display.getControls().remove(toRemoveControlEntry.getMyArmaControl());
		}

	}

	private int getCorrectedIndex(int index, TreeItem<TreeItemEntry> parent) {
		int correctedIndex = 0; //index such that the folders weren't used to calculate index
		int currentIndex = 0;

		TreeItem<TreeItemEntry> cursor = parent;
		while (cursor != getRoot() && cursor.getValue().getCellType() == CellType.FOLDER) {
			for (TreeItem<TreeItemEntry> cursorChild : cursor.getChildren()) {
				if (cursorChild.getValue().getCellType() != CellType.FOLDER) {
					correctedIndex++;
				}
			}
			cursor = cursor.getParent();
		}

		LinkedList<TreeItem<TreeItemEntry>> children = new LinkedList<>();
		children.addAll(parent.getChildren());
		TreeItem<TreeItemEntry> child;
		while (!children.isEmpty()) {
			child = children.removeFirst();
			children.addAll(child.getChildren());
			if (child.getValue().getCellType() != CellType.FOLDER) {
				correctedIndex++;
			}
			currentIndex++;
			if (currentIndex == index) {
				break;
			}
		}

		return correctedIndex;
	}

	@SuppressWarnings("unchecked")
	private static <T extends TreeItemEntry> T getAncestorOfEntryType(TreeItem<TreeItemEntry> start, Class<T> clazz) {
		if (start.getParent() == null) {
			return null;
		}
		if (clazz.isInstance(start.getParent().getValue())) {
			return (T) start.getValue();
		}
		return getAncestorOfEntryType(start.getParent(), clazz);
	}


	static ImageView createFolderIcon() {
		return new ImageView(ImagePaths.ICON_FOLDER);
	}

	static ImageView createCompositeIcon(){
		return new ImageView(ImagePaths.ICON_COMPOSITE);
	}


}
