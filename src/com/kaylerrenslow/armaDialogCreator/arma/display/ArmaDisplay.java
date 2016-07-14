package com.kaylerrenslow.armaDialogCreator.arma.display;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 @author Kayler
 Interface that specifies something that is displayable in preview and in Arma 3 (title, dialog, display)
 Created on 06/14/2016. */
public class ArmaDisplay {

	public enum DisplayUpdate {
		ADD_CONTROL, REMOVE_CONTROL
	}

	private int idd;
	private boolean movingEnable, enableSimulation;
	private ObservableList<ArmaControl> controls;
	private UpdateListenerGroup<DisplayUpdate> updateGroup = new UpdateListenerGroup<>();

	public ArmaDisplay(int idd) {
		this.idd = idd;
		controls = FXCollections.observableList(new ArrayList<>());
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

	public UpdateListenerGroup<DisplayUpdate> getUpdateListenerGroup() {
		return updateGroup;
	}

	/** Get all controls. If simulation isn't enabled, return the controls regardless. */
	public ObservableList<ArmaControl> getControls() {
		return controls;
	}


}
