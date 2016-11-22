/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

/**
 A {@link DisplayControlList} is a {@link ControlList} that will listen to all of it's sub-{@link ControlList} instances in its children.

 @author Kayler
 @since 11/21/2016 */
public class DisplayControlList<C extends CanvasControl> extends ControlList<C> {

	private final ControlListChangeListener<C> controlListChangeListener = new ControlListChangeListener<C>() {
		@Override
		public void onChanged(ControlList<C> controlList, ControlListChange<C> change) {
			switch (change.getChangeType()) {
				case ADD: {
					addChangeListenerToChild(change.getAdded().getControl(), true);
					break;
				}
				case REMOVE: {
					addChangeListenerToChild(change.getRemoved().getControl(), false);
					break;
				}
				case SET: {
					addChangeListenerToChild(change.getSet().getOldControl(), false);
					addChangeListenerToChild(change.getSet().getNewControl(), true);
					break;
				}
				case MOVE: {
					ControlMove<C> moved = change.getMoved();
					if (moved.isOriginalUpdate()) {
						addChangeListenerToChild(change.getMoved().getMovedControl(), false);
					} else {
						addChangeListenerToChild(change.getMoved().getMovedControl(), true);
					}

					break;
				}
				default: {
					throw new IllegalStateException("unexpected change type:" + change.getChangeType());
				}
			}
			updateGroup.update(change);
		}
	};

	private UpdateListenerGroup<ControlListChange<C>> updateGroup = new UpdateListenerGroup<>();

	public DisplayControlList(@NotNull CanvasDisplay<C> display) {
		super(display);
		addChangeListener(controlListChangeListener);
		addChangeListener(new ControlListChangeListener<C>() {
			@Override
			@SuppressWarnings("unchecked")
			public void onChanged(ControlList<C> controlList, ControlListChange<C> change) {
				if (change.wasAdded()) {
					if (change.getAdded().getControl() instanceof CanvasControlGroup) {
						((CanvasControlGroup) change.getAdded().getControl()).setDisplayForGroup(display);
					}
					change.getAdded().getControl().getHolderObserver().updateValue(controlList.getHolder());
					change.getAdded().getControl().getDisplayObserver().updateValue((CanvasDisplay) display);
				} else if (change.wasSet()) {
					if (change.getSet().getNewControl() instanceof CanvasControlGroup) {
						((CanvasControlGroup) change.getSet().getNewControl()).setDisplayForGroup(display);
					}
					change.getSet().getNewControl().getHolderObserver().updateValue(controlList.getHolder());
					change.getSet().getNewControl().getDisplayObserver().updateValue((CanvasDisplay) display);
				} else if (change.wasMoved() && (change.getMoved().getDestinationHolder() == display)) {
					if (change.getMoved().getMovedControl() instanceof CanvasControlGroup) {
						((CanvasControlGroup) change.getMoved().getMovedControl()).setDisplayForGroup(display);
					}
					change.getMoved().getMovedControl().getHolderObserver().updateValue(controlList.getHolder());
					change.getMoved().getMovedControl().getDisplayObserver().updateValue((CanvasDisplay) display);
				}
			}
		});
	}

	@NotNull
	public UpdateListenerGroup<ControlListChange<C>> getUpdateGroup() {
		return updateGroup;
	}

	@SuppressWarnings("unchecked")
	private void addChangeListenerToChild(CanvasControl c, boolean activate) {
		if (c instanceof CanvasControlGroup) {
			CanvasControlGroup<C> controlGroup = (CanvasControlGroup<C>) c;
			if (activate) {
				controlGroup.getControls().addChangeListener(controlListChangeListener);
				controlGroup.getControls().addChangeListener(new GroupHolderListener<>(controlGroup));
			} else {
				controlGroup.getControls().removeChangeListener(controlListChangeListener);
				controlGroup.getControls().removeChangeListener(new GroupHolderListener<>(controlGroup));
			}
			for (CanvasControl c1 : controlGroup.getControls()) {
				addChangeListenerToChild(c1, activate);
			}
		}
	}

	private static class GroupHolderListener<C extends CanvasControl> implements ControlListChangeListener<C> {

		private final CanvasControlGroup<C> controlGroup;

		public GroupHolderListener(@NotNull CanvasControlGroup<C> controlGroup) {
			this.controlGroup = controlGroup;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void onChanged(ControlList<C> controlList, ControlListChange<C> change) {
			//do not set the display in here
			if (change.wasAdded()) {
				change.getAdded().getControl().getHolderObserver().updateValue((ControlHolder) controlGroup);
			} else if (change.wasSet()) {
				change.getSet().getNewControl().getHolderObserver().updateValue((ControlHolder) controlGroup);
			} else if (change.wasMoved() && change.getMoved().getDestinationList() == controlGroup.getControls()) {
				change.getMoved().getMovedControl().getHolderObserver().updateValue((ControlHolder) controlGroup);
			}
		}

		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof GroupHolderListener) {
				GroupHolderListener other = (GroupHolderListener) o;
				return other.controlGroup.equals(this.controlGroup);
			}
			return false;
		}
	}
}
