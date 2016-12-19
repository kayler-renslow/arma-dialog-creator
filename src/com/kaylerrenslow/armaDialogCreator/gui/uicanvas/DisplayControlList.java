package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

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
						addChangeListenerToChild(moved.getMovedControl(), true);
					} else {
						addChangeListenerToChild(moved.getMovedControl(), false);
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
					ControlAdd<C> added = change.getAdded();
					if (added.getControl() instanceof CanvasControlGroup) {
						((CanvasControlGroup) added.getControl()).setDisplayForGroup(display);
					}
					added.getControl().getHolderObserver().updateValue(controlList.getHolder());
					added.getControl().getDisplayObserver().updateValue(display);
				} else if (change.wasSet()) {
					ControlSet<C> set = change.getSet();
					if (set.getNewControl() instanceof CanvasControlGroup) {
						((CanvasControlGroup) set.getNewControl()).setDisplayForGroup(display);
					}
					set.getNewControl().getHolderObserver().updateValue(controlList.getHolder());
					set.getNewControl().getDisplayObserver().updateValue(display);
				} else if (change.wasMoved() && (change.getMoved().getDestinationHolder() == display)) {
					ControlMove<C> moved = change.getMoved();
					if (moved.getMovedControl() instanceof CanvasControlGroup) {
						((CanvasControlGroup) moved.getMovedControl()).setDisplayForGroup(display);
					}
					moved.getMovedControl().getHolderObserver().updateValue(controlList.getHolder());
					moved.getMovedControl().getDisplayObserver().updateValue(display);
				} else if (change.wasRemoved()) {
					change.getRemoved().getControl().getHolderObserver().updateValue(null);
					change.getRemoved().getControl().getDisplayObserver().updateValue(null);
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
