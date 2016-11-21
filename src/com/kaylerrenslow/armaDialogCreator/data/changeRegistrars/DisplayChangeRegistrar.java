/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data.changeRegistrars;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.data.*;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.*;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

/**
 Tracks {@link ArmaControl} movement changes made to {@link Project#getEditingDisplay()}. The movement changes are tracked with {@link DisplayControlList#getUpdateGroup()}

 @author Kayler
 @since 08/10/2016 */
public class DisplayChangeRegistrar implements ChangeRegistrar {
	private boolean disableListener = false;

	public DisplayChangeRegistrar(@NotNull ApplicationData data) {
		final Changelog changelog = Changelog.getInstance();

		ArmaDisplay display = data.getCurrentProject().getEditingDisplay();
		UpdateGroupListener<ControlListChange<ArmaControl>> listChangeListener = new UpdateGroupListener<ControlListChange<ArmaControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlListChange<ArmaControl>> group, ControlListChange<ArmaControl> change) {
				if (disableListener) {
					return;
				}
				if (change.wasMoved() && change.getMoved().isOriginalUpdate()) {
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
		ControlList<ArmaControl> modifiedList = change.getListChange().getModifiedList();
		switch (change.getChangeType()) {
			case ADD: {
				ControlAdd<ArmaControl> added = change.getListChange().getAdded();
				modifiedList.remove(added.getControl());
				break;
			}
			case SET: {
				ControlSet<ArmaControl> set = change.getListChange().getSet();
				modifiedList.set(set.getIndex(), set.getOldControl());
				break;
			}
			case REMOVE: {
				ControlRemove<ArmaControl> removed = change.getListChange().getRemoved();
				modifiedList.add(removed.getIndex(), removed.getControl());
				break;
			}
			case MOVE: {
				ControlMove<ArmaControl> moved = change.getListChange().getMoved();
				moved.getDestinationList().move(moved.getMovedControl(), moved.getOldList(), moved.getOldIndex());
				break;
			}
			default: {
				throw new IllegalStateException("unexpected change type:" + change.getChangeType());
			}

		}
		disableListener = false;
	}

	@Override
	public void redo(@NotNull Change c) throws ChangeUpdateFailedException {
		disableListener = true;
		DisplayControlChange change = (DisplayControlChange) c;
		ControlList<ArmaControl> modifiedList = change.getListChange().getModifiedList();
		switch (change.getChangeType()) {
			case ADD: {
				ControlAdd<ArmaControl> added = change.getListChange().getAdded();
				modifiedList.add(added.getIndex(), added.getControl());
				break;
			}
			case SET: {
				ControlSet<ArmaControl> set = change.getListChange().getSet();
				modifiedList.set(set.getIndex(), set.getNewControl());
				break;
			}
			case REMOVE: {
				ControlRemove<ArmaControl> removed = change.getListChange().getRemoved();
				modifiedList.remove(removed.getControl());
				break;
			}
			case MOVE: {
				ControlMove<ArmaControl> moved = change.getListChange().getMoved();
				moved.getOldList().move(moved.getMovedControl(), moved.getDestinationList(), moved.getDestinationIndex());
				break;
			}
			default: {
				throw new IllegalStateException("unexpected change type:" + change.getChangeType());
			}

		}
		disableListener = false;
	}

	private static class DisplayControlChange implements Change {

		private final DisplayChangeRegistrar registrar;
		private final ControlListChange<ArmaControl> controlControlListChange;
		private final String shortName;
		private final String description;

		public DisplayControlChange(DisplayChangeRegistrar registrar, ControlListChange<ArmaControl> controlControlListChange) {
			this.registrar = registrar;
			this.controlControlListChange = controlControlListChange;

			final ControlListChangeType changeType = controlControlListChange.getChangeType();
			switch (changeType) {
				case ADD: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.add");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.add_f"), controlControlListChange.getAdded().getControl().getClassName());
					break;
				}
				case SET: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.set");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.set_f"),
							controlControlListChange.getSet().getOldControl().getClassName(),
							controlControlListChange.getSet().getNewControl().getClassName()
					);
					break;
				}
				case REMOVE: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.remove");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.remove_f"), controlControlListChange.getRemoved().getControl().getClassName());
					break;
				}
				case MOVE: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.move");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.move_f"), controlControlListChange.getMoved().getMovedControl().getClassName());
					break;
				}
				default: {
					throw new IllegalStateException("unexpected change type:" + changeType);
				}

			}
		}

		@NotNull
		public ControlListChangeType getChangeType() {
			return controlControlListChange.getChangeType();
		}

		@NotNull
		public ControlListChange<ArmaControl> getListChange() {
			return controlControlListChange;
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
