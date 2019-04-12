package com.armadialogcreator.control;

import com.armadialogcreator.canvas.CanvasComponent;
import com.armadialogcreator.canvas.DeepUINodeIterable;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.canvas.UINodeChange;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.DoubleIterable;
import com.armadialogcreator.util.Key;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Interface that specifies something that is displayable in preview and in Arma 3 (title, dialog, display)

 @author Kayler
 @since 06/14/2016. */
public class ArmaDisplay extends ConfigClass implements UINode {
	private static final Key<Boolean> KEY_NODE_IS_IN_BACKGROUND = new Key<>("ArmaDisplay.nodeInBackground", false);
	private final SimpleBaseUINode controlNodes = new ControlsNode(this, false);
	private final SimpleBaseUINode bgControlNodes = new ControlsNode(this, true);
	private final UpdateListenerGroup<UINodeChange> updateGroup = new UpdateListenerGroup<>();
	private final UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup = new UpdateListenerGroup<>();

	public ArmaDisplay() {
		this("MyDisplay");
	}

	public ArmaDisplay(@NotNull String displayName) {
		super(displayName);
		controlNodes.updateGroup.chain(updateGroup);
		bgControlNodes.updateGroup.chain(updateGroup);
	}

	@NotNull
	public UINode getControlNodes() {
		return controlNodes;
	}

	@NotNull
	public UINode getBackgroundControlNodes() {
		return bgControlNodes;
	}

	@Override
	@NotNull
	public Iterable<? extends UINode> iterateChildNodes() {
		return new DoubleIterable<>(controlNodes.children, bgControlNodes.children);
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
		return renderUpdateGroup;
	}

	@Override
	public int getChildCount() {
		return bgControlNodes.children.size() + controlNodes.children.size();
	}

	@Override
	public int indexOf(@NotNull UINode child) {
		int i = bgControlNodes.indexOf(child);
		if (i != -1) {
			return i;
		}
		return controlNodes.indexOf(child);
	}

	@Override
	public boolean containsChildNode(@NotNull UINode node) {
		return bgControlNodes.containsChildNode(node) || controlNodes.containsChildNode(node);
	}

	@Override
	public void addChild(@NotNull UINode node) {
		controlNodes.addChild(node);
	}

	@Override
	public void addChild(@NotNull UINode node, int index) {
		controlNodes.addChild(node, index);
	}

	@Override
	public boolean removeChild(@NotNull UINode node) {
		if (!bgControlNodes.removeChild(node)) {
			return controlNodes.removeChild(node);
		}
		return false;
	}

	@Override
	@Nullable
	public UINode removeChild(int index) {
		return controlNodes.removeChild(index);
	}

	@Override
	public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {
		if (bgControlNodes.containsChildNode(child)) {
			bgControlNodes.moveChild(child, newParent, destIndex);
			return;
		}
		controlNodes.moveChild(child, newParent, destIndex);
	}

	@Override
	public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {
		controlNodes.acceptMovedChild(child, oldParent, destIndex);
	}

	@Override
	@NotNull
	public DeepUINodeIterable deepIterateChildren() {
		return new DeepUINodeIterable(new DoubleIterable<>(controlNodes.deepIterateChildren(), bgControlNodes.deepIterateChildren()));
	}

	@Override
	@Nullable
	public CanvasComponent getComponent() {
		return null;
	}

	@Override
	@Nullable
	public UINode getRootNode() {
		return this;
	}

	@Override
	@Nullable
	public UINode getParentNode() {
		return this;
	}

	@Override
	public void setParentNode(@Nullable UINode newParent) {
		//why are you calling this method on a display?
		throw new IllegalStateException();
	}

	@Override
	public @NotNull UINode deepCopy() {
		return new ArmaDisplay();
	}

	@Override
	@NotNull
	public DataContext getUserData() {
		return controlNodes.getUserData();
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UINodeChange> getUpdateGroup() {
		return updateGroup;
	}

	public boolean controlIsBackgroundControl(ArmaControl control) {
		return KEY_NODE_IS_IN_BACKGROUND.get(control.getUserData(), false);
	}

	private static class ControlsNode extends SimpleBaseUINode {
		private final ArmaDisplay display;
		private final boolean background;

		public ControlsNode(@NotNull ArmaDisplay display, boolean background) {
			this.display = display;
			this.background = background;
			setParentNode(display);
		}

		@Override
		@Nullable
		public CanvasComponent getComponent() {
			return null;
		}

		@Override
		public void addChild(@NotNull UINode node) {
			super.addChild(node);
			node.getUserData().put(KEY_NODE_IS_IN_BACKGROUND, background);
		}

		@Override
		public void addChild(@NotNull UINode node, int index) {
			super.addChild(node, index);
			node.getUserData().put(KEY_NODE_IS_IN_BACKGROUND, background);
		}

		@Override
		public boolean removeChild(@NotNull UINode node) {
			node.getUserData().remove(KEY_NODE_IS_IN_BACKGROUND);
			return super.removeChild(node);
		}

		@Override
		@Nullable
		public UINode removeChild(int index) {
			UINode removed = super.removeChild(index);
			if (removed == null) {
				return null;
			}
			removed.getUserData().remove(KEY_NODE_IS_IN_BACKGROUND);
			return removed;
		}

		@Override
		public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {
			child.getUserData().remove(KEY_NODE_IS_IN_BACKGROUND);
			super.moveChild(child, newParent, destIndex);
		}

		@Override
		public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {
			super.acceptMovedChild(child, oldParent, destIndex);
			child.getUserData().put(KEY_NODE_IS_IN_BACKGROUND, background);
		}

		@Override
		public void setParentNode(@Nullable UINode parentNode) {
			super.setParentNode(parentNode);
			if (parentNode != null && parentNode.getRootNode() == this.display) {
				getUserData().put(KEY_NODE_IS_IN_BACKGROUND, background);
			} else {
				getUserData().remove(KEY_NODE_IS_IN_BACKGROUND);
			}
		}

		@Override
		@NotNull
		public UINode deepCopy() {
			return new ControlsNode(display, background);
		}
	}

}
