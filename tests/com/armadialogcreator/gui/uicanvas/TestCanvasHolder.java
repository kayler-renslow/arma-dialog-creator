package com.armadialogcreator.gui.uicanvas;

import com.armadialogcreator.util.DataContext;

/**
 @author Kayler
 @since 06/18/2017 */
public class TestCanvasHolder implements ControlHolder<TestCanvasControl> {
	private ControlList<TestCanvasControl> list;

	public TestCanvasHolder() {
		this.list = new ControlList<>(this);
	}

	@Override
	public ControlList<TestCanvasControl> getControls() {
		return list;
	}

	@Override
	public DataContext getUserData() {
		return null;
	}
}
