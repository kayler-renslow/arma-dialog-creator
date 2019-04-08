package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.canvas.DeepUINodeIterable;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlGroup;
import com.armadialogcreator.control.FolderUINode;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.util.Key;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 Houses the actual tree view for showing what controls and folders exist in the current project. This class is the link between the project's display and the tree view's data.
 Anything that happens to the gui tree view will echo through the display and vice versa through this class.

 @author Kayler
 @since 06/08/2016. */
public class EditorComponentTreeView extends EditableTreeView<ArmaControl, UINodeTreeItemData> {

	// Assign a globally unique id to each TREE_ITEM_KEY
	// because Key checks equivalence on the key's name rather than the memory address
	private static int id = 0;
	private final Key<TreeItem> TREE_ITEM_KEY = new Key<>("EditorComponentTreeView.TreeItemKey" + (id++));

	private final ContextMenu controlCreationContextMenu = new EditorTreeViewContextMenu(this);
	private UINode rootUINode;

	public EditorComponentTreeView(@NotNull UINode rootUINode) {
		super(null);

		this.rootUINode = rootUINode;

		setContextMenu(controlCreationContextMenu);
		EditorComponentTreeView treeView = this;
		getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<UINodeTreeItemData>> observable, TreeItem<UINodeTreeItemData> oldValue, TreeItem<UINodeTreeItemData> selected) {
				if (selected != null) {
					if (selected.getValue() instanceof ControlTreeItemEntry) {
						treeView.setContextMenu(new EditorTreeViewEditContextMenu(EditorComponentTreeView.this, (ControlTreeItemEntry) selected.getValue()));
						return;
					} else if (selected.getValue() instanceof FolderTreeItemEntry) {
						treeView.setContextMenu(new EditorTreeViewEditContextMenu(EditorComponentTreeView.this, (FolderTreeItemEntry) selected.getValue()));
						return;
					}
				}
				treeView.setContextMenu(controlCreationContextMenu);
			}
		});
	}

	/**
	 Sets the selection such that only the given controls are selected

	 @param controlList list of controls to select
	 */
	public void setSelectedControls(@NotNull List<ArmaControl> controlList) {
		getSelectionModel().clearSelection();

		for (ArmaControl control : controlList) {
			if (control.getRootNode() != rootUINode) {
				continue;
			}
			TreeItem<UINodeTreeItemData> tiFromKey = TREE_ITEM_KEY.get(control.getUserData());
			if (tiFromKey != null) {
				getSelectionModel().select(tiFromKey);
			} else {
				throw new IllegalStateException("couldn't get control's tree item for " + control.getClassName());
			}
		}
	}

	public void setToUINode(@NotNull UINode node) {
		//remove previous data stuff
		rootUINode.getUserData().put(TREE_ITEM_KEY, null);

		clear();

		rootUINode = node;
		TreeItem<UINodeTreeItemData> root = getRoot();
		rootUINode.getUserData().put(TREE_ITEM_KEY, root);
		root.setValue(new RootUINodeTreeItemData(node));

		addAllChildNodes(node, root);
	}

	@SuppressWarnings("unchecked")
	private void addAllChildNodes(@NotNull UINode startParentNode, @NotNull TreeItem parentTreeItem) {
		TreeItem parent = parentTreeItem;
		UINode parentUINode = startParentNode;
		DeepUINodeIterable.MyIterator iterator = startParentNode.deepIterateChildren().iterator();
		int currentDepth = iterator.getDepth();
		while (iterator.hasNext()) {
			UINode node = iterator.next();
			final boolean goneDeeper = currentDepth < iterator.getDepth();
			final boolean goneUp = currentDepth > iterator.getDepth();
			TreeItem item = createTreeItem(node);
			node.getUserData().put(TREE_ITEM_KEY, item);
			parent.getChildren().add(item);
			if (goneDeeper) {
				parent = item;
				parentUINode = node;
			} else if (goneUp) {
				parent = item.getParent();
				parentUINode = parentUINode.getParentNode();
				if (parentUINode == null) {
					throw new IllegalStateException();
				}
			}
		}
	}

	@NotNull
	private TreeItem createTreeItem(@NotNull UINode node) {
		if (node instanceof FolderUINode) {
			FolderUINode folder = ((FolderUINode) node);
			return new TreeItem<>(new FolderTreeItemEntry(folder));
		} else if (node instanceof ArmaControlGroup) {
			ArmaControlGroup group = (ArmaControlGroup) node;
			return new TreeItem<>(new ControlGroupTreeItemEntry(group));
		} else if (node instanceof ArmaControl) {
			ArmaControl control = (ArmaControl) node;
			return new TreeItem<>(new ControlTreeItemEntry(control));
		}
		throw new UnsupportedOperationException(node.getClass().getName());
	}

	private void setTreeItemKey(@NotNull TreeItem<UINodeTreeItemData> child) {
		TREE_ITEM_KEY.put(child.getValue().getNode().getUserData(), child);
	}

	protected void addChildToParent(@NotNull TreeItem<UINodeTreeItemData> parent, @NotNull TreeItem<UINodeTreeItemData> child, int index) {
		super.addChildToParent(parent, child, index);
		setTreeItemKey(child);
	}

	@Override
	protected void addChildToRoot(@NotNull TreeItem<UINodeTreeItemData> child) {
		super.addChildToRoot(child);
		setTreeItemKey(child);
	}

	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<UINodeTreeItemData> child) {
		super.addChildToRoot(index, child);
		setTreeItemKey(child);
	}

	@Override
	public void moveTreeItem(@NotNull TreeItem<UINodeTreeItemData> toMove, @NotNull TreeItem<UINodeTreeItemData> newParent, int index) {
		super.moveTreeItem(toMove, newParent, index);
	}

	@Override
	protected void removeChild(@NotNull TreeItem<UINodeTreeItemData> parent, @NotNull TreeItem<UINodeTreeItemData> toRemove) {
		super.removeChild(parent, toRemove);
		TREE_ITEM_KEY.put(toRemove.getValue().getNode().getUserData(), null);
	}

	private static class RootUINodeTreeItemData extends UINodeTreeItemData {

		public RootUINodeTreeItemData(@NotNull UINode node) {
			super("", null, node);
		}

		@Override
		public void duplicate(@NotNull TreeView<? extends UINodeTreeItemData> treeView) {

		}
	}
}
