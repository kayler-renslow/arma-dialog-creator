package com.armadialogcreator.data.changeRegistrars;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaDisplay;
import com.armadialogcreator.data.olddata.*;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;

/**
 Tracks {@link ArmaControl} movement changes made to {@link Project#getEditingDisplay()}. The movement changes are tracked with {@link DisplayControlList#getUpdateGroup()}

 @author Kayler
 @since 08/10/2016 */
public class DisplayChangeRegistrar implements ChangeRegistrar {
	private boolean disableListener = false;

	public DisplayChangeRegistrar(@NotNull ApplicationData data) {
		final Changelog changelog = data.getChangelog();

		ArmaDisplay display = data.getCurrentProject().getEditingDisplay();
		UpdateGroupListener<ListObserverChange<ArmaControl>> listChangeListener = new UpdateGroupListener<ListObserverChange<ArmaControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<ArmaControl>> group, @NotNull ListObserverChange<ArmaControl> change) {
				if (disableListener) {
					return;
				}
				if (change.wasMoved() && change.getMoved().isEntryUpdate()) {
					return; //only register the change once (register the change when the old list is notified and not the destination list)
				}
				changelog.addChange(new DisplayControlChange(DisplayChangeRegistrar.this, change));
			}
		};
		display.getControls().getUpdateGroup().addListener(listChangeListener);
		display.getBackgroundControls().getUpdateGroup().addListener(listChangeListener);

	}

	@Override
	public void undo(@NotNull Change c) throws ChangeUpdateFailedException {
		disableListener = true;
		DisplayControlChange change = (DisplayControlChange) c;
		ListObserver<ArmaControl> modifiedList = change.getListChange().getModifiedListObserver();
		switch (change.getChangeType()) {
			case Add: {
				ListObserverChangeAdd<ArmaControl> added = change.getListChange().getAdded();
				modifiedList.remove(added.getAdded());
				break;
			}
			case Set: {
				ListObserverChangeSet<ArmaControl> set = change.getListChange().getSet();
				modifiedList.set(set.getIndex(), set.getOld());
				break;
			}
			case Remove: {
				ListObserverChangeRemove<ArmaControl> removed = change.getListChange().getRemoved();
				modifiedList.add(removed.getIndex(), removed.getRemoved());
				break;
			}
			case Move: {
				ListObserverChangeMove<ArmaControl> moved = change.getListChange().getMoved();
				moved.getDestinationList().move(moved.getMoved(), moved.getOldList(), moved.getOldIndex());
				break;
			}
			default: {
				throw new IllegalStateException();
			}

		}
		disableListener = false;
	}

	@Override
	public void redo(@NotNull Change c) throws ChangeUpdateFailedException {
		disableListener = true;
		DisplayControlChange change = (DisplayControlChange) c;
		ListObserver<ArmaControl> modifiedList = change.getListChange().getModifiedListObserver();
		switch (change.getChangeType()) {
			case Add: {
				ListObserverChangeAdd<ArmaControl> added = change.getListChange().getAdded();
				modifiedList.add(added.getIndex(), added.getAdded());
				break;
			}
			case Set: {
				ListObserverChangeSet<ArmaControl> set = change.getListChange().getSet();
				modifiedList.set(set.getIndex(), set.getNew());
				break;
			}
			case Remove: {
				ListObserverChangeRemove<ArmaControl> removed = change.getListChange().getRemoved();
				modifiedList.remove(removed.getRemoved());
				break;
			}
			case Move: {
				ListObserverChangeMove<ArmaControl> moved = change.getListChange().getMoved();
				moved.getOldList().move(moved.getMoved(), moved.getDestinationList(), moved.getDestinationIndex());
				break;
			}
			default: {
				throw new IllegalStateException();
			}

		}
		disableListener = false;
	}

	private static class DisplayControlChange implements Change {

		private final DisplayChangeRegistrar registrar;
		private final ListObserverChange<ArmaControl> listChange;
		private final String shortName;
		private final String description;

		public DisplayControlChange(DisplayChangeRegistrar registrar, ListObserverChange<ArmaControl> change) {
			this.registrar = registrar;
			this.listChange = change;

			switch (change.getChangeType()) {
				case Add: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.add");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.add_f"), change.getAdded().getAdded().getClassName());
					break;
				}
				case Set: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.set");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.set_f"),
							change.getSet().getOld().getClassName(),
							change.getSet().getNew().getClassName()
					);
					break;
				}
				case Remove: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.remove");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.remove_f"), change.getRemoved().getRemoved().getClassName());
					break;
				}
				case Move: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.move");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.move_f"), change.getMoved().getMoved().getClassName());
					break;
				}
				default: {
					throw new IllegalStateException("unexpected change type:" + change.getChangeType());
				}

			}
		}

		@NotNull
		public ListObserverChange.ChangeType getChangeType() {
			return listChange.getChangeType();
		}

		@NotNull
		public ListObserverChange<ArmaControl> getListChange() {
			return listChange;
		}

		@NotNull
		@Override
		public String getShortName() {
			return shortName;
		}

		@NotNull
		@Override
		public String getDescription() {
			return description;
		}

		@NotNull
		@Override
		public ChangeRegistrar getRegistrar() {
			return registrar;
		}
	}


}
