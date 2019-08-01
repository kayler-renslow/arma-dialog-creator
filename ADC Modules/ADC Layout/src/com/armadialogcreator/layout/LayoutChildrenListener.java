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
				added.getAdded().assignBounds(new Bounds(added.getAdded(), layout));
				break;
			}
			case Set: {
				ListObserverChangeSet<LayoutNode> set = change.getSet();
				set.getNew().assignBounds(new Bounds(set.getNew(), layout));
				break;
			}
			case Move: {
				ListObserverChangeMove<LayoutNode> moved = change.getMoved();
				if (!moved.isSourceListChange()) {
					moved.getMoved().assignBounds(new Bounds(moved.getMoved(), layout));
				}
				break;
			}
			case Clear: //fall
			case Remove: {
				break;
			}
		}
	}
}
