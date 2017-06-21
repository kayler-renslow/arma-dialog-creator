package com.kaylerrenslow.armaDialogCreator.gui.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.GUITreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.main.editor.UICanvasConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.TreeItemEntry;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/20/2016.
 */
public interface CanvasView {

	@NotNull UICanvasConfiguration getConfiguration();

	/**
	 Set the background image of the canvas

	 @param imgPath the path to the image, or null to remove the image
	 */
	void setCanvasBackgroundToImage(@Nullable String imgPath);

	/** Fetches the new ui colors and repaints the canvas */
	void updateCanvas();

	/** @return the background image of the canvas, or null if not set */
	@Nullable ImagePattern getCanvasBackgroundImage();

	/** @return the background color of the canvas */
	@NotNull Color getCanvasBackgroundColor();

	/**
	 Update the Absolute region box. For each parameter: -1 to leave unchanged, 0 for false, 1 for true

	 @param alwaysFront true if the region should always be rendered last, false if it should be rendered first
	 @param showing true the region is showing, false if not
	 */
	void updateAbsRegion(int alwaysFront, int showing);

	void setTreeStructure(boolean backgroundTree, @Nullable TreeStructure<ArmaControl> treeStructure);

	@NotNull GUITreeStructure<ArmaControl> getMainControlsTreeStructure();

	@NotNull GUITreeStructure<ArmaControl> getBackgroundControlsTreeStructure();

	@NotNull EditableTreeView<ArmaControl, ? extends TreeItemEntry> getMainControlTreeView();

	@NotNull EditableTreeView<ArmaControl, ? extends TreeItemEntry> getBackgroundControlTreeView();

	/** @return true if treeView == {@link #getBackgroundControlTreeView()}, false otherwise */
	default boolean isBackgroundTreeView(@NotNull EditableTreeView<ArmaControl, ? extends TreeItemEntry> treeView) {
		return treeView == getBackgroundControlTreeView();
	}
}
