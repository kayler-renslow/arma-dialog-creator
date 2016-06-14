package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl;
import com.kaylerrenslow.armaDialogCreator.data.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.data.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ICanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.io.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

/**
 @author Kayler
 Houses the actual tree view. This class is the link between the application data and the tree view's data.
 Anything that happens to the gui tree view will echo through the application data and vice versa through this class.
 Created on 06/08/2016. */
public class EditorComponentTreeView extends EditableTreeView {

	private MenuItem newFolder = new MenuItem("New Folder", createFolderIcon());
	private MenuItem newItem = new MenuItem("New Item");
	private MenuItem newComp = new MenuItem("New Composite");
	private ContextMenu contextMenu = new ContextMenu(newFolder, newItem, newComp);

	private static int id = 0; //delete this later on as its for testing

	public EditorComponentTreeView() {
		super(null);
		setupTreeViewContextMenu();
	}

	private void setupTreeViewContextMenu() {
		setContextMenu(contextMenu);
		TreeViewMenuItemBuilder.setNewFolderAction(this, new TreeItemDataCreator<TreeItemEntry>() {
			@Override
			public TreeItemData<TreeItemEntry> createNew(CellType cellType) {
				return new FolderTreeItem("New Folder");
			}
		}, newFolder);

		TreeViewMenuItemBuilder.setNewItemAction(this, new TreeItemDataCreator<TreeItemEntry>() {

			@Override
			public TreeItemData<TreeItemEntry> createNew(CellType cellType) {
				ICanvasView canvasView = ArmaDialogCreator.getCanvasView();
				StaticControl control = new StaticControl("static_control" + id, 0, ControlStyle.CENTER, 0, 0, 1, 1, canvasView.getCurrentResolution());
				id++;
				ControlTreeItemEntry entry = new ControlTreeItemEntry(control);
				ApplicationDataManager.applicationData.treeViewData.addTreeItemEntry(entry);

				canvasView.getUiCanvasEditor().addControl(control);
				return new ControlTreeItem(entry);
			}
		}, newItem);
		//		TreeViewMenuItemBuilder.setNewCompositeItemAction(this, newComp, "Composite Item", new Object(), null);
	}



	static ImageView createFolderIcon() {
		return new ImageView(ImagePaths.ICON_FOLDER);
	}

}
