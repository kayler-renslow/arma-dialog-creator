package com.armadialogcreator.canvas;

import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

/**
 A {@link DisplayControlList} is a {@link ControlList} that will listen to all of it's sub-{@link ControlList}
 instances in its children, which are {@link CanvasControl} instances.
 <p>
 This will also manage the {@link CanvasControl#getDisplay()} to be equal to this list's {@link #getDisplay()}

 @author Kayler
 @see ControlList
 @since 11/21/2016 */
public class DisplayControlList<C extends CanvasControl> extends ControlList<C> {

	private final UpdateListenerGroup<ControlListChange<C>> updateGroup = new UpdateListenerGroup<>();
	private final ControlListChangeListener<C> changeListener = new ControlListChangeListener<C>() {
		@Override
		@SuppressWarnings("unchecked")
		public void onChanged(ControlList<C> controlList, ControlListChange<C> change) {
			if (change.wasAdded()) {
				CanvasControl addedControl = change.getAdded().getControl();
				if (addedControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) addedControl).getControls(), true);
				}

				addedControl.getDisplayObserver().updateValue(getDisplay());
			} else if (change.wasSet()) {
				CanvasControl newControl = change.getSet().getNewControl();
				if (newControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) newControl).getControls(), true);
				}
				newControl.getDisplayObserver().updateValue(getDisplay());

				CanvasControl oldControl = change.getSet().getOldControl();
				if (oldControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) oldControl).getControls(), false);
				}
				oldControl.getDisplayObserver().updateValue(null);
			} else if (change.wasRemoved()) {
				CanvasControl removedControl = change.getRemoved().getControl();
				if (removedControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) removedControl).getControls(), false);
				}
				removedControl.getDisplayObserver().updateValue(null);
			} else if (change.wasMoved()) {
				ControlMove<C> moved = change.getMoved();
				CanvasControl movedControl = moved.getMovedControl();
				if (movedControl instanceof CanvasControlGroup) {
					//if entry update, then the control moved out of the display
					//if not entry update. then the control moved in display
					setChangeListener(((CanvasControlGroup) movedControl).getControls(), !moved.isEntryUpdate());
				}
				movedControl.getDisplayObserver().updateValue(!moved.isEntryUpdate() ? getDisplay() : null);
			} else {
				throw new IllegalStateException("unhandled change type=" + change.getChangeType());
			}
			updateGroup.update(change);
		}
	};

	private final CanvasDisplay<C> display;

	public DisplayControlList(@NotNull CanvasDisplay<C> display) {
		super(display);
		this.display = display;
		setChangeListener(this, true);
	}

	@SuppressWarnings("unchecked")
	private void setChangeListener(@NotNull ControlList<C> list, boolean add) {
		list.deepIterator().forEach(c -> {
			if (c instanceof CanvasControlGroup) {
				if (add) {
					((CanvasControlGroup) c).getControls().addChangeListener(changeListener);
				} else {
					((CanvasControlGroup) c).getControls().removeChangeListener(changeListener);
				}
			}
			if (add) {
				c.getDisplayObserver().updateValue(getDisplay());
			} else {
				c.getDisplayObserver().updateValue(null);
			}
		});
		if (add) {
			list.addChangeListener(changeListener);
		} else {
			list.removeChangeListener(changeListener);
		}
	}

	/**
	 Get an update group that listens for {@link ControlListChange} between all controls
	 in the list and in the {@link CanvasControlGroup#getControls()}.
	 <p>
	 The data send for each {@link UpdateGroupListener#update(UpdateListenerGroup, Object)} shouldn't
	 ever be null.

	 @return the group instance
	 */
	@NotNull
	public UpdateListenerGroup<ControlListChange<C>> getUpdateGroup() {
		return updateGroup;
	}

	@NotNull
	public CanvasDisplay<C> getDisplay() {
		return display;
	}
}
