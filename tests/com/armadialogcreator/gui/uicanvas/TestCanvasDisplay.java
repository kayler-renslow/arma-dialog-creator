package com.armadialogcreator.gui.uicanvas;

import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

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

	@NotNull
	@Override
	public DisplayControlList<TestCanvasControl> getBackgroundControls() {
		return backgroundControls;
	}

	@NotNull
	@Override
	public DisplayControlList<TestCanvasControl> getControls() {
		return controls;
	}

	@NotNull
	@Override
	public Iterator<TestCanvasControl> iteratorForAllControls(boolean backwards) {
		return null;
	}

	@Override
	public void resolutionUpdate(@NotNull Resolution newResolution) {

	}

	@NotNull
	@Override
	public UpdateListenerGroup<TestCanvasControl> getReRenderUpdateGroup() {
		return null;
	}
}
