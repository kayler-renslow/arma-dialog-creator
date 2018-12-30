package com.armadialogcreator.gui.fxcontrol.treeView;

import com.armadialogcreator.data.tree.TreeNode;
import com.armadialogcreator.data.tree.TreeStructure;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

/**
 Used for setting the entries in a TreeView, or representing the entries for use of outside the GUI code.
 This is a slightly complicated way of converting {@link TreeItem} instances into something more generic outside the GUI,
 but it makes handling the data easier since there won't be any conflict between the GUI code and this structure.

 @author Kayler
 @since 08/07/2016. */
public class GUITreeStructure<T> implements TreeStructure<T> {

	private final TreeNode<T> root;

	public GUITreeStructure(@NotNull TreeNode<T> root) {
		this.root = root;
	}

	@Override
	@NotNull
	public TreeNode<T> getRoot() {
		return root;
	}

	/**
	 Convert a {@link TreeView} into a {@link TreeStructure}.

	 @param treeView {@link TreeView} to convert
	 @param converter the converter between {@link TreeItemData} to {@link Tv}
	 @param <Td> type of the {@link TreeItemData}
	 @param <Tv> type of data to make a {@link TreeStructure} with
	 @return the structure
	 */
	@SuppressWarnings("unchecked")
	public static <Td extends TreeItemData, Tv> GUITreeStructure<Tv> getStructure(@NotNull TreeView<Td> treeView, @NotNull TreeDataToValueConverter<Td, Tv> converter) {

		TreeNode.Simple<Tv> rootAsNode = new TreeNode.Simple<>(null, "~ROOT", false);
		for (TreeItem<Td> child : treeView.getRoot().getChildren()) {
			TreeNode.Simple<Tv> treeItemAsNode = getNode(converter, child);
			rootAsNode.getChildren().add(treeItemAsNode);

			buildStructure(converter, child, treeItemAsNode);
		}

		return new GUITreeStructure<>(rootAsNode);
	}

	private static <Td extends TreeItemData, Tv> void buildStructure(
			@NotNull TreeDataToValueConverter<Td, Tv> converter,
			@NotNull TreeItem<Td> treeItem,
			@NotNull TreeNode<Tv> treeItemAsNode) {

		for (TreeItem<Td> child : treeItem.getChildren()) {
			TreeNode.Simple<Tv> childAsNode = getNode(converter, child);
			treeItemAsNode.getChildren().add(childAsNode);
			buildStructure(converter, child, childAsNode);
		}
	}

	@NotNull
	private static <Td extends TreeItemData, Tv> TreeNode.Simple<Tv> getNode(@NotNull TreeDataToValueConverter<Td, Tv> converter, TreeItem<Td> child) {
		return new TreeNode.Simple<>(converter.convert(child.getValue()), child.getValue().toString(), child.getValue().isFolder());
	}


}
