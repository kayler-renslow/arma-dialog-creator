package com.armadialogcreator.canvas;

import com.armadialogcreator.layout.Bounds;
import com.armadialogcreator.layout.Layout;
import com.armadialogcreator.layout.LayoutNode;
import com.armadialogcreator.layout.StaticPositionLayout;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.NotNullValueObserver;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 @author Kayler
 @since 8/1/19. */
public class LayoutPaneUINode implements NamedUINode {
	private @NotNull Layout layout = StaticPositionLayout.SHARED;
	private UINode parentNode = null;
	private boolean enabled, ghost;
	private Bounds bounds;
	private final UpdateListenerGroup<UpdateListenerGroup.NoData> renderGroup = new UpdateListenerGroup<>();
	private final UpdateListenerGroup<UINodeChange> nodeChangeGroup = new UpdateListenerGroup<>();
	private final DataContext userData = new DataContext();
	private final NotNullValueObserver<String> nodeNameObserver = new NotNullValueObserver<>(layout.getName());

	@NotNull
	public Layout getLayout() {
		return layout;
	}

	@Override
	public @NotNull Iterable<? extends UINode> iterateChildNodes() {
		return new MyUINodeIterable();
	}

	@Override
	public @NotNull UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
		return renderGroup;
	}

	@Override
	public int getChildCount() {
		return layout.getChildren().size();
	}

	@Override
	public int indexOf(@NotNull UINode child) {
		return layout.getChildren().indexOf(child);
	}

	@Override
	public boolean containsChildNode(@NotNull UINode node) {
		return layout.getChildren().contains(node);
	}

	@Override
	public void addChild(@NotNull UINode node) {
		layout.getChildren().add(node);
		nodeChangeGroup.update(new UINodeChange.AddChild(node));
	}

	@Override
	public void addChild(@NotNull UINode node, int index) {
		layout.getChildren().add(index, node);
		nodeChangeGroup.update(new UINodeChange.AddChild(node, index));
	}

	@Override
	public boolean removeChild(@NotNull UINode node) {
		boolean ret = layout.getChildren().remove(node);
		if (ret) {
			nodeChangeGroup.update(new UINodeChange.RemoveChild(node));
		}
		return ret;
	}

	@Override
	@Nullable
	public UINode removeChild(int index) {
		UINode remove = (UINode) layout.getChildren().remove(index);
		if (remove != null) {
			nodeChangeGroup.update(new UINodeChange.RemoveChild(remove));
		}
		return remove;
	}

	@Override
	public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {
		layout.getChildren().remove(child);
		nodeChangeGroup.update(new UINodeChange.MoveChild(child, this, newParent, destIndex, true));
		newParent.acceptMovedChild(child, this, destIndex);
	}

	@Override
	public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {
		nodeChangeGroup.update(new UINodeChange.MoveChild(child, this, oldParent, destIndex, false));
		layout.getChildren().add(child);
	}

	@Override
	public @NotNull DeepUINodeIterable deepIterateChildren() {
		return new DeepUINodeIterable(new MyUINodeIterable());
	}

	@Override
	public @Nullable UINode getParentNode() {
		return parentNode;
	}

	@Override
	public void setParentNode(@Nullable UINode newParent) {
		this.parentNode = newParent;
	}

	@Override
	public @NotNull UINode deepCopy() {
		return new LayoutPaneUINode();
	}

	@Override
	public @NotNull DataContext getUserData() {
		return userData;
	}

	@Override
	public @NotNull UpdateListenerGroup<UINodeChange> getUpdateGroup() {
		return nodeChangeGroup;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isGhost() {
		return ghost;
	}

	@Override
	public void setGhost(boolean ghost) {
		this.ghost = ghost;
	}

	@Override
	public void assignBounds(@NotNull Bounds bounds) {
		this.bounds = bounds;
	}

	@Override
	public @NotNull Bounds getBounds() {
		return bounds;
	}

	@Override
	public void invalidate() {
		layout.invalidate();
		nodeNameObserver.invalidate();
	}

	@Override
	public @NotNull NotNullValueObserver<String> getUINodeName() {
		return nodeNameObserver;
	}

	private class MyUINodeIterable implements Iterable<UINode>, Iterator<UINode> {

		Iterator<LayoutNode> iter = layout.getChildren().iterator();

		@NotNull
		@Override
		public Iterator<UINode> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public UINode next() {
			return (UINode) iter.next();
		}
	}
}
