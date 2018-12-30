package com.armadialogcreator.gui.uicanvas;

import org.jetbrains.annotations.NotNull;

/**
 @see ControlList#add(CanvasControl)
 @author Kayler
 @since 08/12/2016
 */
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
