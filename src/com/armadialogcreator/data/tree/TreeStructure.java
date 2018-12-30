package com.armadialogcreator.data.tree;

import com.armadialogcreator.gui.fxcontrol.treeView.GUITreeStructure;
import org.jetbrains.annotations.NotNull;

/**
 Used for representing a Tree for use of outside or inside the GUI code.
 This interface was created after {@link GUITreeStructure} to modularize the code more.

 @author Kayler
 @see GUITreeStructure
 @since 05/01/2017. */
public interface TreeStructure<E> {

	/**
	 The root node should have null data inside {@link TreeNode#getData()}.
	 It also should not be displayed to the user or written to file. It is merely a
	 starting point for the tree.

	 @return the root node
	 */
	@NotNull
	TreeNode<E> getRoot();

	class Simple<E> implements TreeStructure<E> {

		private TreeNode<E> root;

		public Simple(@NotNull TreeNode<E> root) {
			this.root = root;
		}

		@NotNull
		@Override
		public TreeNode<E> getRoot() {
			return root;
		}
	}

}
