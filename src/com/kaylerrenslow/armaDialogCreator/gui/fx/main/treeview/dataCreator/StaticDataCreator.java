package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.dataCreator;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemData;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlTreeItemData;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;

/**
 Created by Kayler on 06/19/2016.
 */
public class StaticDataCreator implements TreeItemDataCreator<TreeItemEntry> {
	private static int id = 0; //delete this later on as its for testing

	public static final StaticDataCreator INSTANCE = new StaticDataCreator();

	@Override
	public TreeItemData<TreeItemEntry> createNew(CellType cellType) {
		StaticControl control = new StaticControl("static_control" + id, 0, ControlStyle.CENTER, 0, 0, 1, 1, ArmaDialogCreator.getCanvasView().getCurrentResolution());
		id++;
		ControlTreeItemEntry entry = new ControlTreeItemEntry(control);
		return new ControlTreeItemData(entry);
	}
}
