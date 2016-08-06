/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.canvas;

import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;

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
