package com.armadialogcreator.control;

import com.armadialogcreator.canvas.CanvasComponent;
import com.armadialogcreator.canvas.DeepUINodeIterable;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.canvas.UINodeChange;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 @author K
 @since 02/06/2019 */
public abstract class SimpleBaseUINode implements UINode {
	protected UINode rootNode;
	protected UINode parentNode;
	protected final List<UINode> children = new LinkedList<>();
	protected final UpdateListenerGroup<UINodeChange> updateGroup = new UpdateListenerGroup<>();
	protected final DataContext userData = new DataContext();

	public SimpleBaseUINode(@Nullable UINode rootNode) {
		this.rootNode = rootNode;
	}

	@Override
	@NotNull
	public Iterable<? extends UINode> iterateChildNodes() {
		return children;
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
		if (rootNode == null) {
			return ArmaControlRenderer.UNIVERSAL_RENDER_GROUP;
		}
		return rootNode.renderUpdateGroup();
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	@NotNull
	public DeepUINodeIterable deepIterateChildren() {
		return new DeepUINodeIterable(children);
	}

	@Override
	public boolean containsChildNode(@NotNull UINode node) {
		return children.contains(node);
	}

	@Override
	public void addChild(@NotNull UINode node) {
		children.add(node);
		node.setParentNode(this);
		node.setRootNode(this.rootNode);
		for (UINode child : node.deepIterateChildren()) {
			child.setRootNode(this.rootNode);
		}
		updateGroup.update(new UINodeChange.AddChild(node));
	}

	@Override
	public void addChild(@NotNull UINode node, int index) {
		children.add(index, node);
		node.setParentNode(this);
		node.setRootNode(this.rootNode);
		for (UINode child : node.deepIterateChildren()) {
			child.setRootNode(this.rootNode);
		}
		updateGroup.update(new UINodeChange.AddChild(node, index));
	}

	@Override
	public boolean removeChild(@NotNull UINode node) {
		node.setParentNode(null);
		node.setRootNode(null);
		if (children.remove(node)) {
			for (UINode child : node.deepIterateChildren()) {
				child.setRootNode(null);
			}
			updateGroup.update(new UINodeChange.RemoveChild(node));
			return true;
		}
		return false;
	}

	@Override
	public void removeChild(int index) {
		UINode removed = children.remove(index);
		if (removed != null) {
			removed.setRootNode(null);
			removed.setParentNode(null);
			for (UINode child : removed.deepIterateChildren()) {
				child.setRootNode(null);
			}
			updateGroup.update(new UINodeChange.RemoveChild(removed));
		}
	}

	@Override
	public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {
		children.remove(child);
		updateGroup.update(new UINodeChange.MoveChild(child, this, newParent, destIndex, true));
		newParent.acceptMovedChild(child, this, destIndex);
	}

	@Override
	public int indexOf(@NotNull UINode child) {
		return children.indexOf(child);
	}

	@Override
	public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {
		children.add(destIndex, child);
		for (UINode node : child.deepIterateChildren()) {
			node.setRootNode(this.rootNode);
		}
		child.setRootNode(this.rootNode);
		child.setParentNode(this);
		updateGroup.update(new UINodeChange.MoveChild(child, oldParent, this, destIndex, false));
	}

	@Override
	@NotNull
	public DataContext getUserData() {
		return userData;
	}

	@Override
	@Nullable
	public CanvasComponent getComponent() {
		return null;
	}

	@Override
	@Nullable
	public UINode getParentNode() {
		return parentNode;
	}

	@Override
	@Nullable
	public UINode getRootNode() {
		return rootNode;
	}

	public void setRootNode(@Nullable UINode rootNode) {
		this.rootNode = rootNode;
	}

	public void setParentNode(@Nullable UINode parentNode) {
		this.parentNode = parentNode;
	}

	@NotNull
	@Override
	public UpdateListenerGroup<UINodeChange> getUpdateGroup() {
		return updateGroup;
	}
}
