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
	protected void addChildToParent(@NotNull TreeItem<TreeItemEntry> parent, @NotNull TreeItem<TreeItemEntry> child, int index) {
		super.addChildToParent(parent, child, index);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		int correctedIndex = getCorrectedIndex(getRow(child), parent);
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		System.out.println("EditorComponentTreeView.addChildToParent correctedIndex = " + correctedIndex);
		display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl()); //was added in a folder
	}

	@Override
	protected void addChildToRoot(@NotNull TreeItem<TreeItemEntry> child) {
		super.addChildToRoot(child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		System.out.println("EditorComponentTreeView.addChildToRoot getRow = " + getRow(child));
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		display.getControls().add(childControlEntry.getMyArmaControl());
	}

	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<TreeItemEntry> child) {
		super.addChildToRoot(index, child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		System.out.println("EditorComponentTreeView.addChildToRoot2");
		int correctedIndex = getCorrectedIndex(getRow(child), getRoot());
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

	private int getCorrectedIndex(int row, TreeItem<TreeItemEntry> start) {
		//get row after insertion. traverse upwards and count how many folders there are and subtract that from row
		System.out.println("EditorComponentTreeView.getCorrectedIndex row = " + row);
		int correctedIndex = row; //index such that the folders weren't used to calculate index

		TreeItem<TreeItemEntry> cursor = start;
		while (cursor != getRoot() && cursor.getValue().getCellType() == CellType.FOLDER) {
			correctedIndex--;
			for (TreeItem<TreeItemEntry> child : cursor.getChildren()) {
				if (child.getValue().getCellType() == CellType.FOLDER && getRow(child) < row) { //only subtract the folders preceding the row
					System.out.println("EditorComponentTreeView.getCorrectedIndex child = " + child);
					correctedIndex--;
				}
			}
			cursor = cursor.getParent();
		}
		for (TreeItem<TreeItemEntry> child : cursor.getChildren()) {
			if (child.getValue().getCellType() == CellType.FOLDER && getRow(child) < row) { //only subtract the folders preceding the row
				correctedIndex--;
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

	static ImageView createCompositeIcon() {
		return new ImageView(ImagePaths.ICON_COMPOSITE);
	}

}
