package com.armadialogcreator.canvas;

import com.armadialogcreator.util.ValueObserver;

import java.util.HashMap;

/**
 Created by Kayler on 05/17/2016.
 */
public class Keys {
	private HashMap<String, Boolean> map = new HashMap<>();
	private boolean shiftDown, ctrlDown, altDown;
	private ValueObserver<Boolean> keyStateObserver = new ValueObserver<>(false);

	public void update(String key, boolean keyIsDown, boolean shiftDown, boolean ctrlDown, boolean altDown) {
		Boolean old = this.map.put(key, keyIsDown);
		this.shiftDown = shiftDown;
		this.ctrlDown = ctrlDown;
		this.altDown = altDown;
		if (old == null || old != keyIsDown) {
			keyStateObserver.updateValue(keyIsDown); //value changed
		}
	}

	/** For each key update, this observer is notified with the boolean value=true if a key was pressed, or false if a key was released */
	public ValueObserver<Boolean> getKeyStateObserver() {
		return keyStateObserver;
	}

	public boolean spaceDown() {
		return keyIsDown(" ");
	}

	public boolean keyIsDown(String k) {
		Boolean b = map.get(k);
		return b != null && b;
	}

	public boolean isShiftDown() {
		return shiftDown;
	}

	public boolean isCtrlDown() {
		return ctrlDown;
	}

	public boolean isAltDown() {
		return altDown;
	}
}
