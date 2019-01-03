package com.armadialogcreator.canvas;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see ControlList#add(CanvasControl)
 @since 08/12/2016 */
public class ControlAdd<C extends CanvasControl> {
	private final C added;
	private final int index;

	public ControlAdd(C added, int index) {
		this.added = added;
		this.index = index;
	}

	@NotNull
	public C getControl() {
		return added;
	}

	public int getIndex() {
		return index;
	}
}
