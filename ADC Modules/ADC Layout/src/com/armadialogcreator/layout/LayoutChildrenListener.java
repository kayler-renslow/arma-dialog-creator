package com.armadialogcreator.layout;

import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;

/**
 Used to manage {@link Bounds} instances for a {@link Layout#getChildren()} list

 @author Kayler
 @since 8/1/19. */
class LayoutChildrenListener implements ListObserverListener<LayoutNode> {
	private final Layout layout;

	public LayoutChildrenListener(@NotNull Layout layout) {
		this.layout = layout;
	}

	@Override
	public void onChanged(@NotNull ListObserver<LayoutNode> list, @NotNull ListObserverChange<LayoutNode> change) {
		switch (change.getChangeType()) {
			case Add: {
				ListObserverChangeAdd<LayoutNode> added = change.getAdded();
				LayoutNode addedNode = added.getAdded();
				addedNode.assignBounds(addedNode.getBounds().copy(this.layout));
				break;
			}
			case Set: {
				ListObserverChangeSet<LayoutNode> set = change.getSet();
				LayoutNode setNode = set.getNew();
				set.getOld().assignBounds(set.getOld().getBounds().copy(null));
				setNode.assignBounds(setNode.getBounds().copy(this.layout));
				break;
			}
			case Move: {
				ListObserverChangeMove<LayoutNode> moved = change.getMoved();
				if (!moved.isSourceListChange()) {
					LayoutNode movedNode = moved.getMoved();
					movedNode.assignBounds(movedNode.getBounds().copy(this.layout));
				}
				break;
			}
			case Clear: {
				for (LayoutNode n : list) {
					n.assignBounds(n.getBounds().copy(null));
				}
				break;
			}
			case Remove: {
				ListObserverChangeRemove<LayoutNode> removed = change.getRemoved();
				LayoutNode removedNode = removed.getRemoved();
				removedNode.assignBounds(removedNode.getBounds().copy(null));
				break;
			}
		}
	}
}
