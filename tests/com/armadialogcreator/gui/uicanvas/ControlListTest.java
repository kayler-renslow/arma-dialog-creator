package com.armadialogcreator.gui.uicanvas;

import com.armadialogcreator.canvas.ControlList;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 06/18/2017 */
public class ControlListTest {

	@Test
	public void changeListener_remove() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		Reference<Boolean> visited = new Reference<>(false);

		list.addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				if (controlList != list) {
					assertEquals(list, controlList);
					return;
				}
				if (!change.wasRemoved()) {
					assertEquals(true, false);
					return;
				}
				ListObserverChangeRemove<TestCanvasControl> removed = change.getRemoved();
				if (removed.getRemoved() != control0) {
					assertEquals("Expected the removed control to be control0", control0, removed.getRemoved());
					return;
				}
				if (removed.getIndex() != 0) {
					assertEquals("Expected the removal index to be 0", 0, removed.getIndex());
					return;
				}

				visited.setValue(true);
			}
		});

		list.remove(control0);

		assertEquals(true, visited.getValue());
		assertEquals(null, control0.getHolder());
	}

	@Test
	public void changeListener_set() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);

		Reference<Boolean> visited = new Reference<>(false);

		list.addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				if (controlList != list) {
					assertEquals(list, controlList);
					return;
				}
				if (!change.wasSet()) {
					assertEquals(true, false);
					return;
				}
				ListObserverChangeSet<TestCanvasControl> set = change.getSet();
				if (set.getIndex() != 0) {
					assertEquals("Expected the set index to be 0", 0, set.getIndex());
					return;
				}
				if (set.getNew() != control2) {
					assertEquals("Expected control2 to be the new control", control2, set.getNew());
					return;
				}
				if (set.getOld() != control0) {
					assertEquals("Expected control0 to be the old control", control0, set.getOld());
					return;
				}

				visited.setValue(true);
			}
		});

		list.set(0, control2);

		assertEquals(true, visited.getValue());
		assertEquals(null, control0.getHolder());
		assertEquals(holder, control2.getHolder());
	}

	@Test
	public void changeListener_add() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);

		Reference<Boolean> visited = new Reference<>(false);

		list.addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				if (controlList != list) {
					assertEquals(list, controlList);
					return;
				}
				if (!change.wasAdded()) {
					assertEquals(true, false);
					return;
				}
				ListObserverChangeAdd<TestCanvasControl> added = change.getAdded();
				if (added.getAdded() != control2) {
					assertEquals(control2, added.getAdded());
					return;
				}
				if (added.getIndex() != 2) {
					assertEquals("Expected destination index to be 1", 2, added.getIndex());
					return;
				}

				visited.setValue(true);
			}
		});

		list.add(control2);

		assertEquals(true, visited.getValue());
		assertEquals(holder, control2.getHolder());
	}

	@Test
	public void changeListener_move1() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		Reference<Boolean> visited = new Reference<>(false);

		list.addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				if (controlList != list) {
					assertEquals(list, controlList);
					return;
				}
				if (!change.wasMoved()) {
					assertEquals(true, false);
					return;
				}
				ListObserverChangeMove<TestCanvasControl> moved = change.getMoved();
				if (moved.getDestinationList() != holder.getControls()) {
					assertEquals(holder.getControls(), moved.getDestinationList());
					return;
				}
				if (moved.getDestinationIndex() != 1) {
					assertEquals("Expected destination index to be 1", 1, moved.getDestinationIndex());
					return;
				}
				if (moved.getMoved() != control0) {
					assertEquals("Expected moved control to be control0", control0, moved.getMoved());
					return;
				}
				if (moved.getOldList() != holder.getControls()) {
					assertEquals(holder.getControls(), moved.getOldList());
					return;
				}
				if (moved.getOldIndex() != 0) {
					assertEquals("Expected old index to be 0", 0, moved.getOldIndex());
					return;
				}
				visited.setValue(true);
			}
		});

		list.move(0, 1);

		assertEquals(true, visited.getValue());
		assertEquals(holder, control0.getHolder());
		assertEquals(holder, control1.getHolder());
	}

	@Test
	public void changeListener_move2() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list2.add(new TestCanvasControl("list2 control"));

		list.add(control0);
		list.add(control1);
		list.add(control2);

		Reference<Boolean> visited = new Reference<>(false);

		list.addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				assertEquals(list, controlList);
				assertEquals(true, change.wasMoved());
				ListObserverChangeMove<TestCanvasControl> moved = change.getMoved();
				assertEquals(holder2.getControls(), moved.getDestinationList());
				assertEquals("Expected destination index to be 1", 1, moved.getDestinationIndex());
				assertEquals("Expected moved control to be control0", control0, moved.getMoved());
				assertEquals(holder.getControls(), moved.getOldList());
				assertEquals("Expected old index to be 0", 0, moved.getOldIndex());
				visited.setValue(true);
			}
		});
		list2.addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				assertEquals(list2, controlList);
			}
		});

		list.move(control0, list2);

		assertEquals(true, visited.getValue());
		assertEquals(list2.getHolder(), control0.getHolder());
	}

	@Test
	public void changeListener_move_testUpdateOrder() throws Exception {
		//make sure that the entry update happens first

		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		Reference<Boolean> visited = new Reference<>(false);

		list.addChangeListener(new ListObserverListener<TestCanvasControl>() {
			@Override
			public void onChanged(@NotNull ListObserver<TestCanvasControl> controlList, @NotNull ListObserverChange<TestCanvasControl> change) {
				if (!visited.getValue() && !change.getMoved().isEntryUpdate()) {
					throw new IllegalStateException("expected the entry update to happen first");
				}
				visited.setValue(change.getMoved().isEntryUpdate());
			}
		});

		list.move(control0, 1);
	}

	@Test
	public void clear() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.clear();
		assertEquals(0, list.size());
		assertEquals(null, control0.getHolder());
		assertEquals(null, control1.getHolder());
		assertEquals(null, control2.getHolder());
	}

	@Test
	public void get() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.clear();
		assertEquals(0, list.size());
	}

	@Test
	public void set_0() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");
		TestCanvasControl replacement = new TestCanvasControl("replacement");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.set(0, replacement);
		assertEquals(replacement, list.get(0));
		assertEquals(null, control0.getHolder());
		assertEquals(holder, replacement.getHolder());
	}

	@Test
	public void set_x() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");
		TestCanvasControl replacement = new TestCanvasControl("replacement");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.set(1, replacement);
		assertEquals(replacement, list.get(1));
		assertEquals(null, control1.getHolder());
		assertEquals(holder, replacement.getHolder());
	}

	@Test
	public void add() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");
		TestCanvasControl added = new TestCanvasControl("added");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.add(added);
		assertEquals(added, list.get(list.size() - 1));
		assertEquals(holder, added.getHolder());
	}

	@Test
	public void add1_0() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");
		TestCanvasControl added = new TestCanvasControl("added");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.add(0, added);
		assertEquals(added, list.get(0));
		assertEquals(holder, added.getHolder());
	}

	@Test
	public void add1_x() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");
		TestCanvasControl added = new TestCanvasControl("added");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.add(1, added);
		assertEquals(added, list.get(1));
		assertEquals(holder, added.getHolder());
	}

	@Test
	public void remove() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		TestCanvasControl removed = list.remove(0);
		assertEquals(control0, removed);
		assertEquals(null, removed.getHolder());
	}

	@Test
	public void remove1() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		boolean removed = list.remove(control0);
		assertEquals(true, removed && !list.contains(control0));
		assertEquals(null, control0.getHolder());
	}

	@Test
	public void contains_true() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		assertEquals(true, list.contains(control0));
	}

	@Test
	public void contains_false() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		assertEquals(false, list.contains(new TestCanvasControl("newControl")));
	}

	@Test
	public void move_index_index1() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(0, 1);

		assertEquals(control0, list.get(1));
		assertEquals(holder, control0.getHolder());
	}

	@Test
	public void move_index_index2() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(2, 0);

		assertEquals(control2, list.get(0));
		assertEquals(holder, control2.getHolder());
	}

	@Test
	public void move_index_index3() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(0, 1);

		assertEquals(control2, list.get(2));
		assertEquals(holder, control2.getHolder());
	}

	@Test
	public void move_index_index4() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(0, 1);

		assertEquals(control1, list.get(0));
		assertEquals(holder, control1.getHolder());
	}

	//
	//
	//

	@Test
	public void move_control_index1() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, 1);

		assertEquals(control0, list.get(1));
		assertEquals(holder, control0.getHolder());
	}

	@Test
	public void move_control_index2() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control2, 0);

		assertEquals(control2, list.get(0));
		assertEquals(holder, control2.getHolder());
	}

	@Test
	public void move_control_index3() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, 1);

		assertEquals(control2, list.get(2));
		assertEquals(holder, control0.getHolder());
		assertEquals(holder, control1.getHolder());
	}

	@Test
	public void move_control_index4() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, 1);

		assertEquals(control1, list.get(0));
		assertEquals(holder, control0.getHolder());
		assertEquals(holder, control1.getHolder());
	}

	//
	//
	//

	@Test
	public void move_control_list1() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, list2);

		assertEquals(control1, list.get(0));
		assertEquals(holder2, control0.getHolder());
	}

	@Test
	public void move_control_list2() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control2, list2);

		assertEquals(control2, list2.get(0));
		assertEquals(holder2, control2.getHolder());
	}

	@Test
	public void move_control_list3() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, list2);

		assertEquals(control2, list.get(1));
		assertEquals(holder2, control0.getHolder());
	}

	@Test
	public void move_control_list4() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list2.add(new TestCanvasControl("newControl"));

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, list2);

		assertEquals(control0, list2.get(1));
		assertEquals(holder2, control0.getHolder());
	}

	//
	//
	//

	@Test
	public void move_control_list_index1() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, list2, 0);

		assertEquals(control1, list.get(0));
		assertEquals(holder2, control0.getHolder());
	}

	@Test
	public void move_control_list_index2() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list2.add(new TestCanvasControl("newControl"));

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control2, list2, 0);

		assertEquals(control2, list2.get(0));
		assertEquals(holder2, control2.getHolder());
	}

	@Test
	public void move_control_list_index3() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		TestCanvasControl list2Control = new TestCanvasControl("list2Control");

		list2.add(list2Control);

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(control0, list2, 0);

		assertEquals(list2Control, list2.get(1));
		assertEquals(holder2, control0.getHolder());
	}

	//
	//
	//

	@Test
	public void move_index_list_index1() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(0, list2, 0);

		assertEquals(control1, list.get(0));
		assertEquals(holder2, control0.getHolder());
	}

	@Test
	public void move_index_list_index2() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		list2.add(new TestCanvasControl("newControl"));

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(2, list2, 0);

		assertEquals(control2, list2.get(0));
		assertEquals(holder2, control2.getHolder());
	}

	@Test
	public void move_index_list_index3() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasHolder holder2 = new TestCanvasHolder();
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = holder2.getControls();

		TestCanvasControl list2Control = new TestCanvasControl("list2Control");

		list2.add(list2Control);

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.move(0, list2, 0);

		assertEquals(list2Control, list2.get(1));
		assertEquals(holder2, control0.getHolder());
	}


	@Test
	public void deepIterator() throws Exception {
		TestCanvasControl control0 = new TestCanvasControl("control0");
		TestCanvasControl control1 = new TestCanvasControl("control1");
		TestCanvasControl control2 = new TestCanvasControl("control2");

		TestCanvasHolder holder = new TestCanvasHolder();
		TestCanvasControlGroup controlGroup = new TestCanvasControlGroup("controlGroup");
		ControlList<TestCanvasControl> list = holder.getControls();
		ControlList<TestCanvasControl> list2 = controlGroup.getControls();

		TestCanvasControl list2Control = new TestCanvasControl("list2Control");
		list2.add(list2Control);

		list.add(control0);
		list.add(control1);
		list.add(control2);

		list.add(controlGroup);

		List<TestCanvasControl> toVisit = new LinkedList<>();
		toVisit.add(control0);
		toVisit.add(control1);
		toVisit.add(control2);
		toVisit.add(controlGroup);
		toVisit.add(list2Control);

		list.deepIterator().forEach(new Consumer<TestCanvasControl>() {
			@Override
			public void accept(TestCanvasControl testCanvasControl) {
				toVisit.remove(testCanvasControl);
			}
		});

		assertEquals("Expected to visit every control. Not visited=" + toVisit, 0, toVisit.size());
	}

}
