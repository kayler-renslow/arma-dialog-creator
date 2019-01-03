package com.armadialogcreator.canvas;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ControlList#remove(int)
 @since 08/12/2016 */
public class ControlRemove<C extends CanvasControl> {
	private final C removed;
	private final int index;

	public ControlRemove(C removed, int index) {
		this.removed = removed;
		this.index = index;
	}

	@NotNull
	public C getControl() {
		return removed;
	}

	public int getIndex() {
		return index;
	}
}
