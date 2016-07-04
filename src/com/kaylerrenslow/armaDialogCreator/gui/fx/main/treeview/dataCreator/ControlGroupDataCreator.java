package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.dataCreator;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ControlGroupControl;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.ControlGroupTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupDataCreator implements TreeItemDataCreator<TreeItemEntry> {
	private static int id = 0; //delete this later on as its for testing

	public static final ControlGroupDataCreator INSTANCE = new ControlGroupDataCreator();

	@Override
	public TreeItemEntry createNew(CellType cellType) {
		return new ControlGroupTreeItemEntry(new ControlGroupControl("controlGroup" + (id++), ArmaDialogCreator.getCanvasView().getCurrentResolution()));
	}
}
