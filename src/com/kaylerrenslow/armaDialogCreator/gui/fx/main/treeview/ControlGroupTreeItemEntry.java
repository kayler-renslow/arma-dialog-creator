package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Used to depict a control group in a TreeView
 Created on 06/07/2016. */
public class ControlGroupTreeItemEntry extends ControlTreeItemEntry {

	private final ArmaControlGroup controlGroup;

	public ControlGroupTreeItemEntry(@NotNull ArmaControlGroup group) {
		super(CellType.COMPOSITE, EditorComponentTreeView.createCompositeIcon(), group);
		this.controlGroup = group;
	}

	public ArmaControlGroup getControlGroup() {
		return controlGroup;
	}
}
