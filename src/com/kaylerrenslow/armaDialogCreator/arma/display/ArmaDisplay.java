package com.kaylerrenslow.armaDialogCreator.arma.display;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 Interface that specifies something that is displayable in preview and in Arma 3 (title, dialog, display)
 Created on 06/14/2016. */
public class ArmaDisplay {
	protected int idd;
	protected boolean movingEnable, enableSimulation;
	private List<ArmaControl> controls, objects, backgroundControls;

	public ArmaDisplay(int idd) {
		this.idd = idd;
		controls = new ArrayList<>();
		objects = new ArrayList<>();
		backgroundControls = new ArrayList<>();
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

	/** Get all controls. If simulation isn't enabled, return the controls regardless. */
	public List<ArmaControl> getControls() {
		return controls;
	}

	/** Get all objects. */
	public List<ArmaControl> getObjects() {
		return objects;
	}

	/** Get all controls that will always not have simulation/interaction (these are rendered before controls) */
	public List<ArmaControl> getBackgroundControls() {
		return backgroundControls;
	}

}
