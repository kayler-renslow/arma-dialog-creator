package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/08/2016.
 */
public interface TreeItemDataCreator<E extends TreeItemData> {
	/**
	 Returns a new instance of a TreeItemData instance.

	 @param cellType the cell type that is requested to be created.
	 @return new instance, or null if abort creation of TreeItemData
	 */
	@Nullable
	E createNew(@NotNull CellType cellType, @NotNull EditableTreeView<E> treeView);
}
