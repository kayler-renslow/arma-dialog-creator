package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Creates a new EditableTreeView with a root node already in place. This class extends javafx.scene.control.TreeView of type TreeItemData */
public class EditableTreeView<E> extends javafx.scene.control.TreeView<TreeItemData<E>> {

	public EditableTreeView(@Nullable TreeCellSelectionUpdate selectionUpdate) {
		super(new MoveableTreeItem());
		this.showRootProperty().set(false);

		this.setEditable(true);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setCellSelectionUpdate(selectionUpdate);
	}

	public void setCellSelectionUpdate(@Nullable TreeCellSelectionUpdate selectionUpdate) {
		setCellFactory(new TreeFactoryGen<>(new EditableTreeCellFactory<>(this, selectionUpdate)));
	}


	/**
	 Adds a new TreeItem to the tree.

	 @param data data inside tree node
	 */
	public void addChildDataToRoot(TreeItemData<E> data) {
		TreeItem<TreeItemData<E>> item;
		if (data.getCellType() == CellType.FOLDER) {
			item = createFolder(data);
		} else if (data.getCellType() == CellType.COMPOSITE) {
			item = createComposite(data);
		} else {
			item = new MoveableTreeItem<>(data);
		}

		addChildToRoot(item);
	}

	/**
	 Adds a new TreeItem to the tree.

	 @param index where to add the child at
	 @param data data inside node
	 */
	public void addChildDataToRoot(int index, TreeItemData<E> data) {
		if (index < 0) {
			addChildDataToRoot(data);
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

	/** Return the most recent selected index */
	public int getSelectedIndex() {
		return getSelectionModel().getSelectedIndex();
	}

	/**
	 Removes the specified child from the tree of the given parent.

	 @param parent what TreeItem the item is a child of
	 @param toRemove item to remove
	 */
	protected void removeChild(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItem<TreeItemData<E>> toRemove) {
		FoundChild<TreeItemData<E>> found = new FoundChild<TreeItemData<E>>() {
			@Override
			public void found(TreeItem<TreeItemData<E>> found) {
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
	 Adds a child to a designated parent. This simply calls addChildToParent(TreeItem<TreeItemData<E>> parent, TreeItem<TreeItemData<E>> child, int index) with index set to parent.getChildren().size()

	 @param parent parent node
	 @param child node to be made the child of parent
	 */
	protected void addChildToParent(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItem<TreeItemData<E>> child) {
		addChildToParent(parent, child, parent.getChildren().size());
	}

	/**
	 Adds a child to a designated parent.

	 @param parent parent node
	 @param child node to be made the child of parent
	 @param index index for where child is to be inserted (use child count to add to end)
	 */
	protected void addChildToParent(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItem<TreeItemData<E>> child, int index) {
		// if the parent is a folder, remove the placeholder item in that folder if there is one
		if (parent.getValue().canHaveChildren() && parent.getChildren().size() == 1 && parent.getChildren().get(0).getValue().isPlaceholder()) {
			parent.getChildren().remove(0);
		}
		if (index >= parent.getChildren().size()) {
			parent.getChildren().add(child);
		} else {
			parent.getChildren().add(index, child);
		}
	}

	/**
	 Puts the child data into a TreeItem and then adds to designated parent.

	 @param parent parent node
	 @param childData node to be made the child of parent
	 */
	protected void addChildDataToParent(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItemData<E> childData) {
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
	protected void addChildToRoot(@NotNull TreeItem<TreeItemData<E>> item) {
		getRoot().getChildren().add(item);
	}

	/**
	 Adds a new TreeItem to the tree's root.

	 @param index where to add the child at
	 @param item tree item to add
	 */
	protected void addChildToRoot(int index, @NotNull TreeItem<TreeItemData<E>> item) {
		if (index < 0) {
			addChildToRoot(item);
			return;
		}
		getRoot().getChildren().add(index, item);
	}

	/**
	 Creates a new folder tree item with the given data and invokes addChildToParent on that folder tree item

	 @param parent parent to add folder to
	 @param data data of folder
	 */
	protected void addFolderDataToParent(@NotNull TreeItem<TreeItemData<E>> parent, @NotNull TreeItemData<E> data) {
		TreeItem<TreeItemData<E>> folder = createFolder(data);
		addChildToParent(parent, folder);
	}


	@Nullable
	protected TreeItem<TreeItemData<E>> getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}

	@SuppressWarnings("unchecked")
	private TreeItem<TreeItemData<E>> createFolder(@NotNull TreeItemData<E> data) {
		MoveableTreeItem<E> folder = new MoveableTreeItem<>(data);
		folder.getChildren().add(new MoveableTreeItem());
		return folder;
	}

	@SuppressWarnings("unchecked")
	private TreeItem<TreeItemData<E>> createComposite(@NotNull TreeItemData<E> data) {
		MoveableTreeItem<E> comp = new MoveableTreeItem<>(data);
		comp.getChildren().add(new MoveableTreeItem());
		return comp;
	}


}
