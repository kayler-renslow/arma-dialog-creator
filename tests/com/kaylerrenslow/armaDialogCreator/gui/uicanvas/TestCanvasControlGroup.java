package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 06/18/2017 */
public class TestCanvasControlGroup extends TestCanvasControl implements CanvasControlGroup<TestCanvasControl> {
	private final ValueObserver<CanvasDisplay<TestCanvasControlGroup>> displayObserver = new ValueObserver<>(null);
	private final ValueObserver<ControlHolder<TestCanvasControlGroup>> holderObserver = new ValueObserver<>(null);
	private ControlList<TestCanvasControl> controlList = new ControlList<>(this);

	/**
	 @param name a unique name for the control to be identified in a test
	 */
	public TestCanvasControlGroup(@NotNull String name) {
		super(name);
	}

	@Override
	public ControlList<TestCanvasControl> getControls() {
		return controlList;
	}

	@Override
	public DataContext getUserData() {
		return null;
	}
}
