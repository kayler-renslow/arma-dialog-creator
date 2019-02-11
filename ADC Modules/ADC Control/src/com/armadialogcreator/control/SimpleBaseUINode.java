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
	protected UINode parentNode;
	protected final List<UINode> children = new LinkedList<>();
	protected final UpdateListenerGroup<UINodeChange> updateGroup = new UpdateListenerGroup<>();
	protected final DataContext userData = new DataContext();

	public SimpleBaseUINode() {
	}

	@Override
	@NotNull
	public Iterable<? extends UINode> iterateChildNodes() {
		return children;
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
		UINode rootNode = getRootNode();
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
		updateGroup.update(new UINodeChange.AddChild(node));
	}

	@Override
	public void addChild(@NotNull UINode node, int index) {
		children.add(index, node);
		node.setParentNode(this);
		updateGroup.update(new UINodeChange.AddChild(node, index));
	}

	@Override
	public boolean removeChild(@NotNull UINode node) {
		node.setParentNode(null);
		if (children.remove(node)) {
			updateGroup.update(new UINodeChange.RemoveChild(node));
			return true;
		}
		return false;
	}

	@Override
	@Nullable
	public UINode removeChild(int index) {
		UINode removed = children.remove(index);
		if (removed != null) {
			removed.setParentNode(null);
			updateGroup.update(new UINodeChange.RemoveChild(removed));
		}
		return removed;
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


	public void setParentNode(@Nullable UINode parentNode) {
		this.parentNode = parentNode;
	}

	@NotNull
	@Override
	public UpdateListenerGroup<UINodeChange> getUpdateGroup() {
		return updateGroup;
	}
}
