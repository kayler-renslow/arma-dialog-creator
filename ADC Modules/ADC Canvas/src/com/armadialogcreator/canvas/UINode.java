package com.armadialogcreator.canvas;

import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.EmptyIterable;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/06/2019 */
public interface UINode {

	/** @return an iterable that iterates all children in an order that doesn't need to matter */
	@NotNull Iterable<? extends UINode> iterateChildNodes();

	/** @return the update group for when a re-render is requested for this node */
	@NotNull UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup();

	/** @return how many children this node has */
	int getChildCount();

	/** @return index of where the child is stored, or -1 if the node is not a child */
	int indexOf(@NotNull UINode child);

	/** @return true if the direct children of this node contains the provided node */
	boolean containsChildNode(@NotNull UINode node);

	/**
	 Appends the provided node to the end of the children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>
	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 */
	void addChild(@NotNull UINode node);

	/**
	 Appends the provided node at the specified index.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>
	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 */
	void addChild(@NotNull UINode node, int index);

	/**
	 Removes the provided node from the direct children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>null</code>

	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 @return true if the node was removed, false if it wasn't in the children
	 */
	boolean removeChild(@NotNull UINode node);

	/**
	 Removes the node at the specified index from the direct children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>null</code>

	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 @return the node that was removed, or null if no node was removed
	 */
	@Nullable
	UINode removeChild(int index);

	/**
	 Moves the provided node to a new parent's children at an index.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 @throws IllegalArgumentException when the child is not owned by this node
	 */
	void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex);

	/**
	 Takes the moved child from the old parent and places it in this node's children.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>
	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 @see #moveChild(UINode, UINode, int) use this method to start a move.
	 */
	void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex);

	/**
	 @return an iterable that iterates the all children and descendant nodes in the order such that the first node is rendered first
	 and the last iterated node is rendered last (is on top)
	 */
	@NotNull
	DeepUINodeIterable deepIterateChildren();

	/**
	 @return a {@link CanvasComponent} instance that will render what this node looks like,
	 or null if this node is purely structural
	 */
	@Nullable CanvasComponent getComponent();

	/**
	 @return the top level node, or null if this node doesn't belong to a tree.
	 If this node is the top level node, <code>this</code> will be returned
	 */
	default @Nullable UINode getRootNode() {
		if (getParentNode() == this) {
			return this;
		}
		UINode node = getParentNode();
		while (node != null && node != this) {
			node = getParentNode();
		}
		return node;
	}

	/** @return the parent node of this node, or {@link #getRootNode()} if is the root node, or null if not member of a tree */
	@Nullable UINode getParentNode();

	/** Sets the parent node */
	void setParentNode(@Nullable UINode newParent);

	/**
	 Creates a deep copy of this node, but preserves the {@link #getParentNode()}.
	 This method should not deep copy any child nodes and should not append any child nodes to this copied node.

	 @return the copy
	 */
	@NotNull
	UINode deepCopy();


	/**
	 If a node can have children, this method should return true. If it can't have children,
	 the child manipulator methods will throw an {@link IllegalStateException}.
	 <p>
	 Manipulator methods:
	 <ul>
	 <li>{@link #addChild(UINode)}</li>
	 <li>{@link #addChild(UINode, int)}</li>
	 <li>{@link #removeChild(UINode)}</li>
	 <li>{@link #removeChild(int)}</li>
	 <li>{@link #moveChild(UINode, UINode, int)}</li>
	 </ul>

	 @return true if this node can have children, false if it can't. By default, returns true
	 */
	default boolean canHaveChildren() {
		return true;
	}

	@Nullable
	default UINode getFirstNonStructureAncestorNode() {
		UINode node = getParentNode();
		while (node != null && node.getComponent() == null && node != node.getRootNode()) {
			node = node.getParentNode();
		}
		return node;
	}

	@NotNull DataContext getUserData();

	@NotNull UpdateListenerGroup<UINodeChange> getUpdateGroup();

	UINode EMPTY = new UINode() {
		@Override
		@NotNull
		public Iterable<? extends UINode> iterateChildNodes() {
			return new EmptyIterable<>();
		}

		@Override
		@NotNull
		public UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
			return new UpdateListenerGroup<>();
		}

		@Override
		public int getChildCount() {
			return 0;
		}

		@Override
		public int indexOf(@NotNull UINode child) {
			return -1;
		}

		@Override
		public boolean containsChildNode(@NotNull UINode node) {
			return false;
		}

		@Override
		public void addChild(@NotNull UINode node) {

		}

		@Override
		public void addChild(@NotNull UINode node, int index) {

		}

		@Override
		public boolean removeChild(@NotNull UINode node) {
			return false;
		}

		@Override
		public @Nullable UINode removeChild(int index) {
			return null;
		}

		@Override
		public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {

		}

		@Override
		public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {

		}

		@Override
		@NotNull
		public DeepUINodeIterable deepIterateChildren() {
			return new DeepUINodeIterable(new EmptyIterable<>());
		}

		@Override
		@Nullable
		public CanvasComponent getComponent() {
			return null;
		}

		@Override
		@Nullable
		public UINode getParentNode() {
			return null;
		}

		@Override
		public void setParentNode(@Nullable UINode newParent) {

		}

		@Override
		@NotNull
		public UINode deepCopy() {
			return this;
		}

		@Override
		@NotNull
		public DataContext getUserData() {
			return new DataContext();
		}

		@Override
		@NotNull
		public UpdateListenerGroup<UINodeChange> getUpdateGroup() {
			return new UpdateListenerGroup<>();
		}
	};
}
