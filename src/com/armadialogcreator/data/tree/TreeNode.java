package com.armadialogcreator.data.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 @since 05/01/2017 */
public interface TreeNode<E> {
	/** Data inside the node. If null, likely is a folder */
	@Nullable E getData();

	/** Get displayable name for the node. For folders, this is the folder name. */
	@NotNull String getName();

	boolean isFolder();

	/***/
	@NotNull List<TreeNode<E>> getChildren();

	/** Return true if the node has no children (getChildren().length==0), or false if it has children */
	boolean isLeaf();

	class Simple<E> implements TreeNode<E> {
		private final List<TreeNode<E>> children = new ArrayList<>();
		private final E data;
		private final String name;
		private boolean folder;

		public Simple(@Nullable E data, @NotNull String name, boolean isFolder) {
			this.data = data;
			this.name = name;
			this.folder = isFolder;
		}

		@NotNull
		public static <E> TreeNode<E> newRoot() {
			return new Simple<>(null, "", false);
		}

		@Override
		@Nullable
		public E getData() {
			return data;
		}

		@NotNull
		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isFolder() {
			return folder;
		}

		@Override
		@NotNull
		public List<TreeNode<E>> getChildren() {
			return children;
		}

		@Override
		public boolean isLeaf() {
			return children.size() == 0;
		}

	}
}
