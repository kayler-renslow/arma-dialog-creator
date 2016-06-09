package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Used to depict a control group in a TreeView
 Created on 06/07/2016. */
public class ControlGroupTreeItemEntry extends GroupedTreeItemEntry {

	private final ArmaControlGroup controlGroup;

	public ControlGroupTreeItemEntry(@NotNull ArmaControlGroup group) {
		super(toTreeItems(group));
		this.controlGroup = group;
	}

	private static TreeItemEntry[] toTreeItems(ArmaControlGroup group) {
		TreeItemEntry[] treeItems = new TreeItemEntry[group.getControls().size()];
		int i = 0;
		for (ArmaControl control : group.getControls()) {
			treeItems[i++] = new ControlTreeItemEntry(control);
		}
		return treeItems;
	}

	@Override
	public @NotNull String getTreeItemText() {
		return controlGroup.getRenderText();
	}

	@Override
	public boolean isPhantom() {
		return false;
	}
}
