package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemData;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.TreeItemEntry;

/**
 Created by Kayler on 06/08/2016.
 */
class ControlTreeItemData extends TreeItemData<TreeItemEntry> {
	private TreeItemControlGraphic graphic = (TreeItemControlGraphic) getGraphic();
	private ControlTreeItemEntry controlTreeItemEntry;

	ControlTreeItemData(ControlTreeItemEntry controlTreeItemEntry) {
		super(controlTreeItemEntry.getTreeItemText(), CellType.LEAF, controlTreeItemEntry, new TreeItemControlGraphic());
		this.controlTreeItemEntry = controlTreeItemEntry;
		graphic.init(this);
	}

	/** Set whether or not the control is visible or not (updates the radio button as well) */
	public void setControlIsVisible(boolean visible) {
		controlTreeItemEntry.setGhost(!visible);
		graphic.updateVisibilityRadioButton(visible);
	}

	void updateVisibilityFromButton(boolean visible) {
		controlTreeItemEntry.setGhost(!visible);
	}

	String getControlTypeText() {
		return controlTreeItemEntry.getControlTypeText();
	}
}
