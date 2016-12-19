package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;

/**
 Created by Kayler on 05/15/2016.
 */
public enum CellType {
	/** Can not have children */
	LEAF,
	/** Can have children. */
	FOLDER,
	/** Can have children. From a technical standpoint, folders behave no differently than composite. However, folders are meant for organization while composite is meant to be used as like a "item group" */
	COMPOSITE
}
