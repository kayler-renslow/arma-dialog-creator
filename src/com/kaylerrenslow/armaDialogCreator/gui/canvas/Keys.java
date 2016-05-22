package com.kaylerrenslow.armaDialogCreator.gui.canvas;

import java.util.HashMap;

/**
 Created by Kayler on 05/17/2016.
 */
public class Keys  {
	private HashMap<String, Boolean> map = new HashMap<>();
	private boolean shiftDown, ctrlDown, altDown;

	public void update(String key, boolean keyIsDown, boolean shiftDown, boolean ctrlDown, boolean altDown) {
		this.map.put(key, keyIsDown);
		this.shiftDown = shiftDown;
		this.ctrlDown = ctrlDown;
		this.altDown = altDown;
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
