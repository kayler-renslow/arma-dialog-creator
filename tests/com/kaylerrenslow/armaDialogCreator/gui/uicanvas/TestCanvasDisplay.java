package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;

import java.util.Iterator;

/**
 @author Kayler
 @since 06/19/2017 */
public class TestCanvasDisplay implements CanvasDisplay<TestCanvasControl> {
	private final DisplayControlList<TestCanvasControl> backgroundControls = new DisplayControlList<>(this);
	private final DisplayControlList<TestCanvasControl> controls = new DisplayControlList<>(this);

	@Override
	public DataContext getUserData() {
		return null;
	}

	@Override
	public DisplayControlList<TestCanvasControl> getBackgroundControls() {
		return backgroundControls;
	}

	@Override
	public DisplayControlList<TestCanvasControl> getControls() {
		return controls;
	}

	@Override
	public Iterator<TestCanvasControl> iteratorForAllControls(boolean backwards) {
		return null;
	}

	@Override
	public void resolutionUpdate(Resolution newResolution) {

	}
}
