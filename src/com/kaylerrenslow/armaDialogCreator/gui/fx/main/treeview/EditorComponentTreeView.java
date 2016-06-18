package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.ControlGroupTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Houses the actual tree view. This class is the link between the application data and the tree view's data.
 Anything that happens to the gui tree view will echo through the application data and vice versa through this class.
 Created on 06/08/2016. */
public class EditorComponentTreeView extends EditableTreeView<TreeItemEntry> {

	private MenuItem newFolder = new MenuItem("New Folder", createFolderIcon());
	private MenuItem newItem = new MenuItem("New Item");
	private MenuItem newComp = new MenuItem("New Composite");
	private ContextMenu contextMenu = new ContextMenu(newFolder, newItem, newComp);

	private static int id = 0; //delete this later on as its for testing

	public EditorComponentTreeView() {
		super(null);
		setupTreeViewContextMenu();
	}

	@Override
	protected void addChildToParent(@NotNull TreeItem<TreeItemData<TreeItemEntry>> parent, @NotNull TreeItem<TreeItemData<TreeItemEntry>> child) {
		addChildToParent(parent, child, parent.getChildren().size());
	}

	@Override
	protected void addChildToParent(@NotNull TreeItem<TreeItemData<TreeItemEntry>> parent, @NotNull TreeItem<TreeItemData<TreeItemEntry>> child, int index) {
		int correctedIndex = getCorrectedIndex(index, parent); //needs to come before invoking super method
		super.addChildToParent(parent, child, index);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue().getData();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		if (parent == getRoot()) {
			display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
		} else {
			ControlGroupTreeItemEntry groupAncestor = getAncestorOfEntryType(parent, ControlGroupTreeItemEntry.class);
			if (groupAncestor != null) {
				groupAncestor.getControlGroup().getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
			} else {
				//TODO fix corrected index as it is wrong when child goes into a folder of folder
				display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl()); //was added in a folder
			}
		}
	}

	@Override
	protected void addChildToRoot(@NotNull TreeItem<TreeItemData<TreeItemEntry>> child) {
		super.addChildToRoot(child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue().getData();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		display.getControls().add(childControlEntry.getMyArmaControl());
	}

	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<TreeItemData<TreeItemEntry>> child) {
		int correctedIndex = getCorrectedIndex(index, getRoot());
		super.addChildToRoot(index, child);
		if (child.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry childControlEntry = (ControlTreeItemEntry) child.getValue().getData();
		ArmaDisplay display = ArmaDialogCreator.getApplicationData().getEditingDisplay();
		display.getControls().add(correctedIndex, childControlEntry.getMyArmaControl());
	}

	@Override
	protected void removeChild(@NotNull TreeItem<TreeItemData<TreeItemEntry>> parent, @NotNull TreeItem<TreeItemData<TreeItemEntry>> toRemove) {
		super.removeChild(parent, toRemove);
		if (toRemove.getValue().getCellType() == CellType.FOLDER) {
			return;
		}
		ControlTreeItemEntry toRemoveControlEntry = (ControlTreeItemEntry) toRemove.getValue().getData();
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

	private int getCorrectedIndex(int index, TreeItem<TreeItemData<TreeItemEntry>> start) {
		int correctedIndex = 0; //index such that the folders weren't used to calculate index
		int currentIndex = 0;
		for (TreeItem<TreeItemData<TreeItemEntry>> item : start.getChildren()) {
			if (item.getValue().getCellType() == CellType.FOLDER) {
				currentIndex++;
				continue;
			}
			if (currentIndex >= index) {
				break;
			}
			currentIndex++;
			correctedIndex++;
		}
		return correctedIndex;
	}

	private void setupTreeViewContextMenu() {
		setContextMenu(contextMenu);
		TreeViewMenuItemBuilder.setNewFolderAction(this, new TreeItemDataCreator<TreeItemEntry>() {
			@Override
			public TreeItemData<TreeItemEntry> createNew(CellType cellType) {
				return new FolderTreeItemData("New Folder");
			}
		}, newFolder);

		TreeViewMenuItemBuilder.setNewItemAction(this, new TreeItemDataCreator<TreeItemEntry>() {

			@Override
			public TreeItemData<TreeItemEntry> createNew(CellType cellType) {
				StaticControl control = new StaticControl("static_control" + id, 0, ControlStyle.CENTER, 0, 0, 1, 1, ArmaDialogCreator.getCanvasView().getCurrentResolution());
				id++;
				ControlTreeItemEntry entry = new ControlTreeItemEntry(control);
				return new ControlTreeItemData(entry);
			}
		}, newItem);
		//		TreeViewMenuItemBuilder.setNewCompositeItemAction(this, newComp, "Composite Item", new Object(), null);
	}

	private static <T extends TreeItemEntry> T getAncestorOfEntryType(TreeItem<TreeItemData<TreeItemEntry>> start, Class<T> clazz) {
		if (start.getParent() == null) {
			return null;
		}
		if (clazz.isInstance(start.getParent().getValue().getData())) {
			return (T) start.getValue().getData();
		}
		return getAncestorOfEntryType(start.getParent(), clazz);
	}


	static ImageView createFolderIcon() {
		return new ImageView(ImagePaths.ICON_FOLDER);
	}


}
