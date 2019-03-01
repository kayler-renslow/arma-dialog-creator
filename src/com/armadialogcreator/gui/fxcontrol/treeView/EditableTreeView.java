package com.armadialogcreator.gui.fxcontrol.treeView;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Creates a new EditableTreeView with a root node already in place. This class extends javafx.scene.control.TreeView of type TreeItemData */
public class EditableTreeView<Tv, Td extends TreeItemData> extends javafx.scene.control.TreeView<Td> {

	public EditableTreeView(@Nullable TreeCellSelectionUpdate selectionUpdate) {
		super(new TreeItem<>());
		this.showRootProperty().set(false);

		//setEditable() is for double clicking and renaming the cell
		//We do not want the double clicking functionality
		this.setEditable(false);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setCellSelectionUpdate(selectionUpdate);
	}

	public void setCellSelectionUpdate(@Nullable TreeCellSelectionUpdate selectionUpdate) {
		setCellFactory(new TreeFactoryGen<>(new EditableTreeCellFactory<>(this, selectionUpdate)));
	}

	/** Clears the TreeView and loads the given tree structure. If treeStructure is null, will just clear the tree */
	public final void clear() {
		setRoot(new TreeItem<>());
	}

	/**
	 Adds a new TreeItem to the tree.

	 @param data data inside tree node
	 */
	public void addChildDataToRoot(Td data) {
		addChildToRoot(new TreeItem<>(data));
	}

	/**
	 Adds a new TreeItem to the tree.

	 @param index where to add the child at
	 @param data data inside node
	 */
	public void addChildDataToRoot(int index, Td data) {
		if (index < 0) {
			addChildDataToRoot(data);
			return;
		}
		addChildToRoot(index, new TreeItem<>(data));
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
	protected void removeChild(@NotNull TreeItem<Td> parent, @NotNull TreeItem<Td> toRemove) {
		FoundChild<Td> found = new FoundChild<Td>() {
			@Override
			public boolean found(TreeItem<Td> found) {
				found.getValue().delete();
				return false;
			}
		};
		TreeUtil.stepThroughDescendants(toRemove, found);
		toRemove.getValue().delete();
		parent.getChildren().remove(toRemove);
	}


	/**
	 Adds a child to a designated parent. This simply calls addChildToParent(TreeItem<Td> parent, TreeItem<Td> child, int index) with index set to parent.getChildren().size()

	 @param parent parent node
	 @param child node to be made the child of parent
	 */
	protected void addChildToParent(@NotNull TreeItem<Td> parent, @NotNull TreeItem<Td> child) {
		addChildToParent(parent, child, parent.getChildren().size());
	}

	/**
	 Adds a child to a designated parent.

	 @param parent parent node
	 @param child node to be made the child of parent
	 @param index index for where child is to be inserted (use child count to add to end)
	 */
	protected void addChildToParent(@NotNull TreeItem<Td> parent, @NotNull TreeItem<Td> child, int index) {
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
	protected void addChildDataToParent(@NotNull TreeItem<Td> parent, @NotNull Td childData) {
		addChildToParent(parent, new TreeItem<>(childData));
	}

	/**
	 Adds a child to the root

	 @param item item to be added
	 */
	protected void addChildToRoot(@NotNull TreeItem<Td> item) {
		getRoot().getChildren().add(item);
	}

	/**
	 Adds a new TreeItem to the tree's root.

	 @param index where to add the child at
	 @param item tree item to add
	 */
	protected void addChildToRoot(int index, @NotNull TreeItem<Td> item) {
		if (index < 0) {
			addChildToRoot(item);
			return;
		}
		getRoot().getChildren().add(index, item);
	}

	@Nullable
	protected TreeItem<Td> getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}

	/**
	 Used to move a TreeItem from it's current parent to a new parent.
	 When toMove is removed from it's original parent, {@link #removeChild(TreeItem, TreeItem)} will be used

	 @param toMove TreeItem to move
	 @param newParent the new parent of the TreeItem.
	 @param index where to place toMove
	 @throws IllegalArgumentException If newParent can't have children ({@link TreeItemData#canHaveChildren()}==false)
	 */
	protected void moveTreeItem(@NotNull TreeItem<Td> toMove, @NotNull TreeItem<Td> newParent, int index) {
		//move into children

		//Don't use addToParent so that a move and addition can be detected disjointly
		if (newParent == getRoot() || newParent.getValue().canHaveChildren()) {
			toMove.getParent().getChildren().remove(toMove);
			if (newParent.getChildren().size() == 0) {
				newParent.getChildren().add(toMove);
			} else if (index == newParent.getChildren().size()) {
				newParent.getChildren().add(toMove);
			} else {
				newParent.getChildren().add(index, toMove);
			}
			return;
		}

		throw new IllegalArgumentException("newParent can't have children");
	}
}
