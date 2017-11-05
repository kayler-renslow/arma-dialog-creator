package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView;

import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @see GUITreeStructure#getStructure(TreeView, TreeDataToValueConverter)
 @since 05/01/2017 */
public interface TreeDataToValueConverter<Td extends TreeItemData, Tv> {
	/**
	 Convert an instance of {@link TreeItemData} into {@link Tv}

	 @return null or the {@link Tv} instance
	 */
	@Nullable Tv convert(@NotNull Td data);

	/**
	 Convert {@link Tv} into an instance of {@link TreeItemData}

	 @param data the {@link Tv} instance
	 @param asFolder true if the {@link Tv} is for a folder, false if it isn't
	 @param name name of the node for the {@link TreeItemData}
	 */
	@NotNull Td convert(@Nullable Tv data, boolean asFolder, @NotNull String name);
}
