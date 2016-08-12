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
import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.data.Change;
import com.kaylerrenslow.armaDialogCreator.data.ChangeRegistrar;
import com.kaylerrenslow.armaDialogCreator.data.ChangeUpdateFailedException;
import com.kaylerrenslow.armaDialogCreator.main.lang.ChangeLang;

/**
 Created by Kayler on 08/10/2016.
 */
public class DisplayChangeRegistrar implements ChangeRegistrar {
	public DisplayChangeRegistrar(ApplicationData data) {
		DisplayChangeRegistrar self = this;
//		data.getCurrentProject().getEditingDisplay().getBackgroundControls().addListener(new ListChangeListener<ArmaControl>() {
//			@Override
//			public void onChanged(Change<? extends ArmaControl> c) {
//				while (c.next()) {
//					for (ArmaControl control : c.getAddedSubList()) {
//						Changelog.getInstance().addChange(new DisplayControlChange(self, true, true, control));
//					}
//					for (ArmaControl control : c.getRemoved()) {
//						Changelog.getInstance().addChange(new DisplayControlChange(self, true, false, control));
//					}
//				}
//			}
//		});
//		//todo detect swap.
//		data.getCurrentProject().getEditingDisplay().getControls().addListener(new ListChangeListener<ArmaControl>() {
//			@Override
//			public void onChanged(Change<? extends ArmaControl> c) {
//				ObservableList<ArmaControl> list = data.getCurrentProject().getEditingDisplay().getControls();
//				while (c.next()) {
//					if(c.wasAdded()){
//						for(int i = c.getFrom(); i < c.getTo(); i++){
//							Changelog.getInstance().addChange(new DisplayControlChange(self, false, true, list.get(i), i));
//						}
//					}else if(c.wasRemoved()){
//
//					}
//					for (ArmaControl control : c.getAddedSubList()) {
//					}
//					for (ArmaControl control : c.getRemoved()) {
//						Changelog.getInstance().addChange(new DisplayControlChange(self, false, false, control));
//					}
//				}
//			}
//		});
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
		private final boolean added;
		private final ArmaControl control;
		private final int controlIndex;
		
		public DisplayControlChange(DisplayChangeRegistrar registrar, boolean background, boolean added, ArmaControl control, int controlIndex) {
			this.registrar = registrar;
			this.background = background;
			this.added = added;
			this.control = control;
			this.controlIndex = controlIndex;
		}
		
		public int getControlIndex() {
			return controlIndex;
		}
		
		public ArmaControl getControl() {
			return control;
		}
		
		public boolean isBackground() {
			return background;
		}
		
		public boolean isAdded() {
			return added;
		}
		
		@Override
		public String getShortName() {
			if (background) {
				return isAdded() ? ChangeLang.DisplayChange.Background.ControlAdd.SHORT_NAME : ChangeLang.DisplayChange.Background.ControlRemove.SHORT_NAME;
			}
			return isAdded() ? ChangeLang.DisplayChange.Main.ControlAdd.SHORT_NAME : ChangeLang.DisplayChange.Main.ControlRemove.SHORT_NAME;
		}
		
		@Override
		public String getDescription() {
			String f;
			if (background) {
				f = isAdded() ? ChangeLang.DisplayChange.Background.ControlAdd.DESCRIPTION_F : ChangeLang.DisplayChange.Background.ControlRemove.DESCRIPTION_F;
			} else {
				f = isAdded() ? ChangeLang.DisplayChange.Main.ControlAdd.DESCRIPTION_F : ChangeLang.DisplayChange.Main.ControlRemove.DESCRIPTION_F;
			}
			return String.format(f, getControl().getClassName());
		}
		
		@Override
		public ChangeRegistrar getRegistrar() {
			return registrar;
		}
	}
	
	
}
