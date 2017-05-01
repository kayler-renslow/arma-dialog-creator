package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;

import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

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
		LinkedList<TreeItem<Td>> q = new LinkedList<>();
		q.addAll(treeView.getRoot().getChildren());
		TreeItem<Td> pop = treeView.getRoot();

		TreeNode<Tv> parent = new TreeNode.Simple<>(null, "~ROOT", false);
		TreeNode<Tv> created = parent;
		TreeNode<Tv> root = parent;
		int parentInd = 0;
		int childCount = parent.getChildren().size();
		while (q.size() > 0) {
			if (parentInd >= childCount) {
				parentInd = 0;
				childCount = pop.getChildren().size();
				parent = created;
			}
			pop = q.pop();
			q.addAll(pop.getChildren());
			created = new TreeNode.Simple<>(converter.convert(pop.getValue()), pop.getValue().toString(), pop.getValue().isFolder());
			parent.getChildren().add(created);
			parentInd++;
		}

		return new GUITreeStructure<>(root);
	}


}
