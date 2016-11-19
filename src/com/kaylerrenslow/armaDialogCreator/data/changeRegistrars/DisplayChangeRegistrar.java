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
import com.kaylerrenslow.armaDialogCreator.data.*;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlList;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlListChange;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlListChangeListener;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlListChangeType;
import com.kaylerrenslow.armaDialogCreator.main.Lang;

/**
 Created by Kayler on 08/10/2016.
 */
public class DisplayChangeRegistrar implements ChangeRegistrar {
	public DisplayChangeRegistrar(ApplicationData data) {
		final DisplayChangeRegistrar self = this;
		final Changelog changelog = Changelog.getInstance();
		final ControlListChangeListener<ArmaControl> controlListChangeListener = new ControlListChangeListener<ArmaControl>() {
			@Override
			public void onChanged(ControlList<ArmaControl> controlList, ControlListChange<ArmaControl> change) {
				if (change.wasMoved() && change.getMoved().getDestinationList() == controlList) {
					return; //only register the change once (register the change when the old list is notified and not the destination list)
				}
				changelog.addChange(new DisplayControlChange(self, controlList == data.getCurrentProject().getEditingDisplay().getBackgroundControls(), change));
			}
		};
		data.getCurrentProject().getEditingDisplay().getBackgroundControls().addChangeListener(controlListChangeListener);
		data.getCurrentProject().getEditingDisplay().getControls().addChangeListener(controlListChangeListener);
	}

	@Override
	public void undo(Change c) throws ChangeUpdateFailedException {
		DisplayControlChange change = (DisplayControlChange) c;
	}

	@Override
	public void redo(Change c) throws ChangeUpdateFailedException {
		DisplayControlChange change = (DisplayControlChange) c;
	}

	private static class DisplayControlChange implements Change {

		private final DisplayChangeRegistrar registrar;
		private final boolean background;
		private final ControlListChange<ArmaControl> controlControlListChange;
		private final String shortName;
		private final String description;

		public DisplayControlChange(DisplayChangeRegistrar registrar, boolean background, ControlListChange<ArmaControl> controlControlListChange) {
			this.registrar = registrar;
			this.background = background;
			this.controlControlListChange = controlControlListChange;

			final ControlListChangeType changeType = controlControlListChange.getChangeType();
			switch (changeType) {
				case ADD: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.add");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.add"), controlControlListChange.getAdded().getControl().getClassName());
					break;
				}
				case SET: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.set");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.set"),
							controlControlListChange.getSet().getOldControl().getClassName(),
							controlControlListChange.getSet().getNewControl().getClassName()
					);
					break;
				}
				case REMOVE: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.remove");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.remove"), controlControlListChange.getRemoved().getControl().getClassName());
					break;
				}
				case MOVE: {
					shortName = Lang.EditChangeBundle().getString("DisplayChange.ShortName.move");
					description = String.format(Lang.EditChangeBundle().getString("DisplayChange.Description.move"), controlControlListChange.getMoved().getMovedControl().getClassName());
					break;
				}
				default: {
					throw new IllegalStateException("unexpected change type:" + changeType);
				}

			}
		}

		public boolean isBackground() {
			return background;
		}

		public ControlListChangeType getChangeType() {
			return controlControlListChange.getChangeType();
		}

		@Override
		public String getShortName() {
			return shortName;
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public ChangeRegistrar getRegistrar() {
			return registrar;
		}
	}


}
