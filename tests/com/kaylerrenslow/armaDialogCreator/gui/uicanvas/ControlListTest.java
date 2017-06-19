package com.kaylerrenslow.armaDialogCreator.gui.uicanvas;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 06/18/2017 */
public class ControlListTest {

	//todo add change listener tests

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

		list.set(2, replacement);
		assertEquals(replacement, list.get(2));
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