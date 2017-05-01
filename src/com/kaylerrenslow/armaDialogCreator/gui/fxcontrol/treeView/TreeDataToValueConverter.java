package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;

import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @see GUITreeStructure#getStructure(TreeView, TreeDataToValueConverter)
 @since 05/01/2017 */
public interface TreeDataToValueConverter<Td extends TreeItemData, Tv> {
	@Nullable Tv convert(@NotNull Td data);

	@NotNull Td convert(@Nullable Tv data, boolean asFolder, @NotNull String name);
}
