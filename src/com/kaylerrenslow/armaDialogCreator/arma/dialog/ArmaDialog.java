package com.kaylerrenslow.armaDialogCreator.arma.dialog;

import com.kaylerrenslow.armaDialogCreator.arma.header.IHeaderEntry;

/**
 @author Kayler
 The base class for all dialogs
 Created on 05/21/2016.
 */
public class ArmaDialog {
	protected int idd;
	protected boolean movingEnable, enableSimulation;
	protected IHeaderEntry controlsBackground, controls, objects;

	public ArmaDialog(int idd) {
		this.idd = idd;
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
}
