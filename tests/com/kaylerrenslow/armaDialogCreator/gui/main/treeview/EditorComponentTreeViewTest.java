/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaUIScale;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ScreenDimension;
import javafx.application.Application;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 Created on 08/08/2016. */
public class EditorComponentTreeViewTest extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {

	}

	private static class SpecReg implements SpecificationRegistry {

		static final SpecReg INSTANCE = new SpecReg();

		@Nullable
		@Override
		public Macro findMacroByKey(@NotNull String macroKey) {
			return null;
		}

		@Nullable
		@Override
		public ControlClass findControlClassByName(@NotNull String className) {
			return null;
		}

		@Nullable
		@Override
		public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
			return null;
		}

		@Override
		public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch) {

		}
	}

	private static class TestArmaControlClass extends ArmaControl {

		public TestArmaControlClass() {
			super(ControlType.STATIC, "", ArmaControlSpecRequirement.TRIVIAL, new ArmaResolution(ScreenDimension.D960, ArmaUIScale.DEFAULT), RendererLookup.TEST, new Env() {
				@Override
				public @Nullable Value getValue(String identifier) {
					return null;
				}
			}, SpecReg.INSTANCE);
		}
	}

	private static class TestArmaControlGroupClass extends ArmaControlGroup {

		public TestArmaControlGroupClass() {
			super("", new ArmaResolution(ScreenDimension.D960, ArmaUIScale.DEFAULT), RendererLookup.TEST, new Env() {
				@Override
				public @Nullable Value getValue(String identifier) {
					return null;
				}
			}, SpecReg.INSTANCE);
		}
	}

	@SuppressWarnings("unchecked")
	private static class TestTreeView extends EditorComponentTreeView {

		public TestTreeView() {
			super(true);
			setToDisplay(new ArmaDisplay());
		}

		@Override
		public int getCorrectedIndex(TreeItem parent, @NotNull TreeItem childAdded) {
			return super.getCorrectedIndex(parent, childAdded);
		}

		private TreeItem<? extends TreeItemEntry> getEntry(CellType type) {
			switch (type) {
				case LEAF:
					return new TreeItem<>(new ControlTreeItemEntry(new TestArmaControlClass()));
				case FOLDER:
					return new TreeItem<>(new FolderTreeItemEntry(""));
				case COMPOSITE:
					return new TreeItem<>(new ControlGroupTreeItemEntry(new TestArmaControlGroupClass()));

			}
			throw new IllegalStateException("type not known: " + type.name());
		}

		public TreeItem<? extends TreeItemEntry> _addToRoot(CellType type) {
			TreeItem<? extends TreeItemEntry> item = getEntry(type);
			addChildToRoot(item);
			return item;
		}

		public TreeItem<? extends TreeItemEntry> _addToRoot(CellType type, int index) {
			TreeItem<? extends TreeItemEntry> item = getEntry(type);
			addChildToRoot(index, item);
			return item;
		}

		public TreeItem<? extends TreeItemEntry> _addChildToParent(TreeItem<? extends TreeItemEntry> parent, CellType type, int index) {
			TreeItem<? extends TreeItemEntry> item = getEntry(type);
			addChildToParent(parent, item, index);
			return item;
		}

		public TreeItem<? extends TreeItemEntry> _addChildToParent(TreeItem<? extends TreeItemEntry> parent, CellType type) {
			TreeItem<? extends TreeItemEntry> item = getEntry(type);
			addChildToParent(parent, item);
			return item;
		}

		public TreeItem<? extends TreeItemEntry> _addChildToRoot(CellType type) {
			TreeItem<? extends TreeItemEntry> item = getEntry(type);
			addChildToRoot(item);
			return item;
		}

		public TreeItem<? extends TreeItemEntry> _addChildToRoot(int index, CellType type) {
			TreeItem<? extends TreeItemEntry> item = getEntry(type);
			addChildToRoot(index, item);
			return item;
		}

		public void _removeChild(@NotNull TreeItem parent, @NotNull TreeItem toRemove) {
			super.removeChild(parent, toRemove);
		}
	}

	@BeforeClass
	public static void setupClass() throws InterruptedException {
		Thread t = new Thread("JavaFX testing thread") {
			@Override
			public void run() {
				Application.launch(EditorComponentTreeViewTest.class, "");
			}
		};
		t.setDaemon(true);
		t.start();


	}

	@Test
	public void getCorrectedIndex() throws Exception {
		/*
		* leaf
		* folder
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);
		assertEquals(0, testTreeView.getCorrectedIndex(null, leaf));
	}


	@Test
	public void getCorrectedIndex1() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2 - test
		*
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);

		assertEquals(1, testTreeView.getCorrectedIndex(null, leaf2));
	}


	@Test
	public void getCorrectedIndex2() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3 - test
		*
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		assertEquals(2, testTreeView.getCorrectedIndex(null, leaf3));
	}

	@Test
	public void getCorrectedIndex3() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3
		*  + folder2
		*    + leaf4 - test
		*
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		TreeItem folder2 = testTreeView._addChildToParent(folder, CellType.FOLDER);
		TreeItem leaf4 = testTreeView._addChildToParent(folder2, CellType.LEAF);


		assertEquals(3, testTreeView.getCorrectedIndex(null, leaf4));
	}

	@Test
	public void getCorrectedIndex4() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3
		*  + folder2
		*    + leaf4
		*    + group
		*      + leaf5
		*    + leaf6 - test
		*
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		TreeItem folder2 = testTreeView._addChildToParent(folder, CellType.FOLDER);
		TreeItem leaf4 = testTreeView._addChildToParent(folder2, CellType.LEAF);

		TreeItem group = testTreeView._addChildToParent(folder2, CellType.COMPOSITE);
		TreeItem leaf5 = testTreeView._addChildToParent(group, CellType.LEAF);

		TreeItem leaf6 = testTreeView._addChildToParent(folder2, CellType.LEAF);


		assertEquals(5, testTreeView.getCorrectedIndex(null, leaf6));
	}

	@Test
	public void getCorrectedIndex5() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3
		*  + folder2
		*    + leaf4
		*    + group
		*      + leaf5
		*      + leaf6 - test
		*
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		TreeItem folder2 = testTreeView._addChildToParent(folder, CellType.FOLDER);
		TreeItem leaf4 = testTreeView._addChildToParent(folder2, CellType.LEAF);

		TreeItem group = testTreeView._addChildToParent(folder2, CellType.COMPOSITE);
		TreeItem leaf5 = testTreeView._addChildToParent(group, CellType.LEAF);

		TreeItem leaf6 = testTreeView._addChildToParent(group, CellType.LEAF);

		assertEquals(1, testTreeView.getCorrectedIndex(group, leaf6));
	}

	@Test
	public void getCorrectedIndex6() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3
		*  + folder2
		*    + leaf4
		*    + group
		*      + leaf5
		*      + leaf6
		*      + folder3
		*        + group2
		*      + leaf7 - test
		*
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		TreeItem folder2 = testTreeView._addChildToParent(folder, CellType.FOLDER);
		TreeItem leaf4 = testTreeView._addChildToParent(folder2, CellType.LEAF);

		TreeItem group = testTreeView._addChildToParent(folder2, CellType.COMPOSITE);
		TreeItem leaf5 = testTreeView._addChildToParent(group, CellType.LEAF);

		TreeItem leaf6 = testTreeView._addChildToParent(group, CellType.LEAF);
		TreeItem folder3 = testTreeView._addChildToParent(group, CellType.FOLDER);
		TreeItem group2 = testTreeView._addChildToParent(folder3, CellType.COMPOSITE);

		TreeItem leaf7 = testTreeView._addChildToParent(group, CellType.LEAF);

		assertEquals(3, testTreeView.getCorrectedIndex(group, leaf7));
	}

	@Test
	public void getCorrectedIndex7() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3
		*  + folder2
		*    + leaf4
		*    + group
		*      + leaf5
		*      + leaf6
		*      + folder3
		*        + group2 - test
		*      + leaf7
		*
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		TreeItem folder2 = testTreeView._addChildToParent(folder, CellType.FOLDER);
		TreeItem leaf4 = testTreeView._addChildToParent(folder2, CellType.LEAF);

		TreeItem group = testTreeView._addChildToParent(folder2, CellType.COMPOSITE);
		TreeItem leaf5 = testTreeView._addChildToParent(group, CellType.LEAF);

		TreeItem leaf6 = testTreeView._addChildToParent(group, CellType.LEAF);
		TreeItem folder3 = testTreeView._addChildToParent(group, CellType.FOLDER);
		TreeItem group2 = testTreeView._addChildToParent(folder3, CellType.COMPOSITE);

		TreeItem leaf7 = testTreeView._addChildToParent(group, CellType.LEAF);

		assertEquals(2, testTreeView.getCorrectedIndex(group, group2));
	}

	@Test
	public void getCorrectedIndex8() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3
		*  + folder2
		*    + leaf4 - test
		*    + group
		*      + leaf5
		*      + leaf6
		*      + folder3
		*        + group2
		*      + leaf7
		*
		*/
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		TreeItem folder2 = testTreeView._addChildToParent(folder, CellType.FOLDER);
		TreeItem leaf4 = testTreeView._addChildToParent(folder2, CellType.LEAF);

		TreeItem group = testTreeView._addChildToParent(folder2, CellType.COMPOSITE);
		TreeItem leaf5 = testTreeView._addChildToParent(group, CellType.LEAF);

		TreeItem leaf6 = testTreeView._addChildToParent(group, CellType.LEAF);
		TreeItem folder3 = testTreeView._addChildToParent(group, CellType.FOLDER);
		TreeItem group2 = testTreeView._addChildToParent(folder3, CellType.COMPOSITE);

		TreeItem leaf7 = testTreeView._addChildToParent(group, CellType.LEAF);

		assertEquals(3, testTreeView.getCorrectedIndex(null, leaf4));
	}

	@Test
	public void getCorrectedIndex9() throws Exception {
		/*
		* leaf
		* folder
		*  + leaf2
		*  + leaf3
		*  + folder2
		*    + leaf4
		*    + group
		*      + leaf5
		*      + leaf6
		*      + folder3
		*        + group2
		*      + leaf7
		*    + leaf8 - test
		*
		*/
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem folder = testTreeView._addChildToRoot(CellType.FOLDER);

		TreeItem leaf2 = testTreeView._addChildToParent(folder, CellType.LEAF);
		TreeItem leaf3 = testTreeView._addChildToParent(folder, CellType.LEAF);

		TreeItem folder2 = testTreeView._addChildToParent(folder, CellType.FOLDER);
		TreeItem leaf4 = testTreeView._addChildToParent(folder2, CellType.LEAF);

		TreeItem group = testTreeView._addChildToParent(folder2, CellType.COMPOSITE);
		TreeItem leaf5 = testTreeView._addChildToParent(group, CellType.LEAF);

		TreeItem leaf6 = testTreeView._addChildToParent(group, CellType.LEAF);
		TreeItem folder3 = testTreeView._addChildToParent(group, CellType.FOLDER);
		TreeItem group2 = testTreeView._addChildToParent(folder3, CellType.COMPOSITE);

		TreeItem leaf7 = testTreeView._addChildToParent(group, CellType.LEAF);

		TreeItem leaf8 = testTreeView._addChildToParent(folder2, CellType.LEAF);


		assertEquals(5, testTreeView.getCorrectedIndex(null, leaf8));
	}


	@Test
	public void getCorrectedIndexJustRoot0() throws Exception {
		/*
		* leaf -test
		* leaf1
		* leaf2
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem leaf1 = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem leaf2 = testTreeView._addChildToRoot(CellType.LEAF);

		assertEquals(0, testTreeView.getCorrectedIndex(null, leaf));
	}


	@Test
	public void getCorrectedIndexJustRoot1() throws Exception {
		/*
		* leaf
		* leaf1 -test
		* leaf2
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem leaf1 = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem leaf2 = testTreeView._addChildToRoot(CellType.LEAF);

		assertEquals(1, testTreeView.getCorrectedIndex(null, leaf1));
	}


	@Test
	public void getCorrectedIndexJustRoot2() throws Exception {
		/*
		* leaf
		* leaf1
		* leaf2 -test
		* */
		TestTreeView testTreeView = new TestTreeView();
		TreeItem leaf = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem leaf1 = testTreeView._addChildToRoot(CellType.LEAF);
		TreeItem leaf2 = testTreeView._addChildToRoot(CellType.LEAF);

		assertEquals(2, testTreeView.getCorrectedIndex(null, leaf2));
	}
}