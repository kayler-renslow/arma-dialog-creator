/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.*;

/**
 @author Kayler
 Interface that specifies something that is displayable in preview and in Arma 3 (title, dialog, display)
 Created on 06/14/2016. */
public class ArmaDisplay implements Display<ArmaControl>{

	private int idd;
	private boolean movingEnable, enableSimulation;
	private final ControlList<ArmaControl> controlsList = new ControlList<>(this);
	private final ControlList<ArmaControl> bgControlsList = new ControlList<>(this);
		
	public ArmaDisplay(int idd) {
		this.idd = idd;
		final ArmaDisplay display = this;
		final ControlListChangeListener<ArmaControl> controlListListener = new ControlListChangeListener<ArmaControl>() {
			@Override
			public void onChanged(ControlList<ArmaControl> controlList, ControlListChange<ArmaControl> change) {
				if(change.wasAdded()){
					change.getAdded().getControl().setParent(display);
					change.getAdded().getControl().setDisplay(display);
				}
				if(change.wasSet()){
					change.getSet().getNewControl().setParent(display);
					change.getSet().getNewControl().setDisplay(display);
				}
			}
		};
		controlsList.addChangeListener(controlListListener);
		bgControlsList.addChangeListener(controlListListener);
	}

	public int getIdd() {
		return idd;
	}

	public void setIdd(int idd) {
		this.idd = idd;
	}

	/** Return true if the display/dialog is allowed to move. If it isn't, return false. */
	public boolean movingEnabled() {
		return movingEnable;
	}

	public void setMovingEnable(boolean movingEnable) {
		this.movingEnable = movingEnable;
	}

	/** Return true if the display/dialog has user interaction. If no interaction is allowed, return false. */
	public boolean simulationEnabled() {
		return enableSimulation;
	}

	public void setEnableSimulation(boolean enableSimulation) {
		this.enableSimulation = enableSimulation;
	}
	
	@Override
	public ControlList<ArmaControl> getBackgroundControls() {
		return bgControlsList;
	}
	
	@Override
	public void resolutionUpdate(Resolution newResolution) {
		for(ArmaControl control : bgControlsList){
			control.resolutionUpdate(newResolution);
		}
		for(ArmaControl control : controlsList){
			control.resolutionUpdate(newResolution);
		}
	}
	
	/** Get all controls. If simulation isn't enabled, return the controls regardless. */
	public ControlList<ArmaControl> getControls() {
		return controlsList;
	}
	
	
}
