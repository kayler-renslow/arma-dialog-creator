package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Creates a new EditableTreeView with a root node already in place. This class extends javafx.scene.control.TreeView of type TreeItemData */
public class EditableTreeView<E> extends javafx.scene.control.TreeView<TreeItemData<E>> {

	public EditableTreeView(@Nullable ITreeCellSelectionUpdate selectionUpdate) {
		super(new MoveableTreeItem());
		this.showRootProperty().set(false);

		this.setEditable(true);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setCellSelectionUpdate(selectionUpdate);
	}

	public void setCellSelectionUpdate(@Nullable ITreeCellSelectionUpdate selectionUpdate) {
		setCellFactory(new TreeFactoryGen<>(new EditableTreeCellFactory(selectionUpdate)));
	}


	/**
	 Adds a new TreeItem to the tree.

	 @param data data inside tree node
	 */
	public void addChildToRoot(TreeItemData<E> data) {
		TreeItem<TreeItemData<E>> item;
		if (data.getCellType() == CellType.FOLDER) {
			item = createFolder(data);
		} else if (data.getCellType() == CellType.COMPOSITE) {
			item = createComposite(data);
		} else {
			item = new MoveableTreeItem<E>(data);
		}

		addChildToRoot(item);
	}

	/**
	 Adds a new TreeItem to the tree.

	 @param index where to add the child at
	 @param data data inside node
	 */
	public void addChildToRoot(int index, TreeItemData<E> data) {
		if (index < 0) {
			addChildToRoot(data);
			return;
		}
		if (data.getCellType() == CellType.FOLDER) {
			addChildToRoot(index, createFolder(data));
		} else if (data.getCellType() == CellType.COMPOSITE) {
			addChildToRoot(index, createComposite(data));
		} else {
			addChildToRoot(index, new MoveableTreeItem<>(data));
		}
	}


	public int getSelectedIndex() {
		return getSelectionModel().getSelectedIndex();
	}

	public TreeItemData<E> getItem(int index) {
		return getRoot().getChildren().get(index).getValue();
	}

	/**
	 Removes the specified child from the tree of the given parent.

	 @param parent what TreeItem the item is a child of
	 @param toRemove item to remove
	 */
	void removeChild(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItem<TreeItemData<E>> toRemove) {
		IFoundChild found = new IFoundChild() {
			@Override
			public <E> void found(TreeItem<TreeItemData<E>> found) {
				found.getValue().delete();
			}
		};
		for (TreeItem<TreeItemData<E>> item : toRemove.getChildren()) {
			TreeUtil.stepThroughDescendants(item, found);
		}
		toRemove.getValue().delete();
		parent.getChildren().remove(toRemove);
		// if the parent is a folder, add a placeholder item in it if the folder is not empty
		if (parent.getValue().canHaveChildren() && parent.getChildren().size() == 0) {
			parent.getChildren().add(new TreeItem<>());
		}
	}


	/**
	 Adds a child to a designated parent.

	 @param parent parent node
	 @param child node to be made the child of parent
	 */
	void addChildToParent(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItem<TreeItemData<E>> child) {
		// if the parent is a folder, remove the placeholder item in that folder if there is one
		if (parent.getValue().canHaveChildren() && parent.getChildren().size() == 1 && parent.getChildren().get(0).getValue().isPlaceholder()) {
			parent.getChildren().remove(0);
		}
		parent.getChildren().add(child);
	}

	/**
	 Adds a child to a designated parent.

	 @param parent parent node
	 @param childData node to be made the child of parent
	 */
	void addChildToParent(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItemData<E> childData) {
		// if the parent is a folder, remove the placeholder item in that folder if there is one
		if (childData.getCellType() == CellType.FOLDER) {
			addChildToParent(parent, createFolder(childData));
		} else if (childData.getCellType() == CellType.COMPOSITE) {
			addChildToParent(parent, createComposite(childData));
		} else {
			addChildToParent(parent, new TreeItem<>(childData));
		}
	}

	/**
	 Adds a child to the root

	 @param item item to be added
	 */
	void addChildToRoot(@NotNull TreeItem<TreeItemData<E>> item) {
		getRoot().getChildren().add(item);
	}

	/**
	 Adds a new TreeItem to the tree.

	 @param index where to add the child at
	 @param item tree item to add
	 */
	void addChildToRoot(int index, @NotNull TreeItem<TreeItemData<E>> item) {
		if (index < 0) {
			addChildToRoot(item);
			return;
		}
		getRoot().getChildren().add(index, item);
	}

	void addFolderToParent(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItemData<E> data) {
		TreeItem<TreeItemData<E>> folder = createFolder(data);
		addChildToParent(parent, folder);
	}


	@Nullable
	TreeItem<TreeItemData<E>> getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}

	private TreeItem<TreeItemData<E>> createFolder(@NotNull TreeItemData<E> data) {
		MoveableTreeItem<E> folder = new MoveableTreeItem<E>(data);
		folder.getChildren().add(new MoveableTreeItem());
		return folder;
	}

	private TreeItem<TreeItemData<E>> createComposite(@NotNull TreeItemData<E> data) {
		MoveableTreeItem<E> comp = new MoveableTreeItem<E>(data);
		comp.getChildren().add(new MoveableTreeItem());
		return comp;
	}


}
