package com.armadialogcreator.gui.uicanvas;

import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 Note: we do not need to check for correctness of updates because that should be
 tested thoroughly in {@link ControlListTest}

 @author Kayler
 @since 06/19/2017 */
public class DisplayControlListTest {

	@Test
	public void add() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");

		Reference<Boolean> set = new Reference<>(false);

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new RuntimeException();
				}
				set.setValue(data.wasAdded());
			}
		});

		display.getControls().add(control0);

		assertEquals("Expected an add update for controls", true, set.getValue());
		assertEquals(display, control0.getDisplay());
		assertEquals(display, control0.getHolder());
	}

	@Test
	public void set() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().add(control0);

		Reference<Boolean> set = new Reference<>(false);

		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new RuntimeException();
				}
				set.setValue(data.wasSet());
			}
		});

		display.getControls().set(0, control1);

		assertEquals("Expected a set update for controls", true, set.getValue());
		assertEquals(display, control1.getDisplay());
		assertEquals(display, control1.getHolder());
	}

	@Test
	public void move() throws Exception {
		//move a control from controls to a different spot in controls
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");

		Reference<Boolean> set = new Reference<>(false);

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().add(control0);
		display.getControls().add(control1);

		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new RuntimeException();
				}
				set.setValue(data.wasMoved());
			}
		});

		display.getControls().move(control0, 1);

		assertEquals("Expected a move update for controls", true, set.getValue());
		assertEquals(display, control0.getDisplay());
		assertEquals(display, control0.getHolder());
	}

	@Test
	public void moveControlToBackground() throws Exception {
		//move a control from controls to bg controls
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");

		Reference<Boolean> controlsUpdated = new Reference<>(false);
		Reference<Boolean> bgControlsUpdated = new Reference<>(false);

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().add(control0);
		display.getControls().add(control1);

		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				controlsUpdated.setValue(data.wasMoved());
			}
		});
		display.getBackgroundControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				bgControlsUpdated.setValue(data.wasMoved());
			}
		});

		display.getControls().move(control0, display.getBackgroundControls());

		if (controlsUpdated.getValue() == null || bgControlsUpdated.getValue() == null) {
			throw new NullPointerException();
		}

		assertEquals(
				"Expected a move update for controls and bg controls. Controls activated=" +
						controlsUpdated.getValue() + ", bg controls activated=" +
						bgControlsUpdated.getValue(),
				true,
				controlsUpdated.getValue() && bgControlsUpdated.getValue()
		);
		assertEquals(display, control0.getDisplay());
		assertEquals(display, control0.getHolder());
	}

	@Test
	public void moveGroupToBackground() throws Exception {
		//move a non-empty group from controls to bg controls
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");

		TestCanvasControlGroup group = new TestCanvasControlGroup("group");
		group.getControls().add(new TestCanvasControl("groupControl"));

		Reference<Boolean> controlsUpdated = new Reference<>(false);
		Reference<Boolean> bgControlsUpdated = new Reference<>(false);

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().add(control0);
		display.getControls().add(control1);
		display.getControls().add(group);

		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				controlsUpdated.setValue(data.wasMoved());
			}
		});
		display.getBackgroundControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				bgControlsUpdated.setValue(data.wasMoved());
			}
		});

		display.getControls().move(group, display.getBackgroundControls());

		if (controlsUpdated.getValue() == null || bgControlsUpdated.getValue() == null) {
			throw new NullPointerException();
		}

		assertEquals(
				"Expected a move update for controls and bg controls. Controls activated=" +
						controlsUpdated.getValue() + ", bg controls activated=" +
						bgControlsUpdated.getValue(),
				true,
				controlsUpdated.getValue() && bgControlsUpdated.getValue()
		);
		assertEquals(display, group.getDisplay());
		assertEquals(display, group.getHolder());
	}

	@Test
	public void moveEmptyGroupToBackground() throws Exception {
		//move an empty group from controls to bg controls
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");

		TestCanvasControlGroup group = new TestCanvasControlGroup("group");

		Reference<Boolean> controlsUpdated = new Reference<>(false);
		Reference<Boolean> bgControlsUpdated = new Reference<>(false);

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().add(control0);
		display.getControls().add(control1);
		display.getControls().add(group);

		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				controlsUpdated.setValue(data.wasMoved());
			}
		});
		display.getBackgroundControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				bgControlsUpdated.setValue(data.wasMoved());
			}
		});

		display.getControls().move(group, display.getBackgroundControls());

		if (controlsUpdated.getValue() == null || bgControlsUpdated.getValue() == null) {
			throw new NullPointerException();
		}

		assertEquals(
				"Expected a move update for controls and bg controls. Controls activated=" +
						controlsUpdated.getValue() + ", bg controls activated=" +
						bgControlsUpdated.getValue(),
				true,
				controlsUpdated.getValue() && bgControlsUpdated.getValue()
		);
		assertEquals(display, group.getDisplay());
		assertEquals(display, group.getHolder());
	}

	@Test
	public void moveGroupToGroupInBackground() throws Exception {
		//move an empty group in controls in to an empty group that is in bg controls
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");

		TestCanvasControlGroup groupInControls = new TestCanvasControlGroup("group");

		TestCanvasControlGroup groupInBackground = new TestCanvasControlGroup("groupInBackground");

		Reference<Boolean> controlsUpdated = new Reference<>(false);
		Reference<Boolean> controlInbgControlsUpdated = new Reference<>(false);
		Reference<Boolean> bgControlsUpdated = new Reference<>(false);

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().add(control0);
		display.getControls().add(control1);
		display.getControls().add(groupInControls);

		display.getBackgroundControls().add(groupInBackground);

		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				controlsUpdated.setValue(data.wasMoved());
			}
		});
		display.getBackgroundControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				bgControlsUpdated.setValue(true);
			}
		});

		groupInBackground.getControls().addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				controlInbgControlsUpdated.setValue(change.wasMoved());
			}
		});

		display.getControls().move(groupInControls, groupInBackground.getControls());

		if (controlsUpdated.getValue() == null
				|| controlInbgControlsUpdated.getValue() == null
				|| bgControlsUpdated.getValue() == null) {
			throw new NullPointerException();
		}

		assertEquals(
				"Expected a move update for controls, group in bg controls, and bg controls itself. Controls activated=" +
						controlsUpdated.getValue() + ", bg controls group activated=" +
						controlInbgControlsUpdated.getValue() +
						", bg controls activated=" + bgControlsUpdated.getValue(),
				true,
				controlsUpdated.getValue() && controlInbgControlsUpdated.getValue() && bgControlsUpdated.getValue()
		);
		assertEquals(display, groupInControls.getDisplay());
		assertEquals(groupInBackground, groupInControls.getHolder());
	}

	@Test
	public void moveGroupToGroupInBackground_emptyGroup() throws Exception {
		//move a non-empty group in controls in to an empty group that is in bg controls
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");

		TestCanvasControlGroup groupInControls = new TestCanvasControlGroup("group");
		groupInControls.getControls().add(new TestCanvasControl("groupControl"));

		TestCanvasControlGroup groupInBackground = new TestCanvasControlGroup("groupInBackground");

		Reference<Boolean> controlsUpdated = new Reference<>(false);
		Reference<Boolean> controlInbgControlsUpdated = new Reference<>(false);
		Reference<Boolean> bgControlsUpdated = new Reference<>(false);

		TestCanvasDisplay display = new TestCanvasDisplay();
		display.getControls().add(control0);
		display.getControls().add(control1);
		display.getControls().add(groupInControls);

		display.getBackgroundControls().add(groupInBackground);

		display.getControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				if (data == null) {
					throw new IllegalStateException();
				}
				controlsUpdated.setValue(data.wasMoved());
			}
		});
		display.getBackgroundControls().getUpdateGroup().addListener(new UpdateGroupListener<ListObserverChange<TestCanvasControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ListObserverChange<TestCanvasControl>> group, @Nullable ListObserverChange<TestCanvasControl> data) {
				bgControlsUpdated.setValue(true);
			}
		});

		groupInBackground.getControls().addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				controlInbgControlsUpdated.setValue(change.wasMoved());
			}
		});

		display.getControls().move(groupInControls, groupInBackground.getControls());

		if (controlsUpdated.getValue() == null
				|| controlInbgControlsUpdated.getValue() == null
				|| bgControlsUpdated.getValue() == null) {
			throw new NullPointerException();
		}

		assertEquals(
				"Expected a move update for controls, group in bg controls, and bg controls itself. Controls activated=" +
						controlsUpdated.getValue() + ", bg controls group activated=" +
						controlInbgControlsUpdated.getValue() +
						", bg controls activated=" + bgControlsUpdated.getValue(),
				true,
				controlsUpdated.getValue() && controlInbgControlsUpdated.getValue() && bgControlsUpdated.getValue()
		);
		assertEquals(display, groupInControls.getDisplay());
		assertEquals(groupInBackground, groupInControls.getHolder());
	}

}
