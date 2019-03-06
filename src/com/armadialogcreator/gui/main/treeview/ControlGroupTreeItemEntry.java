package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.control.ArmaControlGroup;
import org.jetbrains.annotations.NotNull;

/**
 Used to depict a control group in a TreeView

 @author Kayler
 @since 06/07/2016. */
public class ControlGroupTreeItemEntry extends ControlTreeItemEntry {

	private final ArmaControlGroup controlGroup;

	public ControlGroupTreeItemEntry(@NotNull ArmaControlGroup group) {
		super(new DefaultControlTreeItemGraphic(), group);
		this.controlGroup = group;
	}

	@NotNull
	public ArmaControlGroup getControlGroup() {
		return controlGroup;
	}
}
