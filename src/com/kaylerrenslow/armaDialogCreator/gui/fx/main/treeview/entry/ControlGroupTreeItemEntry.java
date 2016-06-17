package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Used to depict a control group in a TreeView
 Created on 06/07/2016. */
public class ControlGroupTreeItemEntry extends ControlTreeItemEntry implements GroupedTreeItemEntry {

	private final ArmaControlGroup controlGroup;

	public ControlGroupTreeItemEntry(@NotNull ArmaControlGroup group) {
		super(group);
		this.controlGroup = group;
	}

	public ArmaControlGroup getControlGroup() {
		return controlGroup;
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
