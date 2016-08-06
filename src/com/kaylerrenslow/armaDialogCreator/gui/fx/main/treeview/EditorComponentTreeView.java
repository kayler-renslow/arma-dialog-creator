package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.FoundChild;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeUtil;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
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
public class EditorComponentTreeView<T extends TreeItemEntry> extends EditableTreeView<T> {
	
	private final ContextMenu controlCreationContextMenu = new ControlCreationContextMenu(this, true);
	
	public EditorComponentTreeView() {
		super(null);
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
		addControls(getRoot(), null, display.getControls());
	}
	
	private void addControls(TreeItem<T> parentTreeItem, ArmaControl parent, Iterable<ArmaControl> controls) {
		for (ArmaControl control : controls) {
			if (control instanceof ArmaControlGroup) {
				addControls(getTreeData(parent), control, ((ArmaControlGroup) control).getControls());
			} else {
				addChildToParent2(parentTreeItem, getTreeData(control), parentTreeItem.getChildren().size(), false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private TreeItem<T> getTreeData(ArmaControl control){
		return new TreeItem<T>(control instanceof ArmaControlGroup ? (T) new ControlGroupTreeItemEntry((ArmaControlGroup) control) : (T) new ControlTreeItemEntry(control));
	}
	
	private void addChildToParent2(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> child, int index, boolean addToDisplay) {
		super.addChildToParent(parent, child, index);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		if (addToDisplay) {
			int correctedIndex;
			ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
			ArmaDisplay display = ArmaDialogCreator.getApplicationData().getCurrentProject().getEditingDisplay();
			
			ControlGroupTreeItemEntry group = null;
			TreeItem<? extends TreeItemEntry> groupTreeItem;
			if (parent.getValue() instanceof ControlGroupTreeItemEntry) {
				groupTreeItem = parent;
				group = (ControlGroupTreeItemEntry) parent.getValue();
			} else {
				groupTreeItem = getAncestorOfEntryType(parent, ControlGroupTreeItemEntry.class);
				if (groupTreeItem != null) {
					group = (ControlGroupTreeItemEntry) groupTreeItem.getValue();
				}
			}
			
			if (group != null) {
				correctedIndex = getCorrectedIndex(getRow((TreeItem<T>) groupTreeItem) + 1, getRow(child), parent);
				group.getControlGroup().addControl(correctedIndex, childControlEntry.getMyArmaControl());
			} else { //didn't go into a control group, so it is in a folder.
				correctedIndex = getCorrectedIndex(0, getRow(child), parent);
				display.addControl(childControlEntry.getMyArmaControl()); //was added in a folder
			}
			display.getUpdateListenerGroup().update(null);
			System.out.println("EditorComponentTreeView.addChildToParent correctedIndex = " + correctedIndex);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addChildToParent(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> child, int index) {
		addChildToParent2(parent, child, index, true);
	}
	
	@Override
	protected void addChildToRoot(@NotNull TreeItem<T> child) {
		super.addChildToRoot(child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		System.out.println("EditorComponentTreeView.addChildToRoot getRow = " + getRow(child));
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getCurrentProject().getEditingDisplay();
		display.addControl(childControlEntry.getMyArmaControl());
		display.getUpdateListenerGroup().update(null);
	}
	
	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<T> child) {
		super.addChildToRoot(index, child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		System.out.println("EditorComponentTreeView.addChildToRoot2");
		int correctedIndex = getCorrectedIndex(0, getRow(child), getRoot());
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getCurrentProject().getEditingDisplay();
		display.addControl(childControlEntry.getMyArmaControl());
		display.getUpdateListenerGroup().update(null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void removeChild(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> toRemove) {
		super.removeChild(parent, toRemove);
		if (toRemove.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry toRemoveControlEntry = (ControlTreeItemEntry) toRemove.getValue();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getCurrentProject().getEditingDisplay();
		if (parent == getRoot()) {
			display.removeControl(toRemoveControlEntry.getMyArmaControl());
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
			group.getControlGroup().removeControl(toRemoveControlEntry.getMyArmaControl());
		} else {
			display.removeControl(toRemoveControlEntry.getMyArmaControl());
		}
		display.getUpdateListenerGroup().update(null);
		
	}
	
	private int getCorrectedIndex(int rowOriginStart, int row, TreeItem<T> start) {
		//get row after insertion. traverse upwards and count how many folders there are and subtract that from row
		System.out.println("EditorComponentTreeView.getCorrectedIndex row = " + row);
		int correctedIndex = row - rowOriginStart; //index such that the folders weren't used to calculate index
		TreeItem<T> cursor = start;
		while (cursor != getRoot() && cursor.getValue().getCellType() == CellType.FOLDER) {
			for (TreeItem<T> child : cursor.getChildren()) {
				if (child.getValue().getCellType() == CellType.FOLDER && getRow(child) < row) { //only subtract the folders preceding the row
					System.out.println("EditorComponentTreeView.getCorrectedIndex child = " + child);
					correctedIndex--;
				}
			}
			cursor = cursor.getParent();
		}
		for (TreeItem<T> child : cursor.getChildren()) {
			if (child.getValue().getCellType() == CellType.FOLDER && getRow(child) < row) { //only subtract the folders preceding the row
				correctedIndex--;
			}
		}
		
		return correctedIndex;
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends TreeItemEntry> TreeItem<T> getAncestorOfEntryType(TreeItem<? extends TreeItemEntry> start, Class<T> clazz) {
		if (start.getParent() == null || start.getParent().getValue() == null) {
			return null;
		}
		if (clazz.isAssignableFrom(start.getParent().getValue().getClass())) {
			return (TreeItem<T>) start.getParent();
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
