package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.io.ApplicationDataManager;
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
		super.addChildToParent(parent, child);
		ArmaDisplay display = ApplicationDataManager.applicationData.getEditingDisplay();
		//step through 
		TreeUtil.stepThroughDescendants(getRoot(), new FoundChild<TreeItemData<TreeItemEntry>>() {
			@Override
			public void found(TreeItem<TreeItemData<TreeItemEntry>> found) {
				if (found != parent) {
					return;
				}
				if (found.getValue().getData() instanceof ControlTreeItemEntry) {
					ControlTreeItemEntry entry = (ControlTreeItemEntry) found.getValue().getData();
					for (ArmaControl control : display.getControls()) {
						if (entry.getMyArmaControl() == control) {

						}
					}
				}
			}
		});
	}

	@Override
	protected void addChildToRoot(@NotNull TreeItem<TreeItemData<TreeItemEntry>> item) {
		super.addChildToRoot(item);
	}

	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<TreeItemData<TreeItemEntry>> item) {
		super.addChildToRoot(index, item);
	}

	@Override
	protected void removeChild(@NotNull TreeItem<TreeItemData<TreeItemEntry>> parent, @NotNull TreeItem<TreeItemData<TreeItemEntry>> toRemove) {
		super.removeChild(parent, toRemove);
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
				CanvasView canvasView = ArmaDialogCreator.getCanvasView();
				StaticControl control = new StaticControl("static_control" + id, 0, ControlStyle.CENTER, 0, 0, 1, 1, canvasView.getCurrentResolution());
				id++;
				ControlTreeItemEntry entry = new ControlTreeItemEntry(control);
				ApplicationDataManager.applicationData.treeViewData.addTreeItemEntry(entry);

				canvasView.getUiCanvasEditor().addControl(control); //TODO removed getUICanvasEditor from api
				return new ControlTreeItemData(entry);
			}
		}, newItem);
		//		TreeViewMenuItemBuilder.setNewCompositeItemAction(this, newComp, "Composite Item", new Object(), null);
	}


	static ImageView createFolderIcon() {
		return new ImageView(ImagePaths.ICON_FOLDER);
	}

}
