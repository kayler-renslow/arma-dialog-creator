package com.armadialogcreator.canvas;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/09/2019 */
public abstract class UINodeChange {
	public enum Type {
		AddChild, MoveChild, RemoveChild
	}

	private final UINode node;
	private final Type type;

	public UINodeChange(@NotNull UINode node, @NotNull Type type) {
		this.node = node;
		this.type = type;
	}

	@NotNull
	public UINode getNode() {
		return node;
	}

	@NotNull
	public Type getType() {
		return type;
	}

	public static class AddChild extends UINodeChange {
		private final int index;

		public AddChild(@NotNull UINode node) {
			super(node, Type.AddChild);
			this.index = -1;
		}

		public AddChild(@NotNull UINode node, int index) {
			super(node, Type.AddChild);
			this.index = index;
		}

		/** @return the index at which it was added, or -1 if added at the end (appended) */
		public int getIndex() {
			return index;
		}
	}

	public static class MoveChild extends UINodeChange {
		private final UINode oldParent;
		private final UINode newParent;
		private final int destIndex;
		private final boolean entryUpdate;

		public MoveChild(@NotNull UINode node, @NotNull UINode oldParent, @NotNull UINode newParent, int destIndex, boolean entryUpdate) {
			super(node, Type.MoveChild);
			this.oldParent = oldParent;
			this.newParent = newParent;
			this.destIndex = destIndex;
			this.entryUpdate = entryUpdate;
		}

		@NotNull
		public UINode getNewParent() {
			return newParent;
		}

		public boolean isEntryUpdate() {
			return entryUpdate;
		}

		@NotNull
		public UINode getOldParent() {
			return oldParent;
		}

		public int getDestIndex() {
			return destIndex;
		}
	}

	public static class RemoveChild extends UINodeChange {
		public RemoveChild(@NotNull UINode removed) {
			super(removed, Type.RemoveChild);
		}
	}
}
