package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.canvas.DeepUINodeIterable;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.canvas.UINodeChange;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlGroup;
import com.armadialogcreator.control.FolderUINode;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.img.icons.ADCIcons;
import com.armadialogcreator.util.Key;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 Houses the actual tree view for showing what controls and folders exist in the current project. This class is the link between the project's display and the tree view's data.
 Anything that happens to the gui tree view will echo through the display and vice versa through this class.

 @author Kayler
 @since 06/08/2016. */
public class EditorComponentTreeView<T extends UINodeTreeItemData> extends EditableTreeView<ArmaControl, T> {

	private static final Key<TreeItem> TREE_ITEM_KEY = new Key<>("EditorComponentTreeView.TreeItemKey");

	private final ContextMenu controlCreationContextMenu = new EditorTreeViewContextMenu(this);
	private UINode rootUINode;
	private boolean disableListener = false;
	private UpdateGroupListener<UINodeChange> uiNodeChangeListener = new UpdateGroupListener<>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<UINodeChange> group, @NotNull UINodeChange data) {
			if (disableListener) {
				return;
			}
			UINode updatedNode = data.getNode();
			switch (data.getType()) {
				case AddChild: {
					System.out.println("EditorComponentTreeView.update updatedNode.getClass().getName();=" + updatedNode.getClass().getName());
					UINode parentNode = updatedNode.getParentNode();
					System.out.println("EditorComponentTreeView.update parentNode.getClass().getName()=" + parentNode.getClass().getName());
					if (parentNode == null) {
						throw new IllegalStateException();
					}
					TreeItem parent = TREE_ITEM_KEY.get(parentNode.getUserData());
					System.out.println("EditorComponentTreeView.update parent=" + parent);
					if (parent == null) {
						UINode ancestorNode = parentNode;
						UINode rootNode = rootUINode;
						while (parent == null && ancestorNode != rootNode) {
							ancestorNode = ancestorNode.getParentNode();
							if (ancestorNode == null) {
								break;
							}
							parent = TREE_ITEM_KEY.get(ancestorNode.getUserData());
						}
						if (parent == null) {
							if (ancestorNode != null) {
								addAllChildNodes(ancestorNode, getRoot());
							}
							break;
						}
					}
					UINodeChange.AddChild change = (UINodeChange.AddChild) data;
					addMissingNode(updatedNode, parent, change.getIndex());
					break;
				}
				case MoveChild: {
					UINodeChange.MoveChild change = (UINodeChange.MoveChild) data;
					UINode oldParentNode = change.getOldParent();
					UINode newParentNode = change.getNewParent();
					TreeItem<T> oldParent = TREE_ITEM_KEY.get(oldParentNode.getUserData());
					if (oldParent != null) {
						oldParent.getParent().getChildren().remove(oldParent);
					}
					TreeItem<T> newParent = TREE_ITEM_KEY.get(newParentNode.getUserData());
					if (newParent == null) {
						UINode ancestorNode = newParentNode;
						while (newParent == null && ancestorNode != ancestorNode.getRootNode()) {
							ancestorNode = ancestorNode.getParentNode();
							if (ancestorNode == null) {
								throw new IllegalStateException();
							}
							newParent = TREE_ITEM_KEY.get(ancestorNode.getUserData());
						}
						if (newParent == null) {
							addAllChildNodes(ancestorNode, getRoot());
							break;
						}
						addMissingNode(updatedNode, newParent, change.getDestIndex());
					}

					break;
				}
				case RemoveChild: {
					TreeItem<T> treeItem = TREE_ITEM_KEY.get(updatedNode.getUserData());
					if (treeItem == null) {
						return;
					}
					treeItem.getParent().getChildren().remove(treeItem);
					updatedNode.getUserData().put(TREE_ITEM_KEY, null);
					for (UINode child : updatedNode.deepIterateChildren()) {
						child.getUserData().put(TREE_ITEM_KEY, null);
					}
					break;
				}
				default: {
					throw new IllegalStateException(data.getType().name());
				}
			}
		}
	};

	public EditorComponentTreeView(@NotNull UINode rootUINode) {
		super(null);

		this.rootUINode = rootUINode;
		rootUINode.getUpdateGroup().addListener(uiNodeChangeListener);

		setContextMenu(controlCreationContextMenu);
		EditorComponentTreeView treeView = this;
		getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<T>> observable, TreeItem<T> oldValue, TreeItem<T> selected) {
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
	 Set whether or not this tree view's display control/background control listener is activated.
	 The listener is required to be disabled when a new tree item is being inserted into the tree
	 because if it wasn't, the tree view would get 2 tree items inserted
	 */
	private void enableNodeListener(boolean activate) {
		disableListener = !activate;
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
			TreeItem<T> tiFromKey = TREE_ITEM_KEY.get(control.getUserData());
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
		rootUINode.getUpdateGroup().removeListener(uiNodeChangeListener);

		System.out.println("EditorComponentTreeView.setToUINode node.getClass().getName()=" + node.getClass().getName());
		rootUINode = node;
		rootUINode.getUserData().put(TREE_ITEM_KEY, getRoot());
		rootUINode.getUpdateGroup().addListener(uiNodeChangeListener);

		clear();

		enableNodeListener(false);
		addAllChildNodes(node, getRoot());
		enableNodeListener(true);
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

	@SuppressWarnings("unchecked")
	private void addMissingNode(@NotNull UINode missingNode, @NotNull TreeItem parentTreeItem, int index) {
		TreeItem item = createTreeItem(missingNode);
		missingNode.getUserData().put(TREE_ITEM_KEY, item);
		if (index == -1) {
			parentTreeItem.getChildren().add(item);
		} else {
			parentTreeItem.getChildren().add(index, item);
		}
		addAllChildNodes(missingNode, item); //now add all children of missing node
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

	@Override
	protected void addChildToParent(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> child, int index) {
		super.addChildToParent(parent, child, index);
		enableNodeListener(false);
		parent.getValue().getNode().addChild(child.getValue().getNode(), index);
		enableNodeListener(true);

		child.getValue().getNode().getUserData().put(TREE_ITEM_KEY, child);
	}

	@Override
	protected void addChildToRoot(@NotNull TreeItem<T> child) {
		super.addChildToRoot(child);
		enableNodeListener(false);
		rootUINode.addChild(child.getValue().getNode());
		enableNodeListener(true);

		child.getValue().getNode().getUserData().put(TREE_ITEM_KEY, child);
	}

	@Override
	protected void addChildToRoot(int index, @NotNull TreeItem<T> child) {
		super.addChildToRoot(index, child);
		enableNodeListener(false);
		rootUINode.addChild(child.getValue().getNode(), index);
		enableNodeListener(true);

		child.getValue().getNode().getUserData().put(TREE_ITEM_KEY, child);
	}

	@Override
	public void moveTreeItem(@NotNull TreeItem<T> toMove, @NotNull TreeItem<T> newParent, int index) {
		super.moveTreeItem(toMove, newParent, index);
		UINode node = toMove.getValue().getNode();
		enableNodeListener(false);
		node.getParentNode().moveChild(node, newParent.getValue().getNode(), index);
		enableNodeListener(true);
	}

	@Override
	protected void removeChild(@NotNull TreeItem<T> parent, @NotNull TreeItem<T> toRemove) {
		super.removeChild(parent, toRemove);
		enableNodeListener(false);
		parent.getValue().getNode().removeChild(toRemove.getValue().getNode());
		toRemove.getValue().getNode().getUserData().put(TREE_ITEM_KEY, null);
		enableNodeListener(true);
	}

	static ImageView createFolderIcon() {
		return new ImageView(ADCIcons.ICON_FOLDER);
	}

}
