package com.armadialogcreator.canvas;

import com.armadialogcreator.util.*;
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

	private final UpdateListenerGroup<ListObserverChange<C>> updateGroup = new UpdateListenerGroup<>();
	private final ListObserverListener<C> changeListener = new ListObserverListener<C>() {
		@Override
		@SuppressWarnings("unchecked")
		public void onChanged(@NotNull ListObserver<C> list, @NotNull ListObserverChange<C> change) {
			if (change.wasAdded()) {
				CanvasControl addedControl = change.getAdded().getAdded();
				if (addedControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) addedControl).getControls(), true);
				}

				addedControl.getDisplayObserver().updateValue(getDisplay());
			} else if (change.wasSet()) {
				CanvasControl newControl = change.getSet().getNew();
				if (newControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) newControl).getControls(), true);
				}
				newControl.getDisplayObserver().updateValue(getDisplay());

				CanvasControl oldControl = change.getSet().getOld();
				if (oldControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) oldControl).getControls(), false);
				}
				oldControl.getDisplayObserver().updateValue(null);
			} else if (change.wasRemoved()) {
				CanvasControl removedControl = change.getRemoved().getRemoved();
				if (removedControl instanceof CanvasControlGroup) {
					setChangeListener(((CanvasControlGroup) removedControl).getControls(), false);
				}
				removedControl.getDisplayObserver().updateValue(null);
			} else if (change.wasMoved()) {
				ListObserverChangeMove<C> moved = change.getMoved();
				CanvasControl movedControl = moved.getMoved();
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
					((CanvasControlGroup) c).getControls().addListener(changeListener);
				} else {
					((CanvasControlGroup) c).getControls().removeListener(changeListener);
				}
			}
			if (add) {
				c.getDisplayObserver().updateValue(getDisplay());
			} else {
				c.getDisplayObserver().updateValue(null);
			}
		});
		if (add) {
			list.addListener(changeListener);
		} else {
			list.removeListener(changeListener);
		}
	}

	/**
	 Get an update group that listens for {@link ListObserverChange} between all controls
	 in the list and in the {@link CanvasControlGroup#getControls()}.
	 <p>
	 The data send for each {@link UpdateGroupListener#update(UpdateListenerGroup, Object)} shouldn't
	 ever be null.

	 @return the group instance
	 */
	@NotNull
	public UpdateListenerGroup<ListObserverChange<C>> getUpdateGroup() {
		return updateGroup;
	}

	@NotNull
	public CanvasDisplay<C> getDisplay() {
		return display;
	}
}
