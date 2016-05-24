package com.kaylerrenslow.armaDialogCreator.arma.dialog;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 The base class for all dialogs
 Created on 05/21/2016. */
public class ArmaDialog {
	protected int idd;
	protected boolean movingEnable, enableSimulation;
	private List<ArmaControl> controls, objects, backgroundControls;

	public ArmaDialog(int idd) {
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

	public boolean isMovingEnable() {
		return movingEnable;
	}

	public void setMovingEnable(boolean movingEnable) {
		this.movingEnable = movingEnable;
	}

	public boolean isEnableSimulation() {
		return enableSimulation;
	}

	public void setEnableSimulation(boolean enableSimulation) {
		this.enableSimulation = enableSimulation;
	}

	public List<ArmaControl> getControls() {
		return controls;
	}

	public List<ArmaControl> getObjects() {
		return objects;
	}

	public List<ArmaControl> getBackgroundControls() {
		return backgroundControls;
	}
}
