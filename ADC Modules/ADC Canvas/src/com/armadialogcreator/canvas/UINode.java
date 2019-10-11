package com.armadialogcreator.canvas;

import com.armadialogcreator.layout.LayoutNode;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.DataInvalidator;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/06/2019 */
public interface UINode extends DataInvalidator, LayoutNode {

	/** @return an iterable that iterates all children in an order that doesn't need to matter */
	@NotNull Iterable<? extends UINode> iterateChildNodes();

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
	 <p>
	 This method will create a {@link UINodeChange.AddChild} change inside {@link #getUpdateGroup()}

	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 */
	void addChild(@NotNull UINode node);

	/**
	 Appends the provided node at the specified index.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>
	 <p>
	 This method will create a {@link UINodeChange.AddChild} change inside {@link #getUpdateGroup()}

	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 */
	void addChild(@NotNull UINode node, int index);

	/**
	 Removes the provided node from the direct children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>null</code>
	 <p>
	 This method will create a {@link UINodeChange.RemoveChild} change inside {@link #getUpdateGroup()}
	 if a child was actually removed (this returns true)

	 @return true if the node was removed, false if it wasn't in the children
	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 */
	boolean removeChild(@NotNull UINode node);

	/**
	 Removes the node at the specified index from the direct children of this node.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>null</code>
	 <p>
	 This method will create a {@link UINodeChange.RemoveChild} change inside {@link #getUpdateGroup()}
	 if a child was actually removed (this returns true).

	 @return the node that was removed, or null if no node was removed
	 @throws IllegalStateException when {@link #canHaveChildren()} returns false
	 */
	@Nullable
	UINode removeChild(int index);

	/**
	 Moves the provided node to a new parent's children at an index.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 <p>
	 This method will create a {@link UINodeChange.MoveChild} change inside {@link #getUpdateGroup()}
	 and {@link UINodeChange.MoveChild#isEntryUpdate()} will return true.

	 @throws IllegalStateException    when {@link #canHaveChildren()} returns false
	 @throws IllegalArgumentException when the child is not owned by this node
	 */
	void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex);

	/**
	 Takes the moved child from the old parent and places it in this node's children.
	 In this method, the {@link #getRootNode()} is also updated for the child and it's descendants as well.
	 The new {@link #getRootNode()} will become <code>this</code>

	 This method will create a {@link UINodeChange.MoveChild} change inside {@link #getUpdateGroup()}
	 and {@link UINodeChange.MoveChild#isEntryUpdate()} will return false.

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

	@NotNull DataContext getUserData();

	@NotNull UpdateListenerGroup<UINodeChange> getUpdateGroup();

}
