package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Creates a new EditableTreeView with a root node already in place. This class extends javafx.scene.control.TreeView of type TreeItemData */
public class EditableTreeView<E extends TreeItemData> extends javafx.scene.control.TreeView<E> {

	public EditableTreeView(@Nullable TreeCellSelectionUpdate selectionUpdate) {
		super(new TreeItem<>());
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
	public void addChildDataToRoot(E data) {
		addChildToRoot(new TreeItem<>(data));
	}

	/**
	 Adds a new TreeItem to the tree.

	 @param index where to add the child at
	 @param data data inside node
	 */
	public void addChildDataToRoot(int index, E data) {
		if (index < 0) {
			addChildDataToRoot(data);
			return;
		}
		if (data.getCellType() == CellType.FOLDER) {
			addChildToRoot(index, new TreeItem<>(data));
		} else if (data.getCellType() == CellType.COMPOSITE) {
			addChildToRoot(index, new TreeItem<>(data));
		} else {
			addChildToRoot(index, new TreeItem<>(data));
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
	protected void removeChild(@NotNull TreeItem<E> parent, @NotNull TreeItem<E> toRemove) {
		FoundChild<E> found = new FoundChild<E>() {
			@Override
			public void found(TreeItem<E> found) {
				found.getValue().delete();
			}
		};
		for (TreeItem<E> item : toRemove.getChildren()) {
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
	 Adds a child to a designated parent. This simply calls addChildToParent(TreeItem<E> parent, TreeItem<E> child, int index) with index set to parent.getChildren().size()

	 @param parent parent node
	 @param child node to be made the child of parent
	 */
	protected void addChildToParent(@NotNull TreeItem<E> parent, @NotNull TreeItem<E> child) {
		addChildToParent(parent, child, parent.getChildren().size());
	}

	/**
	 Adds a child to a designated parent.

	 @param parent parent node
	 @param child node to be made the child of parent
	 @param index index for where child is to be inserted (use child count to add to end)
	 */
	protected void addChildToParent(@NotNull TreeItem<E> parent, @NotNull TreeItem<E> child, int index) {
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
	protected void addChildDataToParent(@NotNull TreeItem<E> parent, @NotNull E childData) {
		// if the parent is a folder, remove the placeholder item in that folder if there is one
		if (childData.getCellType() == CellType.FOLDER) {
			addChildToParent(parent, new TreeItem<>(childData));
		} else if (childData.getCellType() == CellType.COMPOSITE) {
			addChildToParent(parent, new TreeItem<>(childData));
		} else {
			addChildToParent(parent, new TreeItem<>(childData));
		}
	}

	/**
	 Adds a child to the root

	 @param item item to be added
	 */
	protected void addChildToRoot(@NotNull TreeItem<E> item) {
		getRoot().getChildren().add(item);
	}

	/**
	 Adds a new TreeItem to the tree's root.

	 @param index where to add the child at
	 @param item tree item to add
	 */
	protected void addChildToRoot(int index, @NotNull TreeItem<E> item) {
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
	protected void addFolderDataToParent(@NotNull TreeItem<E> parent, @NotNull E data) {
		TreeItem<E> folder = new TreeItem<>(data);
		addChildToParent(parent, folder);
	}


	@Nullable
	protected TreeItem<E> getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}


}
