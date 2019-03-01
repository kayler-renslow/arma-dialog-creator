package com.armadialogcreator.gui.fxcontrol.treeView;

import com.armadialogcreator.gui.main.treeview.UINodeTreeItemData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/08/2016.
 */
public interface TreeItemDataCreator<Tv, Td extends UINodeTreeItemData> {
	/**
	 Returns a new instance of a TreeItemData instance.

	 @return new instance, or null if abort creation of TreeItemData
	 */
	@Nullable Td createNew(@NotNull EditableTreeView<Tv, Td> treeView);
}
