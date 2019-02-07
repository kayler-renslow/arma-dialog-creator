package com.armadialogcreator.control;

import com.armadialogcreator.canvas.DeepUINodeIterable;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 @author K
 @since 02/06/2019 */
public abstract class SimpleBaseUINode implements UINode {
	protected UINode rootNode;
	protected final ListObserver<UINode> children = new ListObserver<>(new LinkedList<>());

	public SimpleBaseUINode(@Nullable UINode rootNode) {
		this.rootNode = rootNode;
	}

	@NotNull
	public ListObserver<UINode> getChildren() {
		return children;
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
	@Nullable
	public UINode getRootNode() {
		return rootNode;
	}

	public void setRootNode(@Nullable UINode rootNode) {
		this.rootNode = rootNode;
	}
}
